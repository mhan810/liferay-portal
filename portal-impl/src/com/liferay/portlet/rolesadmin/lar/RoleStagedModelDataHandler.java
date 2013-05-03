/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.rolesadmin.lar;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Permission;
import com.liferay.portal.model.ResourceAction;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.model.ResourceTypePermission;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.impl.PermissionImpl;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ResourceActionLocalServiceUtil;
import com.liferay.portal.service.ResourceBlockLocalServiceUtil;
import com.liferay.portal.service.ResourceBlockServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionServiceUtil;
import com.liferay.portal.service.ResourceTypePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.persistence.ResourcePermissionFinderUtil;
import com.liferay.portal.service.persistence.ResourceTypePermissionFinderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Mendez Gonzalez
 */
public class RoleStagedModelDataHandler
	extends BaseStagedModelDataHandler<Role> {

	public static final String[] CLASS_NAMES = {Role.class.getName()};

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	protected void clearEixistingPermissions(Role role)
		throws PortalException, SystemException {

		long roleId = role.getRoleId();

		Group systemGroup = GroupLocalServiceUtil.getCompanyGroup(
			role.getCompanyId());
		long systemGroupId = systemGroup.getGroupId();

		Group userGroup = GroupLocalServiceUtil.getGroup(
			role.getCompanyId(), GroupConstants.USER_PERSONAL_SITE);
		long userGroupId = userGroup.getGroupId();

		ResourcePermissionFinderUtil.resetResourcePermissions(
			roleId, systemGroupId, userGroupId);

		ResourceTypePermissionFinderUtil.resetResourceTypePermissions(
			roleId, systemGroupId, userGroupId);

	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Role role)
		throws Exception {

		Element roleElement = portletDataContext.getExportDataElement(role);

		Document exportedDocument = SAXReaderUtil.createDocument();

		Element rootElement = exportedDocument.addElement("root");

		Element permissionsElement = rootElement.addElement("permissions");

		List<Permission> permissions = getPermissions(role);

		exportPermissionsXML(permissionsElement, permissions);

		String permissionsPath = ExportImportPathUtil.getModelPath(
			role, "permissions");

		ZipWriter zipWriter = portletDataContext.getZipWriter();

		zipWriter.addEntry(permissionsPath, exportedDocument.formattedString());

		portletDataContext.addClassedModel(
			roleElement, ExportImportPathUtil.getModelPath(role), role,
			RolesAdminPortletDataHandler.NAMESPACE);

	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Role role)
		throws Exception {

		Role importedRole = importRole(portletDataContext, role);

		ZipReader zipReader = portletDataContext.getZipReader();

		String permissionsPath = ExportImportPathUtil.getModelPath(
			role, "permissions");

		String exported = zipReader.getEntryAsString(permissionsPath);

		Document exportedDocument = SAXReaderUtil.read(exported);

		Element rootElement = exportedDocument.getRootElement();

		Element permissionsElement = rootElement.element("permissions");

		importPermissions(portletDataContext, permissionsElement, importedRole);

	}

	protected void exportPermissionsXML(
			Element element, List<Permission> permissions)
		throws SystemException {

		for (Permission permission : permissions) {
			Element permissionElement = element.addElement("permission");

			permissionElement.addAttribute(
				"resource", String.valueOf(permission.getName()));
			permissionElement.addAttribute(
				"actionId", permission.getActionId());
			permissionElement.addAttribute(
				"scope", String.valueOf(permission.getScope()));

			if (permission.getScope() == ResourceConstants.SCOPE_GROUP) {
				Group group = GroupLocalServiceUtil.fetchGroup(
					Long.valueOf(permission.getPrimKey()));

				if (group.isUserPersonalSite()) {
					permissionElement.addAttribute(
						"groupType", GroupConstants.USER_PERSONAL_SITE);
				}
				else if (group.isCompany()) {
					permissionElement.addAttribute(
						"groupType", GroupConstants.TYPE_SITE_SYSTEM_LABEL);
				}
			}
		}
	}

	protected List<Permission> getPermissions(Role role)
		throws SystemException {

		int[] scopes;

		if (role.getType() == RoleConstants.TYPE_REGULAR) {
			scopes = new int[] {
				ResourceConstants.SCOPE_COMPANY, ResourceConstants.SCOPE_GROUP};
		}
		else if ((role.getType() == RoleConstants.TYPE_ORGANIZATION) ||
				 (role.getType() == RoleConstants.TYPE_SITE)) {

			scopes = new int[] {ResourceConstants.SCOPE_GROUP_TEMPLATE};
		}
		else {
			throw new UnsupportedOperationException(
				"Unable to export role of type " + role.getTypeLabel());
		}

		List<Permission> permissions = new ArrayList<Permission>();

		List<ResourcePermission> resourcePermissions =
			ResourcePermissionLocalServiceUtil.getRoleResourcePermissions(
				role.getRoleId(), scopes, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (ResourcePermission resourcePermission : resourcePermissions) {
			boolean exportable = isExportable(resourcePermission);

			if (exportable) {
				List<ResourceAction> resourceActions =
					ResourceActionLocalServiceUtil.getResourceActions(
						resourcePermission.getName());

				for (ResourceAction resourceAction : resourceActions) {
					if (ResourcePermissionLocalServiceUtil.hasActionId(
							resourcePermission, resourceAction)) {

						Permission permission = new PermissionImpl();

						permission.setName(resourcePermission.getName());
						permission.setScope(resourcePermission.getScope());
						permission.setPrimKey(resourcePermission.getPrimKey());
						permission.setActionId(resourceAction.getActionId());

						permissions.add(permission);
					}
				}
			}
		}

		List<ResourceTypePermission> resourceTypePermissions =
			ResourceTypePermissionLocalServiceUtil.
				getRoleResourceTypePermissions(role.getRoleId());

		for (ResourceTypePermission resourceTypePermission :
			resourceTypePermissions) {

			boolean exportable = isExportable(role, resourceTypePermission);

			if (exportable) {
				List<String> actionIds =
					ResourceBlockLocalServiceUtil.getActionIds(
						resourceTypePermission.getName(),
						resourceTypePermission.getActionIds());

				int scope;

				if (role.getType() == RoleConstants.TYPE_REGULAR) {
					if (resourceTypePermission.isCompanyScope()) {
						scope = ResourceConstants.SCOPE_COMPANY;
					}
					else {
						scope = ResourceConstants.SCOPE_GROUP;
					}
				}
				else {
					scope = ResourceConstants.SCOPE_GROUP_TEMPLATE;
				}

				for (String actionId : actionIds) {
					Permission permission = new PermissionImpl();

					permission.setName(resourceTypePermission.getName());
					permission.setScope(scope);
					permission.setPrimKey(
						String.valueOf(resourceTypePermission.getGroupId()));
					permission.setActionId(actionId);

					permissions.add(permission);
				}
			}
		}

		return permissions;
	}

	protected void importPermissions(
			PortletDataContext portletDataContext, Element permissionsElement,
			Role role)
		throws PortalException, SystemException {

		long companyId = role.getCompanyId();
		long scopeGroupId = portletDataContext.getScopeGroupId();
		long roleId = role.getRoleId();

		List<Element> permissionElements = permissionsElement.elements(
			"permission");

		boolean clearExisting = portletDataContext.getBooleanParameter(
			RolesAdminPortletDataHandler.NAMESPACE, "clearExistingPermissions");

		if (clearExisting) {
			clearEixistingPermissions(role);
		}

		for (Element permissionElement : permissionElements) {
			String resource = permissionElement.attributeValue("resource");
			String actionId = permissionElement.attributeValue("actionId");
			int scope = GetterUtil.getInteger(
				permissionElement.attributeValue("scope"));

			if (ResourceBlockLocalServiceUtil.isSupported(resource)) {
				if (scope == ResourceConstants.SCOPE_COMPANY ||
					scope == ResourceConstants.SCOPE_GROUP_TEMPLATE) {

					ResourceBlockServiceUtil.addCompanyScopePermission(
						scopeGroupId, companyId, resource, roleId, actionId);
				}
				else {
					String groupType = permissionElement.attributeValue(
						"groupType");

					if (groupType.equals(GroupConstants.USER_PERSONAL_SITE)) {
						Group group = GroupLocalServiceUtil.getGroup(
							companyId, GroupConstants.USER_PERSONAL_SITE);

						ResourceBlockLocalServiceUtil.addGroupScopePermission(
							companyId, group.getGroupId(), resource, roleId,
							actionId);
					}
					else if (groupType.equals(
								GroupConstants.TYPE_SITE_SYSTEM)) {

						Group group = GroupLocalServiceUtil.getCompanyGroup(
							companyId);

						ResourceBlockLocalServiceUtil.addGroupScopePermission(
							companyId, group.getGroupId(), resource, roleId,
							actionId);
					}
					else {
						throw new UnsupportedOperationException(
							"Unknown group type: " + groupType);
					}
				}
			}
			else {
				if (scope == ResourceConstants.SCOPE_COMPANY) {
					ResourcePermissionServiceUtil.addResourcePermission(
						scopeGroupId, companyId, resource, scope,
						String.valueOf(role.getCompanyId()), roleId, actionId);
				}
				else if (scope == ResourceConstants.SCOPE_GROUP_TEMPLATE) {
					ResourcePermissionServiceUtil.addResourcePermission(
						scopeGroupId, companyId, resource,
						ResourceConstants.SCOPE_GROUP_TEMPLATE,
						String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
						roleId, actionId);
				}
				else {
					String groupType = permissionElement.attributeValue(
						"groupType");

					String primKey = StringPool.BLANK;

					if (Validator.isNotNull(groupType)) {
						if (groupType.equals(
								GroupConstants.TYPE_SITE_SYSTEM_LABEL)) {

							Group group =
								GroupLocalServiceUtil.getCompanyGroup(
									companyId);

							primKey = String.valueOf(group.getGroupId());
						}
						else if (groupType.equals(
									GroupConstants.USER_PERSONAL_SITE)) {

							Group group = GroupLocalServiceUtil.getGroup(
								companyId, GroupConstants.USER_PERSONAL_SITE);

							primKey = String.valueOf(group.getGroupId());
						}
						else {
							throw new UnsupportedOperationException(
								"Unknown group type: " + groupType);
						}
					}

					if (Validator.isNotNull(primKey)) {
						ResourcePermissionServiceUtil.addResourcePermission(
							scopeGroupId, companyId, resource,
							ResourceConstants.SCOPE_GROUP, primKey, roleId,
							actionId);
					}
					else {
						if (_log.isInfoEnabled()) {
							_log.info("Ignoring group type " + groupType);
						}
					}
				}
			}
		}
	}

	protected Role importRole(PortletDataContext portletDataContext, Role role)
		throws Exception {

		long userId = portletDataContext.getUserId(role.getUserUuid());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			role, RolesAdminPortletDataHandler.NAMESPACE);

		Role existingRole = RoleLocalServiceUtil.fetchRoleByUuidAndCompanyId(
			role.getUuid(), portletDataContext.getCompanyId());

		if (existingRole == null) {
			existingRole = RoleLocalServiceUtil.fetchRole(
				portletDataContext.getCompanyId(), role.getName());
		}

		Role importedRole = null;

		if (existingRole == null) {
			serviceContext.setUuid(role.getUuid());

			importedRole = RoleLocalServiceUtil.addRole(
				userId, null, 0, role.getName(), role.getTitleMap(),
				role.getDescriptionMap(), role.getType(), role.getSubtype(),
				serviceContext);
		}
		else {
			importedRole = RoleLocalServiceUtil.updateRole(
				existingRole.getRoleId(), role.getName(), role.getTitleMap(),
				role.getDescriptionMap(), role.getSubtype(), serviceContext);
		}

		portletDataContext.importClassedModel(
			role, importedRole, RolesAdminPortletDataHandler.NAMESPACE);

		return importedRole;
	}

	protected boolean isExportable(ResourcePermission resourcePermission)
		throws SystemException {

		if (resourcePermission.getScope() != ResourceConstants.SCOPE_GROUP) {
			return true;
		}
		else {
			Group group = GroupLocalServiceUtil.fetchGroup(
				Long.valueOf(resourcePermission.getPrimKey()));

			return (group.isCompany() || group.isUserPersonalSite());
		}
	}

	protected boolean isExportable(
			Role role, ResourceTypePermission resourceTypePermission)
		throws SystemException {

		if (role.getType() == RoleConstants.TYPE_REGULAR) {
			if (resourceTypePermission.isCompanyScope()) {
				return true;
			}
			else {
				Group group = GroupLocalServiceUtil.fetchGroup(
					resourceTypePermission.getGroupId());

				return (group.isCompany() || group.isUserPersonalSite());
			}
		}
		else {
			return true;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		RoleStagedModelDataHandler.class);

}
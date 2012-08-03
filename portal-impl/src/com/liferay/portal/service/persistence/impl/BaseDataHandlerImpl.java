/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.service.persistence.impl;

import com.liferay.portal.NoSuchRoleException;
import com.liferay.portal.NoSuchTeamException;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.PortletDataContextListener;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.lar.LayoutCache;
import com.liferay.portal.lar.PermissionExporter;
import com.liferay.portal.lar.XStreamWrapper;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.lar.digest.LarDigestPermission;
import com.liferay.portal.lar.digest.LarDigesterConstants;
import com.liferay.portal.model.AuditedModel;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.ResourcedModel;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.Team;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ResourceBlockLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.TeamLocalServiceUtil;
import com.liferay.portal.service.persistence.BaseDataHandler;
import com.liferay.portal.service.persistence.lar.AssetCategoryDataHandler;
import com.liferay.portal.service.persistence.lar.AssetLinkDataHandler;
import com.liferay.portal.service.persistence.lar.AssetVocabularyDataHandler;
import com.liferay.portal.service.persistence.lar.BookmarksEntryDataHandler;
import com.liferay.portal.service.persistence.lar.BookmarksFolderDataHandler;
import com.liferay.portal.service.persistence.lar.BookmarksPortletDataHandler;
import com.liferay.portal.service.persistence.lar.ImageDataHandler;
import com.liferay.portal.service.persistence.lar.JournalArticleDataHandler;
import com.liferay.portal.service.persistence.lar.JournalStructureDataHandler;
import com.liferay.portal.service.persistence.lar.JournalTemplateDataHandler;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portal.service.persistence.lar.LayoutSetDataHandler;


import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Mate Thurzo
 * @author Daniel Kocsis
 */
public abstract class BaseDataHandlerImpl<T extends BaseModel<T>>
	implements BaseDataHandler<T> {

	public void addZipEntry(ZipWriter writer, String path, T object)
		throws SystemException {

		addZipEntry(writer, path, toXML(object));
	}

	public void addZipEntry(ZipWriter writer, String path, Object object)
		throws SystemException {

		addZipEntry(writer, path, toXML(object));
	}

	public void addZipEntry(ZipWriter writer, String path, String s)
		throws SystemException {

		if (_portletDataContextListener != null) {
			_portletDataContextListener.onAddZipEntry(path);
		}

		try {
			writer.addEntry(path, s);
		}
		catch (IOException ioe) {
			throw new SystemException(ioe);
		}
	}

	public ServiceContext createServiceContext(
		String path, ClassedModel classedModel, String namespace,
		DataHandlerContext context) {

		return createServiceContext(
			null, path, classedModel, namespace, context);
	}

	public LarDigestItem digest(
			T object, DataHandlerContext context)
		throws Exception {

		LarDigestItem item = doDigest(object, context);

		context.getLarDigest().addItem(item);

		String path = getEntityPath(object);

		context.addProcessedPath(path);

		return item;
	}

	public abstract LarDigestItem doDigest(
			T object, DataHandlerContext context)
		throws Exception;

	public abstract void doImportData(
			LarDigestItem item, DataHandlerContext context)
		throws Exception;

	public abstract T getEntity(String classPK);

	public String getEntityPath(T object) {
		if (object instanceof Portlet) {
			Portlet portlet = (Portlet)object;

			return getPortletPath(portlet.getPortletId());
		}

		if (object instanceof BaseModel) {
			BaseModel<T> baseModel = (BaseModel<T>)object;

			Map<String, Object> modelAttributes =
				baseModel.getModelAttributes();

			StringBundler sb = new StringBundler();

			sb.append(StringPool.FORWARD_SLASH);
			sb.append("group");
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(modelAttributes.get("groupId"));
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(baseModel.getModelClassName());
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(baseModel.getPrimaryKeyObj() + ".xml");

			return sb.toString();
		}

		return StringPool.BLANK;
	}

	public String getNamespace() {
		return StringPool.BLANK;
	}

	public String getPermissionResourceName() {
		ParameterizedType type =
			(ParameterizedType)getClass().getGenericSuperclass();

		return type.getClass().getName();
	}

	public XStreamWrapper getXstreamWrapper() {
		if (_xStreamWrapper == null) {
			Object o = PortalBeanLocatorUtil.locate("xStreamWrapper");

			if (o != null) {
				_xStreamWrapper = ((XStreamWrapper)o);
			}
		}

		return _xStreamWrapper;
	}

	public byte[] getZipEntryAsByteArray(ZipReader reader, String path) {
		if (_portletDataContextListener != null) {
			_portletDataContextListener.onGetZipEntry(path);
		}

		return reader.getEntryAsByteArray(path);
	}

	public Object getZipEntryAsObject(ZipReader reader, String path) {
		return fromXML(getZipEntryAsString(reader, path));
	}

	public String getZipEntryAsString(ZipReader reader, String path) {
		if (_portletDataContextListener != null) {
			_portletDataContextListener.onGetZipEntry(path);
		}

		return reader.getEntryAsString(path);
	}

	public void importData(LarDigestItem item, DataHandlerContext context) {
		try {
			if (!context.isPathProcessed(item.getPath())) {
				doImportData(item, context);
			}

			context.addProcessedPath(item.getPath());
		}
		catch (Exception e) {
		}
	}

	public void serialize(LarDigestItem item, DataHandlerContext context) {
		T object = getEntity(item.getClassPK());

		if (object == null) {
			return;
		}

		String path = getEntityPath(object);

		try {
			doSerialize(object, context);

			addPermissions(item.getPermissions(), context);
			addExpando(object, context);
		}
		catch (Exception e) {
		}

	}

	public void setXstreamWrapper(XStreamWrapper xStreamWrapper) {
		_xStreamWrapper = xStreamWrapper;
	}

	protected void addExpando(T object, DataHandlerContext context)
		throws SystemException {

		if (!(object instanceof ClassedModel)) {
			return;
		}

		ClassedModel classedModel = (ClassedModel)object;

		String expandoPath = getEntityPath(object);
		expandoPath = StringUtil.replace(expandoPath, ".xml", "-expando.xml");

		ExpandoBridge expandoBridge = classedModel.getExpandoBridge();

		addZipEntry(context.getZipWriter(), expandoPath, expandoBridge.getAttributes());
	}

	protected void addPermissions(
			List<LarDigestPermission> permissions, DataHandlerContext context)
		throws Exception {

		for (LarDigestPermission permission : permissions) {
			Role role = RoleLocalServiceUtil.getRole(
				context.getCompanyId(), permission.getRoleName());

			String path = getRolePath(role);

			addZipEntry(context.getZipWriter(), path, role);
		}
	}

	protected ServiceContext createServiceContext(
		Element element, String path, ClassedModel classedModel,
		String namespace, DataHandlerContext context) {

		Class<?> clazz = classedModel.getModelClass();
		long classPK = getClassPK(classedModel);

		ServiceContext serviceContext = new ServiceContext();

		// Theme display

		serviceContext.setCompanyId(context.getCompanyId());
		serviceContext.setScopeGroupId(context.getScopeGroupId());

		// Dates

		if (classedModel instanceof AuditedModel) {
			AuditedModel auditedModel = (AuditedModel)classedModel;

			serviceContext.setCreateDate(auditedModel.getCreateDate());
			serviceContext.setModifiedDate(auditedModel.getModifiedDate());
		}

		// Permissions

		if (!MapUtil.getBoolean(
				context.getParameters(), PortletDataHandlerKeys.PERMISSIONS)) {

			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
		}

		// Expando

		String expandoPath = null;

		if (element != null) {
			expandoPath = element.attributeValue("expando-path");
		}
		else {
			expandoPath = getExpandoPath(path);
		}

		if (Validator.isNotNull(expandoPath)) {
			try {
				Map<String, Serializable> expandoBridgeAttributes =
					(Map<String, Serializable>)getZipEntryAsObject(
							context.getZipReader(), expandoPath);

				serviceContext.setExpandoBridgeAttributes(
					expandoBridgeAttributes);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}
			}
		}

		return serviceContext;
	}

	protected void doSerialize(T object, DataHandlerContext context)
		throws Exception {

		String path = getEntityPath(object);

		addZipEntry(context.getZipWriter(), path, object);
	}

	protected Object fromXML(String xml) {
		if (Validator.isNull(xml)) {
			return null;
		}

		return getXstreamWrapper().fromXML(xml);
	}

	protected long getClassPK(ClassedModel classedModel) {
		if (classedModel instanceof ResourcedModel) {
			ResourcedModel resourcedModel = (ResourcedModel)classedModel;

			return resourcedModel.getResourcePrimKey();
		}
		else {
			return (Long)classedModel.getPrimaryKeyObj();
		}
	}

	protected int getDigestAction(T object, DataHandlerContext context) {
		if (object instanceof BaseModel) {
			BaseModel modelObj = (BaseModel)object;

			Map<String, Object> modelAttributes = modelObj.getModelAttributes();

			Date modifiedDate = (Date)modelAttributes.get("modifiedDate");

			if (context.getLastPublishDate().before(modifiedDate)) {
				return LarDigesterConstants.ACTION_UPDATE;
			}

			return LarDigesterConstants.ACTION_ADD;
		}

		return -1;
	}

	protected String getExpandoPath(String path) {
		if (!isValidPath(path)) {
			throw new IllegalArgumentException(
				path + " is located outside of the lar");
		}

		int pos = path.lastIndexOf(".xml");

		if (pos == -1) {
			throw new IllegalArgumentException(
				path + " does not end with .xml");
		}

		return path.substring(0, pos).concat("-expando").concat(
			path.substring(pos));
	}

	protected String getPortletPath(String portletId) {
		StringBundler sb = new StringBundler();

		sb.append(ROOT_PATH_PORTLETS);
		sb.append(portletId);
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(portletId + ".xml");

		return sb.toString();
	}

	protected String getRolePath(Role role) {
		return getRolePath(role.getName());
	}

	protected String getRolePath(String roleName) {
		StringBundler sb = new StringBundler();

		sb.append(StringPool.FORWARD_SLASH);
		sb.append("roles");
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(roleName + ".xml");

		return sb.toString();
	}

	protected String getRootPath(long groupId) {
		return ROOT_PATH_GROUPS + groupId;
	}

	protected String getSourcePortletPath(long groupid, String portletId) {
		return getSourceRootPath(groupid) + ROOT_PATH_PORTLETS + portletId;
	}

	protected String getSourceRootPath(long groupId) {
		return ROOT_PATH_GROUPS + groupId;
	}

	protected void importPermissions(
			String resourceName, String resourcePrimKey,
			List<LarDigestPermission> permissions, DataHandlerContext context)
		throws Exception {

		LayoutCache layoutCache = (LayoutCache)context.getAttribute(
			"layoutCache");
		long companyId = context.getCompanyId();
		long groupId = context.getGroupId();
		User user = context.getUser();
		long userId = user.getUserId();

		Map<Long, String[]> roleIdsToActionIds = new HashMap<Long, String[]>();

		for (LarDigestPermission permission : permissions) {
			String roleName = permission.getRoleName();

			Role importedRole = (Role)getZipEntryAsObject(
				context.getZipReader(), getRolePath(roleName));

			int type = importedRole.getType();

			Role role = null;

			if (roleName.startsWith(PermissionExporter.ROLE_TEAM_PREFIX)) {
				roleName = roleName.substring(
					PermissionExporter.ROLE_TEAM_PREFIX.length());

				String description = importedRole.getDescription();

				Team team = null;

				try {
					team = TeamLocalServiceUtil.getTeam(groupId, roleName);
				}
				catch (NoSuchTeamException nste) {
					team = TeamLocalServiceUtil.addTeam(
						userId, groupId, roleName, description);
				}

				role = RoleLocalServiceUtil.getTeamRole(
					companyId, team.getTeamId());
			}
			else {
				role = layoutCache.getRole(companyId, roleName);
			}

			if (role == null) {
				String title = importedRole.getTitle();

				Map<Locale, String> titleMap =
					LocalizationUtil.getLocalizationMap(title);

				String description = importedRole.getDescription();

				Map<Locale, String> descriptionMap =
					LocalizationUtil.getLocalizationMap(description);

				role = RoleLocalServiceUtil.addRole(
					userId, companyId, roleName, titleMap, descriptionMap,
					type);
			}

			List<String> actions = permission.getActionIds();

			roleIdsToActionIds.put(
				role.getRoleId(), actions.toArray(new String[actions.size()]));
		}

		if (roleIdsToActionIds.isEmpty()) {
			return;
		}

		ResourcePermissionLocalServiceUtil.setResourcePermissions(
			companyId, resourceName, ResourceConstants.SCOPE_INDIVIDUAL,
			resourcePrimKey, roleIdsToActionIds);
	}

	protected void importEntityPermission(
			String resourceName, long newResourcePK,
			Map<String, List<String>> permissions, DataHandlerContext context)
		throws PortalException, SystemException {

		long companyId = context.getCompanyId();
		long groupId = context.getGroupId();


		if (!MapUtil.getBoolean(
				context.getParameters(), PortletDataHandlerKeys.PERMISSIONS)) {

			return;
		}

		if (permissions == null) {
			return;
		}

		Map<Long, String[]> roleIdsToActionIds = new HashMap<Long, String[]>();

		for (String roleName : permissions.keySet()) {
			Role role = null;

			Team team = null;

			if (roleName.startsWith(PermissionExporter.ROLE_TEAM_PREFIX)) {
				roleName = roleName.substring(
					PermissionExporter.ROLE_TEAM_PREFIX.length());

				try {
					team = TeamLocalServiceUtil.getTeam(groupId, roleName);
				}
				catch (NoSuchTeamException nste) {
					if (_log.isWarnEnabled()) {
						_log.warn("Team " + roleName + " does not exist");
					}

					continue;
				}
			}

			try {
				if (team != null) {
					role = RoleLocalServiceUtil.getTeamRole(
						companyId, team.getTeamId());
				}
				else {
					role = RoleLocalServiceUtil.getRole(companyId, roleName);
				}
			}
			catch (NoSuchRoleException nsre) {
				if (_log.isWarnEnabled()) {
					_log.warn("Role " + roleName + " does not exist");
				}

				continue;
			}

			List<String> actions = permissions.get(roleName);

			roleIdsToActionIds.put(
				role.getRoleId(), actions.toArray(new String[actions.size()]));
		}

		if (roleIdsToActionIds.isEmpty()) {
			return;
		}

		if (ResourceBlockLocalServiceUtil.isSupported(resourceName)) {
			ResourceBlockLocalServiceUtil.setIndividualScopePermissions(
				companyId, groupId, resourceName, newResourcePK,
				roleIdsToActionIds);
		}
		else {
			ResourcePermissionLocalServiceUtil.setResourcePermissions(
				companyId, resourceName, ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(newResourcePK), roleIdsToActionIds);
		}
	}

	protected boolean isValidPath(String path) {
		if (path.contains(StringPool.DOUBLE_PERIOD)) {
			return false;
		}

		return true;
	}

	protected String toXML(Object object) {
		return getXstreamWrapper().toXML(object);
	}

	private Log _log = LogFactoryUtil.getLog(BaseDataHandlerImpl.class);
	private PortletDataContextListener _portletDataContextListener;
	private XStreamWrapper _xStreamWrapper;

	@BeanReference(type = JournalArticleDataHandler.class)
	protected JournalArticleDataHandler journalArticleDataHandler;
	@BeanReference(type = JournalStructureDataHandler.class)
	protected JournalStructureDataHandler journalStructureDataHandler;
	@BeanReference(type = JournalTemplateDataHandler.class)
	protected JournalTemplateDataHandler journalTemplateDataHandler;
	@BeanReference(type = ImageDataHandler.class)
	protected ImageDataHandler imageDataHandler;
	@BeanReference(type = BookmarksEntryDataHandler.class)
	protected BookmarksEntryDataHandler bookmarksEntryDataHandler;
	@BeanReference(type = BookmarksFolderDataHandler.class)
	protected BookmarksFolderDataHandler bookmarksFolderDataHandler;
	@BeanReference(type = BookmarksPortletDataHandler.class)
	protected BookmarksPortletDataHandler bookmarksPortletDataHandler;
	@BeanReference(type = AssetVocabularyDataHandler.class)
	protected AssetVocabularyDataHandler assetVocabularyDataHandler;
	@BeanReference(type = AssetCategoryDataHandler.class)
	protected AssetCategoryDataHandler assetCategoryDataHandler;
	@BeanReference(type = AssetLinkDataHandler.class)
	protected AssetLinkDataHandler assetLinkDataHandler;
	@BeanReference(type = LayoutSetDataHandler.class)
	protected LayoutSetDataHandler layoutSetDataHandler;

}

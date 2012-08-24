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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.NoSuchRoleException;
import com.liferay.portal.NoSuchTeamException;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.lar.DataHandlersUtil;
import com.liferay.portal.lar.PermissionExporter;
import com.liferay.portal.lar.XStreamWrapper;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PortletConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.Team;
import com.liferay.portal.model.User;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ResourceBlockLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.TeamLocalServiceUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.aop.AfterReturningAdvice;

/**
 * @author Daniel Kocsis
 */
public class ImportPermissionAdvice extends ExportImportAdvice
		implements AfterReturningAdvice {

	public void afterReturning(
			Object returnValue, Method method, Object[] args, Object target)
		throws Throwable {

		Object entity = args[0];
		DataHandlerContext context = (DataHandlerContext)args[1];

		String entityType = getEntityObjectInterfaceName(entity);

		StagedDataHandler dataHandler = DataHandlersUtil.getDataHandlerInstance(
			entityType);

		boolean importPermissions = MapUtil.getBoolean(
			context.getParameters(), PortletDataHandlerKeys.PERMISSIONS);

		if (importPermissions) {
			if (entity instanceof Layout) {
				Layout layout = (Layout)entity;

				importLayoutPermissions(layout, context);
			}
			else if (entity instanceof Portlet) {
				Portlet portlet = (Portlet)entity;

				long plid = context.getPlid();

				Layout layout = LayoutLocalServiceUtil.getLayout(plid);

				importPortletPermissions(portlet, layout, context);
			}
			else {
				String path = ExportImportPathUtil.getPermissionPath(entity);

				Map<String, List<String>> permissionsMap =
					(Map<String, List<String>>)readXmlObject(path, context);

				importEntityPermissions(
					permissionsMap, dataHandler.getPermissionResourceName(),
					context.getScopeGroupId(), context);
			}
		}
	}

	protected void importEntityPermissions(
			Map<String, List<String>> permissionsMap, String resourceName,
			long newResourcePK, DataHandlerContext context)
		throws PortalException, SystemException {

		long companyId = context.getCompanyId();
		long groupId = context.getGroupId();

		Map<Long, String[]> roleIdsToActionIds = new HashMap<Long, String[]>();

		for (String roleName : permissionsMap.keySet()) {
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

			List<String> actions = permissionsMap.get(roleName);

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

	protected void importLayoutPermissions(
			Layout layout, DataHandlerContext context)
		throws Exception {

		String resourceName = Layout.class.getName();
		String resourcePrimKey = String.valueOf(layout.getPlid());

		String path = ExportImportPathUtil.getPermissionPath(layout);

		Map<String, List<String>> permissionsMap =
			(Map<String, List<String>>)readXmlObject(path, context);

		if (permissionsMap == null) {
			return;
		}

		importPermissions(
			permissionsMap, resourceName, resourcePrimKey, context);
	}

	protected void importPermissions(
			Map<String, List<String>> permissionsMap, String resourceName,
			String resourcePrimKey, DataHandlerContext context)
		throws Exception {

		LayoutCache layoutCache = (LayoutCache)context.getAttribute(
			"layoutCache");
		long companyId = context.getCompanyId();
		long groupId = context.getGroupId();
		User user = context.getUser();
		long userId = user.getUserId();

		Map<Long, String[]> roleIdsToActionIds = new HashMap<Long, String[]>();

		for (String name : permissionsMap.keySet()) {

			Role importedRole = (Role)readXmlObject(getRolePath(name), context);

			Role role = null;

			if (name.startsWith(PermissionExporter.ROLE_TEAM_PREFIX)) {
				name = name.substring(
					PermissionExporter.ROLE_TEAM_PREFIX.length());

				Team team = null;

				try {
					team = TeamLocalServiceUtil.getTeam(groupId, name);
				}
				catch (NoSuchTeamException nste) {
					team = TeamLocalServiceUtil.addTeam(
						userId, groupId, name, importedRole.getDescription());
				}

				role = RoleLocalServiceUtil.getTeamRole(
					companyId, team.getTeamId());
			}
			else {
				role = layoutCache.getRole(companyId, name);
			}

			if (role == null) {
				Map<Locale, String> titleMap =
					LocalizationUtil.getLocalizationMap(
						importedRole.getTitle());

				Map<Locale, String> descriptionMap =
					LocalizationUtil.getLocalizationMap(
						importedRole.getDescription());

				role = RoleLocalServiceUtil.addRole(
					userId, companyId, name, titleMap, descriptionMap,
					importedRole.getType());
			}

			List<String> actions = permissionsMap.get(name);

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

	protected void importPortletPermissions(
			Portlet portlet, Layout layout, DataHandlerContext context)
		throws Exception {

		String resourceName = PortletConstants.getRootPortletId(
			portlet.getPortletId());

		String resourcePrimKey = PortletPermissionUtil.getPrimaryKey(
			layout.getPlid(), portlet.getPortletId());

		String path = ExportImportPathUtil.getPermissionPath(portlet);

		Map<String, List<String>> permissionsMap =
			(Map<String, List<String>>)readXmlObject(path, context);

		if (permissionsMap == null) {
			return;
		}

		importPermissions(
			permissionsMap, resourceName, resourcePrimKey, context);
	}

	protected Object readXmlObject(String path, DataHandlerContext context) {

		ZipReader zipReader = context.getZipReader();

		String xml = zipReader.getEntryAsString(path);

		if (xml == null) {
			return null;
		}

		XStreamWrapper xStream = (XStreamWrapper)PortalBeanLocatorUtil.locate(
			"xStreamWrapper");

		return xStream.fromXML(xml);
	}

	private static Log _log = LogFactoryUtil.getLog(
		ImportPermissionAdvice.class);

}
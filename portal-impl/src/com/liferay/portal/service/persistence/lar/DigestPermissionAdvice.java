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

package com.liferay.portal.service.persistence.lar;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.DataHandlerContextThreadLocal;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PrimitiveLongList;
import com.liferay.portal.lar.DataHandlersUtil;
import com.liferay.portal.lar.LayoutCache;
import com.liferay.portal.lar.PermissionExporter;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.PortletConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.Team;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ResourceBlockLocalServiceUtil;
import com.liferay.portal.service.ResourceBlockPermissionLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.TeamLocalServiceUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.service.persistence.BaseDataHandler;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mate Thurzo
 */
public class DigestPermissionAdvice implements AfterReturningAdvice {

	public void afterReturning(
			Object returnValue, Method method, Object[] args, Object target)
		throws Throwable {

		if ((returnValue == null) || !(returnValue instanceof LarDigestItem)) {
			return;
		}

		LarDigestItem item = (LarDigestItem)returnValue;

		DataHandlerContext context =
			DataHandlerContextThreadLocal.getDataHandlerContext();

		BaseDataHandler dataHandler = DataHandlersUtil.getDataHandlerInstance(
			item.getType());

		boolean exportPermissions = MapUtil.getBoolean(
			context.getParameters(), PortletDataHandlerKeys.PERMISSIONS);

		if (exportPermissions) {
			Map permissionsMap = null;

			if (Layout.class.getName().equals(item.getType())) {
				long plid = Long.valueOf(item.getClassPK());

				Layout layout = LayoutLocalServiceUtil.getLayout(plid);

				permissionsMap = digestLayoutPermissions(context, layout);
			}
			else if (dataHandler instanceof PortletDataHandler) {
				Map<String, String> metadataMap = item.getMetadata();

				long plid = Long.valueOf(metadataMap.get("old-plid"));
				String portletId = metadataMap.get("portlet-id");

				Layout layout = LayoutLocalServiceUtil.getLayout(plid);

				permissionsMap = digestPortletPermissions(
					context, portletId, layout);
			}
			else {
				permissionsMap = digestEntityPermissions(
					dataHandler.getPermissionResourceName(),
					context.getScopeGroupId(), context);
			}

			if (permissionsMap != null) {
				item.setPermissions(permissionsMap);
			}
		}
	}

	private Map<String, List<String>> digestEntityPermissions(
			String resourceName, long resourcePK, DataHandlerContext context)
		throws Exception {

		HashMap<String, List<String>> permissions =
			new HashMap<String, List<String>>();

		Group group = GroupLocalServiceUtil.getGroup(context.getGroupId());

		List<Role> roles = RoleLocalServiceUtil.getRoles(
			context.getCompanyId());

		PrimitiveLongList roleIds = new PrimitiveLongList(roles.size());
		Map<Long, String> roleIdsToNames = new HashMap<Long, String>();

		for (Role role : roles) {
			int type = role.getType();

			if ((type == RoleConstants.TYPE_REGULAR) ||
				((type == RoleConstants.TYPE_ORGANIZATION) &&
					group.isOrganization()) ||
				((type == RoleConstants.TYPE_SITE) &&
					(group.isLayoutSetPrototype() || group.isSite()))) {

				String name = role.getName();

				roleIds.add(role.getRoleId());
				roleIdsToNames.put(role.getRoleId(), name);
			}
			else if ((type == RoleConstants.TYPE_PROVIDER) && role.isTeam()) {
				Team team = TeamLocalServiceUtil.getTeam(role.getClassPK());

				if (team.getGroupId() == context.getGroupId()) {
					String name =
						PermissionExporter.ROLE_TEAM_PREFIX + team.getName();

					roleIds.add(role.getRoleId());
					roleIdsToNames.put(role.getRoleId(), name);
				}
			}
		}

		List<String> actionIds = ResourceActionsUtil.getModelResourceActions(
			resourceName);

		Map<Long, Set<String>> roleIdsToActionIds = getActionIds(
			context.getCompanyId(), roleIds.getArray(), resourceName,
			resourcePK, actionIds);

		for (Map.Entry<Long, String> entry : roleIdsToNames.entrySet()) {
			long roleId = entry.getKey();
			String name = entry.getValue();

			Set<String> availableActionIds = roleIdsToActionIds.get(roleId);

			if ((availableActionIds == null) || availableActionIds.isEmpty()) {
				continue;
			}

			List<String> actionIdsList = ListUtil.fromCollection(
				availableActionIds);

			permissions.put(name, actionIdsList);
		}

		return permissions;
	}

	private Map<String, List<String>> digestLayoutPermissions(
			DataHandlerContext context, Layout layout)
		throws Exception {

		long companyId = context.getCompanyId();
		long groupId = context.getGroupId();

		String resourceName = Layout.class.getName();
		String resourcePrimKey = String.valueOf(layout.getPlid());

		LayoutCache layoutCache = (LayoutCache)context.getAttribute(
			"layoutCache");

		return digestPermissions(
			layoutCache, companyId, groupId, resourceName, resourcePrimKey,
			false);
	}

	protected Map<String, List<String>> digestPermissions(
			LayoutCache layoutCache, long companyId, long groupId,
			String resourceName, String resourcePrimKey, boolean portletActions)
		throws Exception {

		List<Role> roles = layoutCache.getGroupRoles_5(groupId, resourceName);

		List<String> actionIds = null;

		if (portletActions) {
			actionIds = ResourceActionsUtil.getPortletResourceActions(
				resourceName);
		}
		else {
			actionIds = ResourceActionsUtil.getModelResourceActions(
				resourceName);
		}

		if (actionIds.isEmpty()) {
			return null;
		}

		PrimitiveLongList roleIds = new PrimitiveLongList(roles.size());
		Map<Long, Role> roleIdsToRoles = new HashMap<Long, Role>();

		for (Role role : roles) {
			String name = role.getName();

			if (name.equals(RoleConstants.ADMINISTRATOR)) {
				continue;
			}

			roleIds.add(role.getRoleId());
			roleIdsToRoles.put(role.getRoleId(), role);
		}

		Map<Long, Set<String>> roleIdsToActionIds =
			ResourcePermissionLocalServiceUtil.
				getAvailableResourcePermissionActionIds(
					companyId, resourceName, ResourceConstants.SCOPE_INDIVIDUAL,
					resourcePrimKey, roleIds.getArray(), actionIds);

		HashMap<String, List<String>> roleMap =
				new HashMap<String, List<String>>();

		for (Map.Entry<Long, Set<String>> entry :
				roleIdsToActionIds.entrySet()) {

			List<String> values = ListUtil.fromCollection(entry.getValue());

			roleMap.put(String.valueOf(entry.getKey()), values);
		}

		return roleMap;
	}

	private Map<String, List<String>> digestPortletPermissions(
			DataHandlerContext context, String portletId, Layout layout)
		throws Exception {

		long companyId = context.getCompanyId();
		long groupId = context.getGroupId();

		String resourceName = PortletConstants.getRootPortletId(portletId);
		String resourcePrimKey = PortletPermissionUtil.getPrimaryKey(
				layout.getPlid(), portletId);
		LayoutCache layoutCache = (LayoutCache)context.getAttribute(
			"layoutCache");

		return digestPermissions(
			layoutCache, companyId, groupId, resourceName, resourcePrimKey,
			true);
	}

	private Map<Long, Set<String>> getActionIds(
			long companyId, long[] roleIds, String className, long primKey,
			List<String> actionIds)
		throws PortalException, SystemException {

		if (ResourceBlockLocalServiceUtil.isSupported(className)) {
			return ResourceBlockPermissionLocalServiceUtil.
				getAvailableResourceBlockPermissionActionIds(
						roleIds, className, primKey, actionIds);
		}
		else {
			return ResourcePermissionLocalServiceUtil.
				getAvailableResourcePermissionActionIds(
					companyId, className, ResourceConstants.SCOPE_INDIVIDUAL,
					String.valueOf(primKey), roleIds, actionIds);
		}
	}

}

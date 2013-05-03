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

package com.liferay.portlet.usergroupsadmin.lar;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.lar.UserIdStrategy;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserGroupLocalServiceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author David Mendez Gonzalez
 */
public class UserGroupStagedModelDataHandler
	extends BaseStagedModelDataHandler<UserGroup> {

	public static final String[] CLASS_NAMES = {UserGroup.class.getName()};

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, UserGroup userGroup)
		throws Exception {

		Element userGroupElement = portletDataContext.getExportDataElement(
			userGroup);

		portletDataContext.addClassedModel(
			userGroupElement, ExportImportPathUtil.getModelPath(userGroup),
			userGroup, UserGroupsAdminPortletDataHandler.NAMESPACE);

		exportLayoutLar(portletDataContext, userGroup);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, UserGroup userGroup)
		throws Exception {

		long userId = portletDataContext.getUserId(userGroup.getUserUuid());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			userGroup, UserGroupsAdminPortletDataHandler.NAMESPACE);

		UserGroup existingUserGroup =
			UserGroupLocalServiceUtil.fetchUserGroupByUuidAndCompanyId(
				userGroup.getUuid(), portletDataContext.getCompanyId());

		if (existingUserGroup == null) {
			existingUserGroup = UserGroupLocalServiceUtil.fetchUserGroup(
				portletDataContext.getCompanyId(), userGroup.getName());
		}

		UserGroup importedUserGroup = null;

		if (existingUserGroup == null) {
			serviceContext.setUuid(userGroup.getUuid());

			importedUserGroup = UserGroupLocalServiceUtil.addUserGroup(
				userId, portletDataContext.getCompanyId(), userGroup.getName(),
				userGroup.getDescription(), serviceContext);
		}
		else {
			importedUserGroup = UserGroupLocalServiceUtil.updateUserGroup(
				portletDataContext.getCompanyId(),
				existingUserGroup.getUserGroupId(), userGroup.getName(),
				userGroup.getDescription(), serviceContext);
		}

		portletDataContext.importClassedModel(
			userGroup, importedUserGroup,
			UserGroupsAdminPortletDataHandler.NAMESPACE);

		importSite(userId, portletDataContext, userGroup, existingUserGroup);
	}

	protected void exportLayoutLar(
			PortletDataContext portletDataContext, UserGroup userGroup)
		throws PortalException, SystemException {

		String privatePath = getLayoutLarPath(
			portletDataContext, userGroup, true);
		String publicPath = getLayoutLarPath(
			portletDataContext, userGroup, false);

		long groupId = userGroup.getGroup().getGroupId();

		Map<String, String[]> parameters = getLayoutImportExportParameters(
			true);

		byte[] layouts = LayoutLocalServiceUtil.exportLayouts(
			groupId, true, parameters, null, null);

		portletDataContext.addZipEntry(privatePath, layouts);

		parameters = getLayoutImportExportParameters(false);

		layouts = LayoutLocalServiceUtil.exportLayouts(
			groupId, false, parameters, null, null);

		portletDataContext.addZipEntry(publicPath, layouts);
	}

	protected Map<String, String[]> getLayoutImportExportParameters(
		boolean isPrivate) {

		if (isPrivate) {
			return LAR_PARAMETERS;
		}
		else {
			Map<String, String[]> parameters = new HashMap<String, String[]>(
				LAR_PARAMETERS);

			LAR_PARAMETERS.put(
				PortletDataHandlerKeys.PORTLET_DATA,
				new String[] {Boolean.FALSE.toString()});
			LAR_PARAMETERS.put(
				PortletDataHandlerKeys.PORTLET_DATA_ALL,
				new String[] {Boolean.FALSE.toString()});

			return parameters;
		}
	}

	protected String getLayoutLarPath(
			PortletDataContext portletDataContext, UserGroup userGroup,
			boolean isPrivate) {

		String larPath = ExportImportPathUtil.getModelPath(
			portletDataContext, userGroup.getModelClassName(),
			userGroup.getPrimaryKey());

		if (isPrivate) {
			larPath.concat("-private");
		}
		else {

			larPath.concat("-public");
		}

		larPath.concat("-layout.lar");

		return larPath;
	}

	protected void importSite(
			long userId, PortletDataContext portletDataContext,
			UserGroup userGroup, UserGroup importedUserGroup)
		throws PortalException, SystemException {

		String publicPath = getLayoutLarPath(
			portletDataContext, userGroup, false);
		String privatePath = getLayoutLarPath(
			portletDataContext, userGroup, true);

		long groupId = importedUserGroup.getGroup().getGroupId();

		Map<String, String[]> parameters = getLayoutImportExportParameters(
			false);

		byte[] larBytes = portletDataContext.getZipEntryAsByteArray(publicPath);

		LayoutLocalServiceUtil.importLayouts(
			userId, groupId, false, parameters, larBytes);

		parameters = getLayoutImportExportParameters(true);

		larBytes = portletDataContext.getZipEntryAsByteArray(privatePath);

		LayoutLocalServiceUtil.importLayouts(
			userId, groupId, true, parameters, larBytes);
	}

	private static final Map<String, String[]> LAR_PARAMETERS =
		new HashMap<String, String[]>();

	static {
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.CATEGORIES,
			new String[] {Boolean.TRUE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.DATA_STRATEGY,
			new String[] {PortletDataHandlerKeys.DATA_STRATEGY_MIRROR});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.DELETE_MISSING_LAYOUTS,
			new String[] {Boolean.FALSE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.DELETE_PORTLET_DATA,
			new String[] {Boolean.FALSE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.LAYOUT_SET_SETTINGS,
			new String[] {Boolean.FALSE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE,
			new String[] {PortletDataHandlerKeys.
				LAYOUTS_IMPORT_MODE_CREATED_FROM_PROTOTYPE});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.LOGO,
			new String[] {Boolean.FALSE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.PERMISSIONS,
			new String[] {Boolean.TRUE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.PORTLET_DATA,
			new String[] {Boolean.TRUE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.PORTLET_DATA_ALL,
			new String[] {Boolean.TRUE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.PORTLET_SETUP,
			new String[] {Boolean.TRUE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.PORTLET_USER_PREFERENCES,
			new String[] {Boolean.TRUE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.PORTLETS_MERGE_MODE,
			new String[] {PortletDataHandlerKeys.
				PORTLETS_MERGE_MODE_ADD_TO_BOTTOM});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.THEME,
			new String[] {Boolean.FALSE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.THEME_REFERENCE,
			new String[] {Boolean.TRUE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.UPDATE_LAST_PUBLISH_DATE,
			new String[] {Boolean.FALSE.toString()});
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.USER_ID_STRATEGY,
			new String[] {UserIdStrategy.CURRENT_USER_ID});
	}
}
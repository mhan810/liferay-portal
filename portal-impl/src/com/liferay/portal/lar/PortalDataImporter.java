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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.portaldata.PortalDataPortletResolverUtil;

import java.io.File;
import java.io.InputStream;

import java.util.Map;

/**
 * @author Edward C. Han
 */
public class PortalDataImporter {
	public void importPortalData(
			long userId, long companyId, Class type,
			Map<String, String[]> parameterMap, File file)
		throws Exception {

		User user = UserLocalServiceUtil.getUser(userId);

		Portlet portlet = PortalDataPortletResolverUtil.resolvePortlet(
			user.getCompanyId(), type);

		importPortalData(
			userId, companyId, portlet.getPortletId(), parameterMap, file);
	}

	public void importPortalData(
			long userId, long companyId, Class type,
			Map<String, String[]> parameterMap, InputStream is)
		throws Exception {

		File file = null;

		try {
			file = FileUtil.createTempFile("lar");

			FileUtil.write(file, is);

			importPortalData(userId, companyId, type, parameterMap, file);
		}
		finally {
			FileUtil.delete(file);
		}
	}

	public void importPortalData(
			long userId, long companyId, String portletId,
			Map<String, String[]> parameterMap, File file)
		throws Exception {

		doImportPortalData(userId, companyId, portletId, parameterMap, file);
	}

	public void importPortalData(
			long userId, long plid, String portletId,
			Map<String, String[]> parameterMap, InputStream is)
		throws Exception {

		File file = null;

		try {
			file = FileUtil.createTempFile("lar");

			FileUtil.write(file, is);

			importPortalData(userId, plid, portletId, parameterMap, file);
		}
		finally {
			FileUtil.delete(file);
		}
	}

	protected void doImportPortalData(
			long userId, long companyId, String portletId,
			Map<String, String[]> parameterMap, File file)
		throws Exception {

		Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(companyId);

		Layout layout = getCompanyLayout(companyId);

		PortletImporter portletImporter = new PortletImporter();

		portletImporter.importPortletInfo(
			userId, layout.getPlid(), companyGroup.getGroupId(), portletId,
			parameterMap, file);

	}

	protected Layout getCompanyLayout(long companyId)
		throws PortalException, SystemException {

		Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(companyId);

		Layout layout = LayoutLocalServiceUtil.fetchFirstLayout(
			companyGroup.getGroupId(), true,
			LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

		if (layout == null) {
			User defaultUser = UserLocalServiceUtil.getDefaultUser(companyId);

			layout = LayoutLocalServiceUtil.addLayout(
				defaultUser.getUserId(), companyGroup.getGroupId(), true,
				LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, "Default", "Default",
				"Default system layout", LayoutConstants.TYPE_PORTLET, true,
				"/default", new ServiceContext());
		}

		return layout;
	}

}
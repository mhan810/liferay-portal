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
import com.liferay.portal.kernel.lar.ExportImportThreadLocal;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.portaldata.PortalDataPortletResolverUtil;

import java.io.File;

import java.util.Date;
import java.util.Map;

/**
 * @author Edward C. Han
 */
public class PortalDataExporter {

	public byte[] exportPortalData(
			long companyId, Class type, Map<String, String[]> parameterMap,
			Date startDate, Date endDate)
		throws Exception {

		File file = null;

		try {
			file = exportPortalDataAsFile(
				companyId, type, parameterMap, startDate, endDate);

			return FileUtil.getBytes(file);
		}
		finally {
			FileUtil.delete(file);
		}
	}

	public byte[] exportPortalData(
			long companyId, Portlet portlet, Map<String, String[]> parameterMap,
			Date startDate, Date endDate)
		throws Exception {

		File file = null;

		try {
			file = exportPortalDataAsFile(
				companyId, portlet, parameterMap, startDate, endDate);

			return FileUtil.getBytes(file);
		}
		finally {
			FileUtil.delete(file);
		}
	}

	public byte[] exportPortalData(
			long companyId, String portletId,
			Map<String, String[]> parameterMap, Date startDate, Date endDate)
		throws Exception {

		File file = null;

		try {
			file = exportPortalDataAsFile(
				companyId, portletId, parameterMap, startDate, endDate);

			return FileUtil.getBytes(file);
		}
		finally {
			FileUtil.delete(file);
		}
	}

	public File exportPortalDataAsFile(
			long companyId, Class type, Map<String, String[]> parameterMap,
			Date startDate, Date endDate)
		throws Exception {

		Portlet portlet = PortalDataPortletResolverUtil.resolvePortlet(
			companyId, type);

		return doExportPortalDataAsFile(
			companyId, portlet, parameterMap, startDate, endDate);
	}

	public File exportPortalDataAsFile(
			long companyId, Portlet portlet, Map<String, String[]> parameterMap,
			Date startDate, Date endDate)
		throws Exception {

		return doExportPortalDataAsFile(
			companyId, portlet, parameterMap, startDate, endDate);
	}

	public File exportPortalDataAsFile(
			long companyId, String portletId,
			Map<String, String[]> parameterMap, Date startDate, Date endDate)
		throws Exception {

		ExportImportThreadLocal.setLayoutExportInProcess(true);

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			companyId, portletId);

		return doExportPortalDataAsFile(
			companyId, portlet, parameterMap, startDate, endDate);
	}

	protected File doExportPortalDataAsFile(
			long companyId, Portlet portlet, Map<String, String[]> parameterMap,
			Date startDate, Date endDate)
		throws Exception {

		try {
			Layout layout = getCompanyLayout(companyId);

			PortletExporter portletExporter = new PortletExporter();

			return portletExporter.exportPortletInfoAsFile(
				layout.getPlid(), layout.getGroupId(), portlet.getPortletId(),
				parameterMap, startDate, endDate);
		}
		finally {
			ExportImportThreadLocal.setLayoutExportInProcess(false);
		}
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
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

package com.liferay.portlet.portaldata;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.lar.ExportImportUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.upload.UploadServletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.portaldata.service.PortalDataHandlerServiceUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author Edward C. Han
 */
public class PortalDataServlet extends HttpServlet {
	@Override
	protected void service(
			HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		String cmd = ParamUtil.getString(request, Constants.CMD);

		try {
			if (cmd.equals(Constants.EXPORT)) {
				doExport(request, response);
			}
			else if (cmd.equals(Constants.IMPORT)) {
				doImport(request);
			}
		}
		catch (Exception e) {
			if (_log.isErrorEnabled()) {
				_log.error(e, e);
			}

			if (e instanceof IOException) {
				throw (IOException) e;
			}

			throw new ServletException(e);
		}
	}

	protected void doExport(
			HttpServletRequest request, HttpServletResponse response)
		throws PortalException, SystemException, IOException {

		long companyId = PortalUtil.getCompanyId(request);

		String fileName = ParamUtil.getString(request, "fileName");

		String portletId = getPortletId(request, companyId);

		UploadServletRequest uploadRequest =
			PortalUtil.getUploadServletRequest(request);

		Map<String, String[]> parameters = getParameters(uploadRequest);

		DateFormat dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			PropsValues.PORTAL_DATA_SERVLET_DATE_FORMAT);

		Date startDate = getDate(request, "startDate", dateFormat);
		Date endDate = getDate(request, "endDate", dateFormat);

		File file = null;

		try {
			file = PortalDataHandlerServiceUtil.exportPortalDataAsFile(
				portletId, parameters, startDate, endDate);

			ServletResponseUtil.sendFile(
				request, response, fileName, new FileInputStream(file),
				ContentTypes.APPLICATION_ZIP);
		}
		finally {
			FileUtil.delete(file);
		}
	}

	protected void doImport(
			HttpServletRequest request)
		throws PortalException, SystemException, IOException {

		long companyId = PortalUtil.getCompanyId(request);

		UploadServletRequest uploadRequest =
			PortalUtil.getUploadServletRequest(request);

		Map<String, String[]> parameters = getParameters(uploadRequest);

		File file = uploadRequest.getFile("file");

		String portletId = getPortletId(request, companyId);

		PortalDataHandlerServiceUtil.importPortalData(
			portletId, parameters, file);
	}

	protected Date getDate(
		HttpServletRequest request, String name, DateFormat dateFormat) {

		Calendar startDateParam = ExportImportUtil.getDate(request, name);

		if (Validator.isNotNull(startDateParam)) {
			return startDateParam.getTime();
		}
		else {
			return ParamUtil.getDate(request, name, dateFormat, null);
		}
	}

	protected Map<String, String[]> getParameters(
			UploadServletRequest uploadRequest)
		throws IOException {

		Map<String, String[]> parameters = null;

		File paramFile = uploadRequest.getFile("paramJson");

		if (paramFile != null) {
			String paramJson = FileUtil.read(paramFile);

			if (Validator.isNotNull(paramJson)) {
				parameters = (Map<String, String[]>) JSONFactoryUtil.
					deserialize(paramJson);
			}
		}

		if (parameters == null) {
			parameters = uploadRequest.getParameterMap();
		}

		return parameters;
	}

	protected String getPortletId(
			HttpServletRequest request, long companyId)
		throws PortalException, SystemException {

		String typeName = ParamUtil.getString(request, "type");

		Class type;

		try {
			type = Class.forName(typeName);
		}
		catch (ClassNotFoundException cnfe) {
			throw new PortalException(cnfe);
		}

		Portlet portlet = PortalDataPortletResolverUtil.resolvePortlet(
			companyId, type);

		return portlet.getPortletId();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortalDataServlet.class);
}

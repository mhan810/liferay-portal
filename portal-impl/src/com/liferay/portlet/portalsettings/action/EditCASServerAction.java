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

package com.liferay.portlet.portalsettings.action;

import com.liferay.portal.kernel.cas.CASManagerUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.cas.InvalidCASSettingException;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Edward Han
 */
public class EditCASServerAction extends PortletAction {

	@Override
	public void processAction(
			ActionMapping mapping, ActionForm form, PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD)) {
				updateCASServer(actionRequest, true);
			}
			else if (cmd.equals(Constants.UPDATE)) {
				updateCASServer(actionRequest, false);
			}
			else if (cmd.equals(Constants.DELETE)) {
				deleteCASServer(actionRequest);
			}

			sendRedirect(actionRequest, actionResponse);
		}
		catch (InvalidCASSettingException icse) {
			List<String> errors = icse.getErrors();

			for (String error : errors) {
				SessionErrors.add(actionRequest, error);
			}
		}
		catch (Exception e) {
			if (e instanceof PrincipalException) {
				SessionErrors.add(actionRequest, e.getClass());

				setForward(actionRequest, "portlet.portal_settings.error");
			}
			else {
				throw e;
			}
		}
	}

	@Override
	public ActionForward render(
			ActionMapping mapping, ActionForm form, PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		return mapping.findForward(
			getForward(
				renderRequest, "portlet.portal_settings.edit_cas_server"));
	}

	protected void deleteCASServer(ActionRequest actionRequest)
			throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		String casServerId = ParamUtil.getString(actionRequest, "casServerId");

		long companyId = themeDisplay.getCompanyId();

		CASManagerUtil.deleteCASServer(companyId, casServerId);
	}

	protected void updateCASServer(ActionRequest actionRequest, boolean add)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String casServerId = ParamUtil.getString(actionRequest, "casServerId");

		UnicodeProperties properties = PropertiesParamUtil.getProperties(
			actionRequest, "settings--");

		long companyId = themeDisplay.getCompanyId();

		CASManagerUtil.updateCASServer(companyId, casServerId, properties, add);
	}

}
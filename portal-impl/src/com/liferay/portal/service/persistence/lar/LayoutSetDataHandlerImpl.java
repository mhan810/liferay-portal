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

import com.liferay.portal.kernel.lar.*;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;

import java.util.Map;

/**
 * @author Daniel Kocsis
 */
public class LayoutSetDataHandlerImpl extends BaseDataHandlerImpl<LayoutSet>
	implements LayoutSetDataHandler {

@Override
	public LayoutSet getEntity(String classPK) {
		// TODO implement getEntity
		return null;
	}

	@Override
	public void doDigest(LayoutSet object) throws Exception {
		// TODO implement doDigest
	}

	@Override
	public void doImport(LarDigestItem item) throws Exception{
		DataHandlerContext context = getDataHandlerContext();

		Map parameterMap = context.getParameters();

		boolean importTheme = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.THEME);
		boolean importLogo = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.LOGO);
		boolean importLayoutSetSettings = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.LAYOUT_SET_SETTINGS);
		boolean importThemeSettings = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.THEME_REFERENCE);


		LayoutSet layoutSet = null;

		try {
			layoutSet = LayoutSetLocalServiceUtil.getLayoutSet(
				context.getGroupId(), context.isPrivateLayout());
		}
		catch (Exception ex) {
			return;
		}

		String layoutSetPrototypeUuid = GetterUtil.getString(
			context.getAttribute("layoutSetPrototypeUuid"));

		if (Validator.isNotNull(layoutSetPrototypeUuid)) {
			layoutSet.setLayoutSetPrototypeUuid(layoutSetPrototypeUuid);

			boolean layoutSetPrototypeLinkEnabled = GetterUtil.getBoolean(
				context.getAttribute("layoutSetPrototypeLinkEnabled"));

			layoutSet.setLayoutSetPrototypeLinkEnabled(
				layoutSetPrototypeLinkEnabled);

			LayoutSetLocalServiceUtil.updateLayoutSet(layoutSet);
		}

		// Look and feel

		String themeId = layoutSet.getThemeId();
		String colorSchemeId = layoutSet.getColorSchemeId();

		/*if (importThemeSettings) {
			Attribute themeIdAttribute = headerElement.attribute("theme-id");

			if (themeIdAttribute != null) {
				themeId = themeIdAttribute.getValue();
			}

			Attribute colorSchemeIdAttribute = headerElement.attribute(
				"color-scheme-id");

			if (colorSchemeIdAttribute != null) {
				colorSchemeId = colorSchemeIdAttribute.getValue();
			}
		}

		if (importLogo) {
			String logoPath = headerElement.attributeValue("logo-path");

			byte[] iconBytes = portletDataContext.getZipEntryAsByteArray(
				logoPath);

			if ((iconBytes != null) && (iconBytes.length > 0)) {
				File logo = FileUtil.createTempFile(iconBytes);

				LayoutSetLocalServiceUtil.updateLogo(
					groupId, privateLayout, true, logo);
			}
			else {
				LayoutSetLocalServiceUtil.updateLogo(
					groupId, privateLayout, false, (File) null);
			}
		}

		if (importLayoutSetSettings) {
			String settings = GetterUtil.getString(
				headerElement.elementText("settings"));

			LayoutSetLocalServiceUtil.updateSettings(
				groupId, privateLayout, settings);
		}

		String css = GetterUtil.getString(headerElement.elementText("css"));

		// Look and feel
		InputStream themeZip = null;

		if (importTheme) {
			themeZip = getZipEntryAsInputStream("theme.zip");
		}

		if (themeZip != null) {
			String importThemeId = importTheme(layoutSet, themeZip);

			if (importThemeId != null) {
				themeId = importThemeId;
				colorSchemeId =
						ColorSchemeImpl.getDefaultRegularColorSchemeId();
			}

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Importing theme takes " + stopWatch.getTime() + " ms");
			}
		}

		boolean wapTheme = false;

		LayoutSetLocalServiceUtil.updateLookAndFeel(
			context.getGroupId(), context.isPrivateLayout(), themeId,
			colorSchemeId, css, wapTheme); */
	}

	private static Log _log =
		LogFactoryUtil.getLog(LayoutSetDataHandlerImpl.class);
}

/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.search.web.internal.page;

import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationConstants;
import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationSettingsMapFactory;
import com.liferay.exportimport.kernel.lar.PortletDataHandlerKeys;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.kernel.service.ExportImportConfigurationLocalService;
import com.liferay.exportimport.kernel.service.ExportImportLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.InputStream;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lino Alves
 * @author André de Oliveira
 */
@Component(immediate = true, service = DefaultSearchPageGenerator.class)
public class DefaultSearchPageGenerator {

	public void generate(long companyId) throws Exception {
		long groupId = getGuestGroupId(companyId);

		if (isPagePresent(groupId)) {
			return;
		}

		if (_log.isInfoEnabled()) {
			_log.info("Default Search Page not found. Importing...");
		}

		importPage(companyId, groupId);

		if (_log.isInfoEnabled()) {
			_log.info("Default Search Page imported.");
		}
	}

	public boolean isPagePresent(long groupId) {
		Layout layout = layoutLocalService.fetchLayoutByFriendlyURL(
			groupId, false, "/" + _FRIENDLY_URL);

		if (Validator.isNotNull(layout)) {
			return true;
		}

		return false;
	}

	protected ExportImportConfiguration addExportImportConfiguration(
			long companyId, long groupId)
		throws PortalException {

		long userId = getDefaultUserId(companyId);

		Map<String, String[]> parameterMap = new HashMap<>();

		parameterMap.put(
			PortletDataHandlerKeys.DELETE_MISSING_LAYOUTS,
			new String[] {StringPool.FALSE});

		Map<String, Serializable> settingsMap =
			ExportImportConfigurationSettingsMapFactory.
				buildImportLayoutSettingsMap(
					userId, groupId, false, null, parameterMap,
					Locale.getDefault(), TimeZone.getDefault());

		ExportImportConfiguration exportImportConfiguration =
			exportImportConfigurationLocalService.addExportImportConfiguration(
				userId, groupId, _CONFIGURATION, StringPool.BLANK,
				ExportImportConfigurationConstants.TYPE_IMPORT_LAYOUT,
				settingsMap, WorkflowConstants.STATUS_DRAFT,
				new ServiceContext());

		return exportImportConfiguration;
	}

	protected long getDefaultUserId(long companyId) throws PortalException {
		return userLocalService.getDefaultUserId(companyId);
	}

	protected long getGuestGroupId(long companyId) throws PortalException {
		Group guestGroup = groupLocalService.getGroup(
			companyId, GroupConstants.GUEST);

		return guestGroup.getGroupId();
	}

	protected InputStream getLarInputStream() {
		Class<?> clazz = getClass();

		return clazz.getResourceAsStream(_LAR);
	}

	protected void importPage(long companyId, long groupId) throws Exception {
		ExportImportConfiguration exportImportConfiguration =
			addExportImportConfiguration(companyId, groupId);

		try (InputStream inputStream = getLarInputStream()) {
			exportImportLocalService.importLayouts(
				exportImportConfiguration, inputStream);
		}
	}

	@Reference
	protected ExportImportConfigurationLocalService
		exportImportConfigurationLocalService;

	@Reference
	protected ExportImportLocalService exportImportLocalService;

	@Reference
	protected GroupLocalService groupLocalService;

	@Reference
	protected LayoutLocalService layoutLocalService;

	@Reference
	protected UserLocalService userLocalService;

	private static final String _CONFIGURATION = "search-import";

	private static final String _FRIENDLY_URL = "search";

	private static final String _LAR = "default-search-page.lar";

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultSearchPageGenerator.class);

}
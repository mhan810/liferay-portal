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

package com.liferay.portlet.layoutsadmin.lar;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniela Zapata Riesco
 */
public class LayoutSetPrototypeStagedModelDataHandler
	extends BaseStagedModelDataHandler<LayoutSetPrototype> {

	public static final String[] CLASS_NAMES =
		{LayoutSetPrototype.class.getName()};

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext,
			LayoutSetPrototype layoutSetPrototype)
		throws Exception {

		Element layoutSetPrototypeElement =
			portletDataContext.getExportDataElement(layoutSetPrototype);

		portletDataContext.addClassedModel(
			layoutSetPrototypeElement,
			ExportImportPathUtil.getModelPath(layoutSetPrototype),
			layoutSetPrototype, LayoutSetPrototypePortletDataHandler.NAMESPACE);

		exportLayoutLar(portletDataContext, layoutSetPrototype);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			LayoutSetPrototype layoutSetPrototype)
		throws Exception {

		long userId = portletDataContext.getUserId(
			layoutSetPrototype.getUserUuid());

		UnicodeProperties settingsProperties =
			layoutSetPrototype.getSettingsProperties();

		boolean layoutsUpdateable = GetterUtil.getBoolean(
			settingsProperties.getProperty("layoutsUpdateable"), true);

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			layoutSetPrototype, LayoutSetPrototypePortletDataHandler.NAMESPACE);

		LayoutSetPrototype importedLayoutSetPrototype = null;

		if (portletDataContext.isDataStrategyMirror()) {
			LayoutSetPrototype existingLayoutSetPrototype =
				LayoutSetPrototypeLocalServiceUtil.
					fetchLayoutSetPrototypeByUuidAndCompanyId(
						layoutSetPrototype.getUuid(),
						portletDataContext.getCompanyId());

			if (existingLayoutSetPrototype == null) {
				serviceContext.setUuid(layoutSetPrototype.getUuid());

				importedLayoutSetPrototype =
					LayoutSetPrototypeLocalServiceUtil.addLayoutSetPrototype(
						userId, portletDataContext.getCompanyId(),
						layoutSetPrototype.getNameMap(),
						layoutSetPrototype.getDescription(),
						layoutSetPrototype.isActive(), layoutsUpdateable,
						serviceContext);
			}
			else {
				importedLayoutSetPrototype =
					LayoutSetPrototypeLocalServiceUtil.updateLayoutSetPrototype(
						existingLayoutSetPrototype.getLayoutSetPrototypeId(),
						layoutSetPrototype.getNameMap(),
						layoutSetPrototype.getDescription(),
						layoutSetPrototype.isActive(), layoutsUpdateable,
						serviceContext);
			}
		}
		else {
			importedLayoutSetPrototype =
				LayoutSetPrototypeLocalServiceUtil.addLayoutSetPrototype(
					userId, portletDataContext.getCompanyId(),
					layoutSetPrototype.getNameMap(),
					layoutSetPrototype.getDescription(),
					layoutSetPrototype.isActive(), layoutsUpdateable,
					serviceContext);
		}

		portletDataContext.importClassedModel(
			layoutSetPrototype, importedLayoutSetPrototype,
			LayoutSetPrototypePortletDataHandler.NAMESPACE);
	}

	protected void exportLayoutLar(
			PortletDataContext portletDataContext,
			LayoutSetPrototype layoutSetPrototype)
		throws PortalException, SystemException {

		LayoutSet layoutSet = layoutSetPrototype.getLayoutSet();

		String path = getLayoutLarPath(portletDataContext, layoutSetPrototype);

		Map<String, String[]> parameters = getLayoutImportExportParameters();

		byte[] layouts =  LayoutLocalServiceUtil.exportLayouts(
			layoutSet.getGroupId(), layoutSet.isPrivateLayout(), parameters,
			null, null);

		portletDataContext.addZipEntry(path, layouts);
	}

	protected Map<String, String[]> getLayoutImportExportParameters() {
		return LAR_PARAMETERS;
	}

	protected String getLayoutLarPath(
		PortletDataContext portletDataContext,
		LayoutSetPrototype layoutSetPrototype) {

		StringBundler sb = new StringBundler(6);

		sb.append(ExportImportPathUtil.getPortletPath(
			portletDataContext, PortletKeys.LAYOUT_SET_PROTOTYPE));
		sb.append(StringPool.SLASH);
		sb.append(LayoutPrototypePortletDataHandler.NAMESPACE);
		sb.append(StringPool.SLASH);
		sb.append(layoutSetPrototype.getLayoutSetPrototypeId());
		sb.append("-layout.lar");

		return sb.toString();
	}

	private static final Map<String, String[]> LAR_PARAMETERS =
		new HashMap<String, String[]>();

	static {
		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE,
			new String[]{PortletDataHandlerKeys.
				LAYOUTS_IMPORT_MODE_MERGE_BY_LAYOUT_UUID});

		LAR_PARAMETERS.put(PortletDataHandlerKeys.PERMISSIONS,
			new String[]{String.valueOf(true)});

		LAR_PARAMETERS.put(PortletDataHandlerKeys.PORTLET_SETUP_ALL,
			new String[]{String.valueOf(true)});

		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.DELETE_MISSING_LAYOUTS,
			new String[]{String.valueOf(true)});

		LAR_PARAMETERS.put(
			PortletDataHandlerKeys.LAYOUT_SET_PROTOTYPE_LINK_ENABLED,
			new String[]{String.valueOf(false)});
	}

}
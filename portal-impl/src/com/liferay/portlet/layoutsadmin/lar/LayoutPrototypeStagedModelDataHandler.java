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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniela Zapata Riesco
 */
public class LayoutPrototypeStagedModelDataHandler
	extends BaseStagedModelDataHandler <LayoutPrototype> {

	public static final String[] CLASS_NAMES =
		{LayoutPrototype.class.getName()};

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext,
			LayoutPrototype layoutPrototype)
		throws Exception {

		Element layoutPrototypeElement =
			portletDataContext.getExportDataElement(layoutPrototype);

		portletDataContext.addClassedModel(
			layoutPrototypeElement,
			ExportImportPathUtil.getModelPath(layoutPrototype), layoutPrototype,
			LayoutPrototypePortletDataHandler.NAMESPACE);

		exportLayoutLar(portletDataContext, layoutPrototype);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			LayoutPrototype layoutPrototype)
		throws Exception {

		long userId = portletDataContext.getUserId(
			layoutPrototype.getUserUuid());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			layoutPrototype, LayoutPrototypePortletDataHandler.NAMESPACE);

		LayoutPrototype importedLayoutPrototype = null;

		if (portletDataContext.isDataStrategyMirror()) {
			LayoutPrototype existingLayoutPrototype =
				LayoutPrototypeLocalServiceUtil.
					fetchLayoutPrototypeByUuidAndCompanyId(
						layoutPrototype.getUuid(),
						portletDataContext.getCompanyId());

			if (existingLayoutPrototype == null) {
				serviceContext.setUuid(layoutPrototype.getUuid());

				importedLayoutPrototype =
					LayoutPrototypeLocalServiceUtil.addLayoutPrototype(
						userId, portletDataContext.getCompanyId(),
						layoutPrototype.getNameMap(),
						layoutPrototype.getDescription(),
						layoutPrototype.isActive(), serviceContext);
			}
			else {
				importedLayoutPrototype =
					LayoutPrototypeLocalServiceUtil.updateLayoutPrototype(
						existingLayoutPrototype.getLayoutPrototypeId(),
						layoutPrototype.getNameMap(),
						layoutPrototype.getDescription(),
						layoutPrototype.isActive(), serviceContext);
			}
		}
		else {
			importedLayoutPrototype =
				LayoutPrototypeLocalServiceUtil.addLayoutPrototype(
					userId, portletDataContext.getCompanyId(),
					layoutPrototype.getNameMap(),
					layoutPrototype.getDescription(),
					layoutPrototype.isActive(), serviceContext);
		}

		portletDataContext.importClassedModel(
			layoutPrototype, importedLayoutPrototype,
			LayoutPrototypePortletDataHandler.NAMESPACE);
	}

	protected void exportLayoutLar(
			PortletDataContext portletDataContext,
			LayoutPrototype layoutPrototype)
		throws PortalException, SystemException {

		Layout layout = layoutPrototype.getLayout();

		String path = getLayoutLarPath(portletDataContext, layoutPrototype);

		Map<String, String[]> parameters = getLayoutImportExportParameters();

		byte[] layouts =  LayoutLocalServiceUtil.exportLayouts(
			layout.getGroupId(), layout.isPrivateLayout(), parameters, null,
			null);

		portletDataContext.addZipEntry(path, layouts);
	}

	protected Map<String, String[]> getLayoutImportExportParameters() {
		return LAR_PARAMETERS;
	}

	protected String getLayoutLarPath(
		PortletDataContext portletDataContext,
		LayoutPrototype layoutPrototype) {

		StringBundler sb = new StringBundler(6);

		sb.append(ExportImportPathUtil.getPortletPath(
			portletDataContext, PortletKeys.LAYOUT_SET_PROTOTYPE));
		sb.append(StringPool.SLASH);
		sb.append(LayoutPrototypePortletDataHandler.NAMESPACE);
		sb.append(StringPool.SLASH);
		sb.append(layoutPrototype.getLayoutPrototypeId());
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
	}

}
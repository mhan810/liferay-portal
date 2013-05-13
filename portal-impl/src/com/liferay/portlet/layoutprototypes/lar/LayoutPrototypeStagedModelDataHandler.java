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

package com.liferay.portlet.layoutprototypes.lar;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

		exportLayouts(portletDataContext, layoutPrototype);

		portletDataContext.addClassedModel(
			layoutPrototypeElement,
			ExportImportPathUtil.getModelPath(layoutPrototype), layoutPrototype,
			LayoutPrototypePortletDataHandler.NAMESPACE);
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

		importLayouts(
			userId, portletDataContext, layoutPrototype,
			importedLayoutPrototype);
	}

	protected void exportLayouts(
			PortletDataContext portletDataContext,
			LayoutPrototype layoutPrototype)
		throws PortalException, SystemException {

		Layout layout = layoutPrototype.getLayout();

		String path = getLayoutExportLarPath(
			portletDataContext, layoutPrototype);

		Map<String, String[]> parameters = getLayoutImportExportParameters();

		File layoutsFile = null;

		FileInputStream fis = null;

		try {
			layoutsFile = LayoutLocalServiceUtil.exportLayoutsAsFile(
				layout.getGroupId(), layout.isPrivateLayout(), null, parameters,
				null, null);

			fis = new FileInputStream(layoutsFile);

			portletDataContext.addZipEntry(path, fis);
		}
		catch (FileNotFoundException fnfe) {
			throw new SystemException(
				"Unable to find temporary layout file", fnfe);
		}
		finally {
			StreamUtil.cleanUp(fis);

			FileUtil.delete(layoutsFile);
		}
	}

	protected String getLayoutExportLarPath(
		PortletDataContext portletDataContext,
		LayoutPrototype layoutPrototype) {

		StringBundler sb = new StringBundler(6);

		sb.append(ExportImportPathUtil.getPortletPath(
			portletDataContext, PortletKeys.LAYOUT_PROTOTYPE));

		return getLayoutLarPath(layoutPrototype, sb);
	}

	protected Map<String, String[]> getLayoutImportExportParameters() {

		Map<String, String[]> parameterMap = new HashMap<String, String[]>();

		parameterMap.put(
			PortletDataHandlerKeys.DELETE_MISSING_LAYOUTS,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.LAYOUTS_IMPORT_MODE,
			new String[] {
				PortletDataHandlerKeys.
					LAYOUTS_IMPORT_MODE_MERGE_BY_LAYOUT_UUID});
		parameterMap.put(
			PortletDataHandlerKeys.PERMISSIONS,
			new String[] {Boolean.TRUE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_SETUP_ALL,
			new String[] {Boolean.TRUE.toString()});

		return parameterMap;
	}

	protected String getLayoutImportLarPath(
		PortletDataContext portletDataContext,
		LayoutPrototype layoutPrototype) {

		StringBundler sb = new StringBundler(6);

		sb.append(ExportImportPathUtil.getSourcePortletPath(
			portletDataContext, PortletKeys.LAYOUT_PROTOTYPE));

		return getLayoutLarPath(layoutPrototype, sb);
	}

	protected String getLayoutLarPath(
		LayoutPrototype layoutPrototype, StringBundler sb) {
		sb.append(StringPool.SLASH);
		sb.append(LayoutPrototypePortletDataHandler.NAMESPACE);
		sb.append(StringPool.SLASH);
		sb.append(layoutPrototype.getLayoutPrototypeId());
		sb.append("-layout.lar");

		return sb.toString();
	}

	protected void importLayouts(
			long userId, PortletDataContext portletDataContext,
			LayoutPrototype layoutPrototype,
			LayoutPrototype importedLayoutPrototype)
		throws PortalException, SystemException {

		String path = getLayoutImportLarPath(
			portletDataContext, layoutPrototype);

		long groupId = importedLayoutPrototype.getGroup().getGroupId();

		Map<String, String[]> parameters = getLayoutImportExportParameters();

		InputStream is = null;

		try {
			is = portletDataContext.getZipEntryAsInputStream(path);

			LayoutLocalServiceUtil.importLayouts(
				userId, groupId, true, parameters, is);
		}
		finally {
			StreamUtil.cleanUp(is);
		}
	}

}
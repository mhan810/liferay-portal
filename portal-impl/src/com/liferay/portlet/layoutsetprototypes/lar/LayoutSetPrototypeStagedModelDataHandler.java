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

package com.liferay.portlet.layoutsetprototypes.lar;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataException;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.List;
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

		exportLayoutPrototypes(
			portletDataContext, layoutSetPrototype, layoutSetPrototypeElement);

		exportLayouts(portletDataContext, layoutSetPrototype);

		portletDataContext.addClassedModel(
			layoutSetPrototypeElement,
			ExportImportPathUtil.getModelPath(layoutSetPrototype),
			layoutSetPrototype, LayoutSetPrototypePortletDataHandler.NAMESPACE);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			LayoutSetPrototype layoutSetPrototype)
		throws Exception {

		importLayoutPrototype(portletDataContext, layoutSetPrototype);

		long userId = portletDataContext.getUserId(
			layoutSetPrototype.getUserUuid());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			layoutSetPrototype, LayoutSetPrototypePortletDataHandler.NAMESPACE);

		UnicodeProperties settingsProperties =
			layoutSetPrototype.getSettingsProperties();

		boolean layoutsUpdateable = GetterUtil.getBoolean(
			settingsProperties.getProperty("layoutsUpdateable"), true);

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

		importLayouts(
			userId, portletDataContext, layoutSetPrototype,
			importedLayoutSetPrototype);

		portletDataContext.importClassedModel(
			layoutSetPrototype, importedLayoutSetPrototype,
			LayoutSetPrototypePortletDataHandler.NAMESPACE);
	}

	protected void exportLayoutPrototypes(
			PortletDataContext portletDataContext,
			LayoutSetPrototype layoutSetPrototype,
			Element layoutSetPrototypeElement)
		throws Exception {

		Class<?> clazz = getClass();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Layout.class, clazz.getClassLoader());

		Property groupIdProperty = PropertyFactoryUtil.forName("groupId");

		dynamicQuery.add(
			groupIdProperty.eq(layoutSetPrototype.getGroup().getGroupId()));

		Property layoutPrototypeUuidProperty = PropertyFactoryUtil.forName(
			"layoutPrototypeLinkEnabled");

		dynamicQuery.add(layoutPrototypeUuidProperty.eq(true));

		List<Layout> layouts = LayoutLocalServiceUtil.dynamicQuery(
			dynamicQuery);

		for (Layout layout : layouts) {
			String layoutPrototypeUuid = layout.getLayoutPrototypeUuid();

			LayoutPrototype layoutPrototype =
				LayoutPrototypeLocalServiceUtil.
					getLayoutPrototypeByUuidAndCompanyId(
						layoutPrototypeUuid, portletDataContext.getCompanyId());

			portletDataContext.addReferenceElement(
				layout, layoutSetPrototypeElement, layoutPrototype,
				PortletDataContext.REFERENCE_TYPE_DEPENDENCY, false);

			StagedModelDataHandlerUtil.exportStagedModel(
				portletDataContext, layoutPrototype);
		}
	}

	protected void exportLayouts(
			PortletDataContext portletDataContext,
			LayoutSetPrototype layoutSetPrototype)
		throws PortalException, SystemException {

		LayoutSet layoutSet = layoutSetPrototype.getLayoutSet();

		String path = getLayoutExportLarPath(
			portletDataContext, layoutSetPrototype);

		Map<String, String[]> parameters = getLayoutImportExportParameters();

		File layoutsFile = null;

		FileInputStream fis = null;

		try {
			layoutsFile = LayoutLocalServiceUtil.exportLayoutsAsFile(
				layoutSet.getGroupId(), layoutSet.isPrivateLayout(), null,
				parameters, null, null);

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
		LayoutSetPrototype layoutSetPrototype) {

		StringBundler sb = new StringBundler(6);

		sb.append(ExportImportPathUtil.getPortletPath(
			portletDataContext, PortletKeys.LAYOUT_SET_PROTOTYPE));

		return getLayoutLarPath(layoutSetPrototype, sb);
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
			PortletDataHandlerKeys.LAYOUT_SET_PROTOTYPE_LINK_ENABLED,
			new String[] {Boolean.FALSE.toString()});
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
		LayoutSetPrototype layoutSetPrototype) {

		StringBundler sb = new StringBundler(6);

		sb.append(ExportImportPathUtil.getSourcePortletPath(
			portletDataContext, PortletKeys.LAYOUT_SET_PROTOTYPE));

		return getLayoutLarPath(layoutSetPrototype, sb);
	}

	protected String getLayoutLarPath(
		LayoutSetPrototype layoutSetPrototype, StringBundler sb) {
		sb.append(StringPool.SLASH);
		sb.append(LayoutSetPrototypePortletDataHandler.NAMESPACE);
		sb.append(StringPool.SLASH);
		sb.append(layoutSetPrototype.getLayoutSetPrototypeId());
		sb.append("-layout.lar");

		return sb.toString();
	}

	protected void importLayoutPrototype(
			PortletDataContext portletDataContext,
			LayoutSetPrototype layoutSetPrototype)
		throws PortletDataException {

		List<Element> layoutPrototypesElement =
			portletDataContext.getReferenceDataElements(
				layoutSetPrototype, LayoutPrototype.class);

		for (Element layoutPrototypeElement : layoutPrototypesElement) {
			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, layoutPrototypeElement);
		}
	}

	protected void importLayouts(
			long userId, PortletDataContext portletDataContext,
			LayoutSetPrototype layoutSetPrototype,
			LayoutSetPrototype importedLayoutSetPrototype)
		throws PortalException, SystemException {

		String path = getLayoutImportLarPath(
			portletDataContext, layoutSetPrototype);

		long groupId = importedLayoutSetPrototype.getGroup().getGroupId();

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

		LayoutSet layoutSet = importedLayoutSetPrototype.getLayoutSet();

		layoutSet.setLayoutSetPrototypeUuid(StringPool.BLANK);
		layoutSet.setLayoutSetPrototypeLinkEnabled(false);
	}

}
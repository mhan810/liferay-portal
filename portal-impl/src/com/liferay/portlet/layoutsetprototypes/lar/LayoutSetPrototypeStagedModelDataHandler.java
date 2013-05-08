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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataException;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;

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

		exportLayoutLar(portletDataContext, layoutSetPrototype);

		exportLayoutPrototypes(
			portletDataContext, layoutSetPrototype, layoutSetPrototypeElement);

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

		importLayoutLar(
			userId, portletDataContext, layoutSetPrototype,
			importedLayoutSetPrototype);

		portletDataContext.importClassedModel(
			layoutSetPrototype, importedLayoutSetPrototype,
			LayoutSetPrototypePortletDataHandler.NAMESPACE);
	}

	protected void exportLayoutLar(
			PortletDataContext portletDataContext,
			LayoutSetPrototype layoutSetPrototype)
		throws PortalException, SystemException {

		LayoutSet layoutSet = layoutSetPrototype.getLayoutSet();

		String path = getLayoutExportLarPath(
			portletDataContext, layoutSetPrototype);

		Map<String, String[]> parameters = getLayoutImportExportParameters();

		byte[] layouts = LayoutLocalServiceUtil.exportLayouts(
			layoutSet.getGroupId(), layoutSet.isPrivateLayout(), parameters,
			null, null);

		portletDataContext.addZipEntry(path, layouts);
	}

	protected void exportLayoutPrototypes(
			PortletDataContext portletDataContext,
			LayoutSetPrototype layoutSetPrototype,
			Element layoutSetPrototypeElement)
		throws Exception {

		LayoutSet layoutSet = layoutSetPrototype.getLayoutSet();

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
			layoutSet.getGroupId(), true,
			LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

		for (Layout layout : layouts) {
			if (layout.isLayoutPrototypeLinkEnabled()) {
				String layoutPrototypeUuid = layout.getLayoutPrototypeUuid();

				LayoutPrototype layoutPrototype =
					LayoutPrototypeLocalServiceUtil.
						getLayoutPrototypeByUuidAndCompanyId(
							layoutPrototypeUuid,
							portletDataContext.getCompanyId());

				portletDataContext.addReferenceElement(
					layoutSetPrototypeElement, layoutPrototype);

				StagedModelDataHandlerUtil.exportStagedModel(
					portletDataContext, layoutPrototype);
			}
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

	protected void importLayoutLar(
			long userId, PortletDataContext portletDataContext,
			LayoutSetPrototype layoutSetPrototype,
			LayoutSetPrototype importedLayoutSetPrototype)
		throws PortalException, SystemException {

		String path = getLayoutImportLarPath(
			portletDataContext, layoutSetPrototype);

		long groupId = importedLayoutSetPrototype.getGroup().getGroupId();

		byte[] larBytes = portletDataContext.getZipEntryAsByteArray(path);

		Map<String, String[]> parameters = getLayoutImportExportParameters();

		LayoutLocalServiceUtil.importLayouts(
			userId, groupId, true, parameters, larBytes);

		LayoutSet layoutSet = importedLayoutSetPrototype.getLayoutSet();

		layoutSet.setLayoutSetPrototypeUuid(StringPool.BLANK);
		layoutSet.setLayoutSetPrototypeLinkEnabled(false);
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

}
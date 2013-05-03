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

import com.liferay.portal.events.AddDefaultLayoutSetPrototypesAction;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;
import com.liferay.portal.service.persistence.LayoutSetPrototypeActionableDynamicQuery;

import java.util.List;

import javax.portlet.PortletPreferences;

/**
 * @author Daniela Zapata Riesco
 */
public class LayoutSetPrototypePortletDataHandler
	extends BasePortletDataHandler {

	public static final String NAMESPACE = "layout_set_prototypes";

	@Override
	protected PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		if (portletDataContext.addPrimaryKey(
				LayoutSetPrototypePortletDataHandler.class, "deleteData")) {

			return portletPreferences;
		}

		LayoutSetPrototypeLocalServiceUtil.deleteLayoutSetPrototypes(
			portletDataContext.getCompanyId());

		SimpleAction addDefaultLayoutSetPrototypesAction =
			new AddDefaultLayoutSetPrototypesAction();

		addDefaultLayoutSetPrototypesAction.run(
			new String[]{String.valueOf(portletDataContext.getCompanyId())});

		return portletPreferences;
	}

	@Override
	protected String doExportData(
			final PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		portletDataContext.addPermissions(
			"com.liferay.portlet.layoutsadmin",
			portletDataContext.getScopeGroupId());

		Element rootElement = addExportDataRootElement(portletDataContext);

		rootElement.addAttribute(
			"group-id", String.valueOf(portletDataContext.getScopeGroupId()));

		ActionableDynamicQuery actionableDynamicQuery =
			new LayoutSetPrototypeActionableDynamicQuery() {

			@Override
			protected void addCriteria(DynamicQuery dynamicQuery) {
				portletDataContext.addDateRangeCriteria(
					dynamicQuery, "modifiedDate");
			}

			@Override
			protected void performAction(Object object) throws PortalException {
				LayoutSetPrototype layoutSetPrototype =
					(LayoutSetPrototype)object;

				StagedModelDataHandlerUtil.exportStagedModel(
					portletDataContext, layoutSetPrototype);
			}
		};

		actionableDynamicQuery.setGroupId(portletDataContext.getScopeGroupId());

		actionableDynamicQuery.performActions();

		return getExportDataRootElementString(rootElement);
	}

	@Override
	protected PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
		throws Exception {

		portletDataContext.importPermissions(
			"com.liferay.portlet.layoutsadmin",
			portletDataContext.getSourceGroupId(),
			portletDataContext.getScopeGroupId());

		Element layoutSetPrototypesElement =
			portletDataContext.getImportDataGroupElement(
				LayoutSetPrototype.class);

		List<Element> layoutSetPrototypeElements =
			layoutSetPrototypesElement.elements();

		for (Element layoutSetPrototypeElement : layoutSetPrototypeElements) {
			StagedModelDataHandlerUtil.importStagedModel(
				portletDataContext, layoutSetPrototypeElement);
		}

		return null;
	}

}
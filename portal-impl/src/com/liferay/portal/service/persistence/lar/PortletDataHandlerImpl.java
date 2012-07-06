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

import com.liferay.portal.NoSuchPortletPreferencesException;
import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.lar.PortletDataHandlerKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.model.User;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.service.persistence.LayoutUtil;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portal.util.PortletKeys;

import java.util.List;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class PortletDataHandlerImpl extends BaseDataHandlerImpl<Portlet>
	implements PortletDataHandler {

	public void deserialize(Document document) {
		return;
	}

	public void digest(Portlet portlet) throws Exception {
		DataHandlerContext context = getDataHandlerContext();

		String path = getPortletPath(portlet.getPortletId());

		doDigest(portlet);

		digestPortletPreferences(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, false,
			portlet.getPortletId());

		digestPortletPreferences(
			context.getScopeGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
			false, portlet.getPortletId());

		digestPortletPreferences(
			context.getCompanyId(), PortletKeys.PREFS_OWNER_TYPE_COMPANY,
			false, portlet.getPortletId());
	}

	@Override
	protected void doDigest(Portlet portlet) throws Exception {
		return;
	}

	protected void digestPortletPreferences(
			long ownerId, int ownerType, boolean defaultUser, String portletId)
		throws Exception {

		DataHandlerContext context = getDataHandlerContext();

		long plid = PortletKeys.PREFS_OWNER_ID_DEFAULT;
		Layout layout = null;

		try {
			plid = context.getPlid();
			layout = LayoutLocalServiceUtil.getLayout(plid);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug("No plid is available from context: " +
					e.getMessage());
			}
		}

		PortletPreferences portletPreferences = null;

		if ((ownerType == PortletKeys.PREFS_OWNER_TYPE_COMPANY) ||
			(ownerType == PortletKeys.PREFS_OWNER_TYPE_GROUP) ||
			(ownerType == PortletKeys.PREFS_OWNER_TYPE_ARCHIVED)) {

			plid = PortletKeys.PREFS_OWNER_ID_DEFAULT;
		}

		try {
			portletPreferences =
				PortletPreferencesLocalServiceUtil.getPortletPreferences(
					ownerId, ownerType, plid, portletId);
		}
		catch (NoSuchPortletPreferencesException nsppe) {
			return;
		}

		LayoutTypePortlet layoutTypePortlet = null;

		if (layout != null) {
			layoutTypePortlet = (LayoutTypePortlet)layout.getLayoutType();
		}

		if ((layoutTypePortlet == null) ||
			layoutTypePortlet.hasPortletId(portletId)) {

			portletPreferencesDataHandler.digest(portletPreferences);
		}
	}

	@Override
	protected void doImport(LarDigestItem item) throws Exception {
		//Element portletsElement = null; //rootElement.element("portlets");

		//List<Element> portletElements = portletsElement.elements("portlet");

		DataHandlerContext context = getDataHandlerContext();

		Map parameterMap = context.getParameters();

		boolean deletePortletData = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.DELETE_PORTLET_DATA);

		Portlet portlet = (Portlet)getZipEntryAsObject(item.getPath());

		Map<Long, Layout> newLayoutsMap =
			(Map<Long, Layout>)context.getNewPrimaryKeysMap(Layout.class);

		// Delete portlet data

		/*if (deletePortletData) {
			if (_log.isDebugEnabled()) {
					_log.debug("Deleting portlet data");
			}

			//for (Element portletElement : portletElements) {
			String portletId = portlet.getPortletId();

			long layoutId = GetterUtil.getLong(
				portletElement.attributeValue("layout-id"));

			//long plid = newLayoutsMap.get(layoutId).getPlid();

			//context.setPlid(plid);

			// toDo: add portletImporter methods to here!
			//_portletImporter.deletePortletData(
			//	portletDataContext, portletId, plid);
			//}
		}

		// Import portlets

		if (_log.isDebugEnabled()) {
				_log.debug("Importing portlets");
		}

		//for (Element portletElement : portletElements) {
		String portletPath = portletElement.attributeValue("path");
		String portletId = portletElement.attributeValue("portlet-id");
		long layoutId = GetterUtil.getLong(
			portletElement.attributeValue("layout-id"));
		long oldPlid = GetterUtil.getLong(
			portletElement.attributeValue("old-plid"));

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			portletDataContext.getCompanyId(), portletId);

		if (!portlet.isActive() || portlet.isUndeployedPortlet()) {
			continue;
		}

		Layout layout = newLayoutsMap.get(layoutId);

		long plid = LayoutConstants.DEFAULT_PLID;

		if (layout != null) {
			plid = layout.getPlid();
		}

		layout = LayoutUtil.fetchByPrimaryKey(plid);

		if ((layout == null) && !group.isCompany()) {
			continue;
		}

		portletDataContext.setPlid(plid);
		portletDataContext.setOldPlid(oldPlid);

		Document portletDocument = SAXReaderUtil.read(
			portletDataContext.getZipEntryAsString(portletPath));

		portletElement = portletDocument.getRootElement();

		// The order of the import is important. You must always import
		// the portlet preferences first, then the portlet data, then
		// the portlet permissions. The import of the portlet data
		// assumes that portlet preferences already exist.

		_portletImporter.setPortletScope(
			portletDataContext, portletElement);

		long portletPreferencesGroupId = groupId;

		try {

			if ((layout != null) && !group.isCompany()) {
				portletPreferencesGroupId = layout.getGroupId();
			}

			// Portlet preferences

			_portletImporter.importPortletPreferences(
				portletDataContext, layoutSet.getCompanyId(),
				portletPreferencesGroupId, layout, null, portletElement,
				importPortletSetup, importPortletArchivedSetups,
				importPortletUserPreferences, false);

			// Portlet data

			Element portletDataElement = portletElement.element(
				"portlet-data");

			if (importPortletData && (portletDataElement != null)) {
				_portletImporter.importPortletData(
					portletDataContext, portletId, plid,
					portletDataElement);
			}
		}
		finally {
			_portletImporter.resetPortletScope(
				portletDataContext, portletPreferencesGroupId);
		}

		// Portlet permissions

		if (importPermissions) {
			_permissionImporter.importPortletPermissions(
				layoutCache, companyId, groupId, userId, layout,
				portletElement, portletId);
		}

		// Archived setups

		_portletImporter.importPortletPreferences(
			portletDataContext, layoutSet.getCompanyId(), groupId, null,
			null, portletElement, importPortletSetup,
			importPortletArchivedSetups, importPortletUserPreferences,
			false);
		//}

		if (importPermissions) {
			if (userId > 0) {
				Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(
					User.class);

				indexer.reindex(userId);
			}
		}

		// Asset links

		_portletImporter.readAssetLinks(portletDataContext);
		*/
	}

	public PortletDataHandlerControl[] getExportControls() {
		return new PortletDataHandlerControl[0];
	}

	public PortletDataHandlerControl[] getExportMetadataControls() {
		return new PortletDataHandlerControl[0];
	}

	public PortletDataHandlerControl[] getImportControls() {
		return new PortletDataHandlerControl[0];
	}

	public PortletDataHandlerControl[] getImportMetadataControls() {
		return new PortletDataHandlerControl[0];
	}

	public boolean isAlwaysExportable() {
		return _ALWAYS_EXPORTABLE;
	}

	public boolean isAlwaysStaged() {
		return _ALWAYS_STAGED;
	}

	public boolean isPublishToLiveByDefault() {
		return _PUBLISH_TO_LIVE_BY_DEFAULT;
	}

	private static final Log _log =
		LogFactoryUtil.getLog(PortletDataHandlerImpl.class);

	private static final boolean _ALWAYS_EXPORTABLE = false;

	private static final boolean _ALWAYS_STAGED = false;

	private static final boolean _PUBLISH_TO_LIVE_BY_DEFAULT = false;

}

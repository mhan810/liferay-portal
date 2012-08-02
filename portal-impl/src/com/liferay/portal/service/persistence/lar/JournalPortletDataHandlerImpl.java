
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

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.lar.digest.LarDigestItem;
import com.liferay.portal.model.Portlet;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalFeed;
import com.liferay.portlet.journal.model.JournalFolder;
import com.liferay.portlet.journal.model.JournalFolderConstants;
import com.liferay.portlet.journal.model.JournalStructure;
import com.liferay.portlet.journal.model.JournalTemplate;
import com.liferay.portlet.journal.service.persistence.JournalArticleUtil;
import com.liferay.portlet.journal.service.persistence.JournalFeedUtil;
import com.liferay.portlet.journal.service.persistence.JournalFolderUtil;
import com.liferay.portlet.journal.service.persistence.JournalStructureUtil;
import com.liferay.portlet.journal.service.persistence.JournalTemplateUtil;
import com.liferay.portlet.journal.util.comparator.ArticleIDComparator;
import com.liferay.portlet.journal.util.comparator.StructurePKComparator;

import java.util.List;

/**
 * @author Daniel Kocsis
 * @author Mate Thurzo
 */
public class JournalPortletDataHandlerImpl extends PortletDataHandlerImpl
	implements JournalPortletDataHandler {

	@Override
	public LarDigestItem doDigestPortlet(
			Portlet portlet, LarDigestItem item, DataHandlerContext context)
		throws Exception {

		return null;
	}

	@Override
	public void doImportData(LarDigestItem item, DataHandlerContext context)
		throws Exception {

		return;
	}

	@Override
	public String[] getDataPortletPreferences() {
		return new String[0];
	}

	@Override
	public PortletDataHandlerControl[] getExportControls() {
		return new PortletDataHandlerControl[0];
	}

	@Override
	public PortletDataHandlerControl[] getExportMetadataControls() {
		return new PortletDataHandlerControl[0];
	}

	@Override
	public PortletDataHandlerControl[] getImportControls() {
		return new PortletDataHandlerControl[0];
	}

	@Override
	public PortletDataHandlerControl[] getImportMetadataControls() {
		return new PortletDataHandlerControl[0];
	}

	@Override
	public boolean isAlwaysExportable() {
		return ALWAYS_EXPORTABLE;
	}

	@Override
	public boolean isAlwaysStaged() {
		return ALWAYS_STAGED;
	}

	@Override
	public boolean isDataLocalized() {
		return DATA_LOCALIZED;
	}

	@Override
	public boolean isPublishToLiveByDefault() {
		return PUBLISH_TO_LIVE_BY_DEFAULT;
	}

	private static Log _log = LogFactoryUtil.getLog(
		JournalPortletDataHandlerImpl.class);

	private static PortletDataHandlerControl[] _metadataControls =
		new PortletDataHandlerControl[] {
			new PortletDataHandlerBoolean(NAMESPACE, "images"),
			new PortletDataHandlerBoolean(NAMESPACE, "categories"),
			new PortletDataHandlerBoolean(NAMESPACE, "comments"),
			new PortletDataHandlerBoolean(NAMESPACE, "ratings"),
			new PortletDataHandlerBoolean(NAMESPACE, "tags")
		};

	private static PortletDataHandlerBoolean
		_structuresTemplatesAndFeeds = new PortletDataHandlerBoolean(
			NAMESPACE, "structures-templates-and-feeds", true, true);

}

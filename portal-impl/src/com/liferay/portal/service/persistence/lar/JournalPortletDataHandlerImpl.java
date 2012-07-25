
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
 */
public class JournalPortletDataHandlerImpl extends PortletDataHandlerImpl
	implements JournalPortletDataHandler {

	@Override
	public LarDigestItem doDigest(Portlet portlet) throws Exception {
		return null;
		return null;
	}

	private static final boolean _ALWAYS_EXPORTABLE = true;

	private static Log _log = LogFactoryUtil.getLog(
		JournalPortletDataHandlerImpl.class);

	private static PortletDataHandlerControl[] _metadataControls =
		new PortletDataHandlerControl[] {
			new PortletDataHandlerBoolean(_NAMESPACE, "images"),
			new PortletDataHandlerBoolean(_NAMESPACE, "categories"),
			new PortletDataHandlerBoolean(_NAMESPACE, "comments"),
			new PortletDataHandlerBoolean(_NAMESPACE, "ratings"),
			new PortletDataHandlerBoolean(_NAMESPACE, "tags")
		};

	private static PortletDataHandlerBoolean
		_structuresTemplatesAndFeeds = new PortletDataHandlerBoolean(
			_NAMESPACE, "structures-templates-and-feeds", true, true);

}

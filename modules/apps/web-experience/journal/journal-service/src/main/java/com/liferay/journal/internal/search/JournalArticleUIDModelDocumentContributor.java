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

package com.liferay.journal.internal.search;

import com.liferay.journal.configuration.JournalServiceConfiguration;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import com.liferay.portal.search.spi.model.index.contributor.SearchPermissionModelDocumentContributor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eric Yan
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.liferay.journal.model.JournalArticle",
	service = {
		ModelDocumentContributor.class,
		SearchPermissionModelDocumentContributor.class
	}
)
public class JournalArticleUIDModelDocumentContributor
	implements ModelDocumentContributor<JournalArticle>,
			   SearchPermissionModelDocumentContributor<JournalArticle> {

	@Override
	public void contribute(Document document, JournalArticle journalArticle) {
		long classPK = journalArticle.getId();

		if (!isIndexAllArticleVersions()) {
			classPK = journalArticle.getResourcePrimKey();
		}

		document.addUID(JournalArticle.class.getName(), classPK);
	}

	protected boolean isIndexAllArticleVersions() {
		try {
			JournalServiceConfiguration journalServiceConfiguration =
				configurationProvider.getCompanyConfiguration(
					JournalServiceConfiguration.class,
					CompanyThreadLocal.getCompanyId());

			return journalServiceConfiguration.indexAllArticleVersionsEnabled();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return false;
	}

	@Reference
	protected ConfigurationProvider configurationProvider;

	private static final Log _log = LogFactoryUtil.getLog(
		JournalArticleUIDModelDocumentContributor.class);

}
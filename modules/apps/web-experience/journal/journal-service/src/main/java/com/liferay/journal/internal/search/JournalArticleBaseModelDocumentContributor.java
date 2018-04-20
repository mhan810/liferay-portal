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
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentContributor;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eric Yan
 */
@Component(
	immediate = true,
	property = {
		"base.model.document.contributor=true",
		"indexer.class.name=com.liferay.journal.model.JournalArticle"
	},
	service = DocumentContributor.class
)
public class JournalArticleBaseModelDocumentContributor
	implements DocumentContributor {

	@Override
	public void contribute(Document document, BaseModel baseModel) {
		if (!(baseModel instanceof JournalArticle)) {
			return;
		}

		JournalArticle journalArticle = (JournalArticle)baseModel;

		long classPK = journalArticle.getId();

		if (!isIndexAllArticleVersions()) {
			classPK = journalArticle.getResourcePrimKey();
		}

		document.addUID(JournalArticle.class.getName(), classPK);
	}

	protected boolean isIndexAllArticleVersions() {
		try {
			JournalServiceConfiguration journalServiceConfiguration =
				_configurationProvider.getCompanyConfiguration(
					JournalServiceConfiguration.class,
					CompanyThreadLocal.getCompanyId());

			return journalServiceConfiguration.indexAllArticleVersionsEnabled();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JournalArticleBaseModelDocumentContributor.class);

	@Reference
	private ConfigurationProvider _configurationProvider;

}
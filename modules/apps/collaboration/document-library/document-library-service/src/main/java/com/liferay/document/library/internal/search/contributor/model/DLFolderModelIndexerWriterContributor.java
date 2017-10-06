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

package com.liferay.document.library.internal.search.contributor.model;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.search.contributor.model.ModelIndexerWriterContributor;
import com.liferay.portal.search.indexer.IndexerDocumentBuilder;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {
		"indexer.class.name=com.liferay.document.library.kernel.model.DLFolder"
	},
	service = ModelIndexerWriterContributor.class
)
public class DLFolderModelIndexerWriterContributor
	implements ModelIndexerWriterContributor<DLFolder> {

	@Override
	public void customize(
		final IndexableActionableDynamicQuery indexableActionableDynamicQuery,
		final IndexerDocumentBuilder<DLFolder> indexerDocumentBuilder,
		long companyId) {

		indexableActionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> {
				Property property = PropertyFactoryUtil.forName("mountPoint");

				dynamicQuery.add(property.eq(false));
			});

		indexableActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<DLFolder>() {

				@Override
				public void performAction(DLFolder dlFolder)
					throws PortalException {

					try {
						Document document = indexerDocumentBuilder.getDocument(
							dlFolder);

						indexableActionableDynamicQuery.addDocuments(document);
					}
					catch (PortalException pe) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Unable to index document library folder " +
									dlFolder.getFolderId(),
								pe);
						}
					}
				}

			});
	}

	@Override
	public Optional<DLFolder> getBaseModel(long classPK) {
		DLFolder dlFolder = dlFolderLocalService.fetchDLFolder(classPK);

		return Optional.ofNullable(dlFolder);
	}

	@Override
	public long getCompanyId(DLFolder dlFolder) {
		return dlFolder.getCompanyId();
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return dlFolderLocalService.getIndexableActionableDynamicQuery();
	}

	@Reference
	protected DLFolderLocalService dlFolderLocalService;

	private static final Log _log = LogFactoryUtil.getLog(
		DLFolderModelIndexerWriterContributor.class);

}
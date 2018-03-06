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

package com.liferay.portal.search.contributor.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.TrashedModel;
import com.liferay.portal.kernel.model.WorkflowedModel;
import com.liferay.portal.search.indexer.IndexerDocumentBuilder;

import java.util.Optional;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface ModelIndexerWriterContributor<T extends BaseModel> {

	public void customize(
		IndexableActionableDynamicQuery indexableActionableDynamicQuery,
		IndexerDocumentBuilder<T> indexerDocumentBuilder, long companyId);

	public Optional<T> getBaseModel(long classPK);

	public long getCompanyId(T object);

	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	public default IndexerWriterMode getIndexerWriterMode(T baseModel) {
		if ((baseModel instanceof WorkflowedModel) &&
			(baseModel instanceof TrashedModel)) {

			TrashedModel trashedModel = (TrashedModel)baseModel;
			WorkflowedModel workflowedModel = (WorkflowedModel)baseModel;

			if (!workflowedModel.isApproved() && !trashedModel.isInTrash()) {
				return IndexerWriterMode.SKIP;
			}
		}

		return IndexerWriterMode.UPDATE;
	}

	public default void modelIndexed(T baseModel) {
	}

}
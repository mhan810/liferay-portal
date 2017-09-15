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

package com.liferay.portal.search.internal.indexer;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.search.contributor.model.ModelSummaryContributor;
import com.liferay.portal.search.indexer.IndexerSummaryBuilder;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

/**
 * @author Michael C. Han
 */
public class IndexerSummaryBuilderImpl implements IndexerSummaryBuilder {

	public IndexerSummaryBuilderImpl(
		ModelSummaryContributor modelSummaryContributor,
		Iterable<IndexerPostProcessor> indexerPostProcessors) {

		_modelSummaryContributor = modelSummaryContributor;
		_indexerPostProcessors = indexerPostProcessors;
	}

	@Override
	public Summary getSummary(
		Document document, String snippet, PortletRequest portletRequest,
		PortletResponse portletResponse) {

		if (_modelSummaryContributor == null) {
			return null;
		}

		try {
			Locale locale = getLocale(portletRequest);

			Summary summary = _modelSummaryContributor.getSummary(
				document, locale, snippet, portletRequest, portletResponse);

			for (IndexerPostProcessor indexerPostProcessor :
					_indexerPostProcessors) {

				indexerPostProcessor.postProcessSummary(
					summary, document, locale, snippet);
			}

			return summary;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	protected Locale getLocale(PortletRequest portletRequest) {
		if (portletRequest != null) {
			return portletRequest.getLocale();
		}

		return LocaleUtil.getMostRelevantLocale();
	}

	private final Iterable<IndexerPostProcessor> _indexerPostProcessors;
	private final ModelSummaryContributor _modelSummaryContributor;

}
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

package com.liferay.portal.search.elasticsearch.internal.facet.daterange;

import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.search.elasticsearch.facet.FacetProcessor;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeBuilder;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 * @author Milen Dyankov
 */
@Component(
	immediate = true,
property = {"class.name=com.liferay.portal.kernel.search.facet.DateRangeFacet"}
)
public class DateRangeFacetProcessor
	implements FacetProcessor<SearchRequestBuilder> {

	@Override
	public void processFacet(
		SearchRequestBuilder searchRequestBuilder, Facet facet) {

		DateRangeFacet dateRangeFacet = (DateRangeFacet)facet;

		String fieldName =
			dateRangeFacet.getFacetConfiguration().getFieldName();
		DateRangeBuilder dateRangeBuilder = new DateRangeBuilder(fieldName);

		dateRangeBuilder.field(fieldName);

		if (dateRangeFacet.getTo() == null) {
			dateRangeBuilder.addUnboundedFrom(dateRangeFacet.getFrom());
		}
		else if (dateRangeFacet.getFrom() == null) {
			dateRangeBuilder.addUnboundedTo(dateRangeFacet.getTo());
		}
		else {
			dateRangeBuilder.addRange(
				dateRangeFacet.getFrom(), dateRangeFacet.getTo());
		}

		searchRequestBuilder.addAggregation(dateRangeBuilder);
	}

}
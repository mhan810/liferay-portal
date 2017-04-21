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

package com.liferay.portal.search.elasticsearch.internal.facet;

import java.io.IOException;

import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.AggregatorFactory;
import org.elasticsearch.search.aggregations.bucket.range.AbstractRangeBuilder;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregatorFactory;
import org.elasticsearch.search.aggregations.support.AggregationContext;
import org.elasticsearch.search.aggregations.support.ValuesSource;
import org.elasticsearch.search.aggregations.support.ValuesSourceAggregatorFactory;
import org.elasticsearch.search.aggregations.support.ValuesSourceConfig;

/**
 * @author Michael C. Han
 */
public class DefaultRangeBuilder
	extends AbstractRangeBuilder<DefaultRangeBuilder, RangeAggregator.Range> {

	public DefaultRangeBuilder(String name) {
		super(name, new InternalRange.Factory<>());
	}

	public DefaultRangeBuilder addRange(String from, String to) {
		return addRange(null, from, to);
	}

	public DefaultRangeBuilder addRange(String key, String from, String to) {
		ranges.add(new RangeAggregator.Range(key, from, to));

		return this;
	}

	public DefaultRangeBuilder addUnboundedFrom(String from) {
		return addUnboundedFrom(null, from);
	}

	public DefaultRangeBuilder addUnboundedFrom(String key, String from) {
		ranges.add(new RangeAggregator.Range(key, from, null));
		return this;
	}

	public DefaultRangeBuilder addUnboundedTo(String to) {
		return addUnboundedTo(null, to);
	}

	public DefaultRangeBuilder addUnboundedTo(String key, String to) {
		ranges.add(new RangeAggregator.Range(key, null, to));

		return this;
	}

	public DefaultRangeBuilder format(String format) {
		_format = format;

		return this;
	}

	@Override
	public String getWriteableName() {
		return "range";
	}

	public boolean hasRanges() {
		return !ranges.isEmpty();
	}

	@Override
	protected XContentBuilder doXContentBody(
			XContentBuilder builder, ToXContent.Params params)
		throws IOException {

		super.doXContentBody(builder, params);

		if (_format != null) {
			builder.field("format", _format);
		}

		return builder;
	}

	@Override
	protected ValuesSourceAggregatorFactory<ValuesSource.Numeric, ?> innerBuild(
			AggregationContext context,
			ValuesSourceConfig<ValuesSource.Numeric> config,
			AggregatorFactory<?> parent,
			AggregatorFactories.Builder factoriesBuilder)
		throws IOException {

		return new RangeAggregatorFactory(
			name, type, config,
			ranges.toArray(new RangeAggregator.Range[ranges.size()]), keyed,
			rangeFactory, context, parent, factoriesBuilder, metaData);
	}

	private String _format;

}
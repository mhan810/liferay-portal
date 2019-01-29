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

package com.liferay.portal.search.aggregation.bucket;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.AggregationResultTranslator;
import com.liferay.portal.search.aggregation.AggregationVisitor;
import com.liferay.portal.search.aggregation.BaseFieldAggregation;
import com.liferay.portal.search.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael C. Han
 */
@ProviderType
public class FiltersAggregation extends BaseFieldAggregation {

	public FiltersAggregation(String name, String field) {
		super(name, field);
	}

	@Override
	public <S extends AggregationResult, T> S accept(
		AggregationResultTranslator<S, T> aggregationResultTranslator,
		T aggregationResult) {

		return aggregationResultTranslator.translate(this, aggregationResult);
	}

	@Override
	public <T> T accept(AggregationVisitor<T> aggregationVisitor) {
		return aggregationVisitor.visit(this);
	}

	public void addKeyedQuery(String key, Query query) {
		_keyedQueries.add(new KeyedQuery(key, query));
	}

	public List<KeyedQuery> getKeyedQueries() {
		return Collections.unmodifiableList(_keyedQueries);
	}

	public Boolean getOtherBucket() {
		return _otherBucket;
	}

	public String getOtherBucketKey() {
		return _otherBucketKey;
	}

	public void setOtherBucket(Boolean otherBucket) {
		_otherBucket = otherBucket;
	}

	public void setOtherBucketKey(String otherBucketKey) {
		_otherBucketKey = otherBucketKey;
	}

	public class KeyedQuery {

		public KeyedQuery(String key, Query query) {
			_key = key;
			_query = query;
		}

		public String getKey() {
			return _key;
		}

		public Query getQuery() {
			return _query;
		}

		private final String _key;
		private final Query _query;

	}

	private List<KeyedQuery> _keyedQueries = new ArrayList<>();
	private Boolean _otherBucket;
	private String _otherBucketKey;

}
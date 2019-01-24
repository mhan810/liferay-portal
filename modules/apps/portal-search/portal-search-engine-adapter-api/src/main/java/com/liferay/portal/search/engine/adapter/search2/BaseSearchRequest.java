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

package com.liferay.portal.search.engine.adapter.search2;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregation;
import com.liferay.portal.search.query.Query;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael C. Han
 */
@ProviderType
public abstract class BaseSearchRequest {

	public Map<String, Aggregation> getAggregations() {
		return Collections.unmodifiableMap(_aggregations);
	}

	public Map<String, PipelineAggregation> getPipelineAggregations() {
		return Collections.unmodifiableMap(_pipelineAggregations);
	}

	public String[] getIndexNames() {
		return _indexNames;
	}


	public Query getPostFilterQuery() {
		return _postFilterQuery;
	}

	public Query getQuery() {
		return _query;
	}

	public Query getRescoreQuery() {
		return _rescoreQuery;
	}

	public boolean isIncludeResponseString() {
		return _includeResponseString;
	}

	public void putAggregation(Aggregation aggregation) {
		_aggregations.put(aggregation.getName(), aggregation);
	}

	public void putPipelineAggregation(PipelineAggregation pipelineAggregation) {
		_pipelineAggregations.put(
			pipelineAggregation.getName(), pipelineAggregation);
	}

	public void setIncludeResponseString(boolean includeResponseString) {
		_includeResponseString = includeResponseString;
	}

	public void setIndexNames(String[] indexNames) {
		_indexNames = indexNames;
	}

	public void setPostFilterQuery(Query postFilterQuery) {
		_postFilterQuery = postFilterQuery;
	}

	public void setQuery(Query query) {
		_query = query;
	}

	public Boolean getRequestCache() {
		return _requestCache;
	}

	public void setRequestCache(Boolean requestCache) {
		_requestCache = requestCache;
	}

	public void setRescoreQuery(Query rescoreQuery) {
		_rescoreQuery = rescoreQuery;
	}

	public Boolean getExplain() {
		return _explain;
	}

	public void setExplain(Boolean explain) {
		_explain = explain;
	}

	public Float getMinimumScore() {
		return _minimumScore;
	}

	public void setMinimumScore(Float minimumScore) {
		_minimumScore = minimumScore;
	}

	public Long getTimeoutInMilliseconds() {
		return _timeoutInMilliseconds;
	}

	public void setTimeoutInMilliseconds(Long timeoutInMilliseconds) {
		_timeoutInMilliseconds = timeoutInMilliseconds;
	}

	public Boolean getTrackTotalHits() {
		return _trackTotalHits;
	}

	public void setTrackTotalHits(Boolean trackTotalHits) {
		_trackTotalHits = trackTotalHits;
	}

	public String[] getTypes() {
		return _types;
	}

	public void setTypes(String... types) {
		_types = types;
	}

	public void addIndexBoost(String index, float boost) {
		_indexBoosts.put(index, boost);
	}

	public Map<String, Float> getIndexBoosts() {
		return Collections.unmodifiableMap(_indexBoosts);
	}

	private final Map<String, Aggregation> _aggregations = new HashMap<>();
	private Boolean _explain;
	private Map<String, Float> _indexBoosts = new HashMap<>();
	private boolean _includeResponseString;
	private String[] _indexNames;
	private String[] _types;
	private Float _minimumScore;
	private final Map<String, PipelineAggregation> _pipelineAggregations =
		new HashMap<>();
	private Query _postFilterQuery;
	private Query _query;
	private Boolean _requestCache;
	private Query _rescoreQuery;
	private Long _timeoutInMilliseconds;
	private Boolean _trackTotalHits;

}
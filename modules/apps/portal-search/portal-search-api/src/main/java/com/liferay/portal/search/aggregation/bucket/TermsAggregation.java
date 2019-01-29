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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael C. Han
 */
@ProviderType
public class TermsAggregation extends BaseFieldAggregation {

	public TermsAggregation(String name, String field) {
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

	public void addOrders(Order... orders) {
		Collections.addAll(_orders, orders);
	}

	public CollectionMode getCollectionMode() {
		return _collectionMode;
	}

	public String getExecutionHint() {
		return _executionHint;
	}

	public IncludeExcludeClause getIncludeExcludeClause() {
		return _includeExcludeClause;
	}

	public Integer getMinDocCount() {
		return _minDocCount;
	}

	public List<Order> getOrders() {
		return Collections.unmodifiableList(_orders);
	}

	public Integer getShardMinDocCount() {
		return _shardMinDocCount;
	}

	public Integer getShardSize() {
		return _shardSize;
	}

	public Boolean getShowTermDocCountError() {
		return _showTermDocCountError;
	}

	public Integer getSize() {
		return _size;
	}

	public void setCollectionMode(CollectionMode collectionMode) {
		_collectionMode = collectionMode;
	}

	public void setExecutionHint(String executionHint) {
		_executionHint = executionHint;
	}

	public void setIncludeExcludeClause(
		IncludeExcludeClause includeExcludeClause) {

		_includeExcludeClause = includeExcludeClause;
	}

	public void setMinDocCount(Integer minDocCount) {
		_minDocCount = minDocCount;
	}

	public void setShardMinDocCount(Integer shardMinDocCount) {
		_shardMinDocCount = shardMinDocCount;
	}

	public void setShardSize(Integer shardSize) {
		_shardSize = shardSize;
	}

	public void setShowTermDocCountError(Boolean showTermDocCountError) {
		_showTermDocCountError = showTermDocCountError;
	}

	public void setSize(Integer size) {
		_size = size;
	}

	private CollectionMode _collectionMode;
	private String _executionHint;
	private IncludeExcludeClause _includeExcludeClause;
	private Integer _minDocCount;
	private List<Order> _orders = new ArrayList<>();
	private Integer _shardMinDocCount;
	private Integer _shardSize;
	private Boolean _showTermDocCountError;
	private Integer _size;

}
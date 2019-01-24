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

package com.liferay.portal.search.aggregation.pipeline;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.sort.FieldSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael C. Han
 */
@ProviderType
public class BucketSortPipelineAggregation
	extends BasePipelineAggregation implements PipelineAggregation {

	public BucketSortPipelineAggregation(String name) {
		super(name);
	}

	@Override
	public <S extends AggregationResult, T> S accept(
		PipelineAggregationResultTranslator<S, T>
			pipelineAggregationResultTranslator,
		T aggregationResult) {

		throw new UnsupportedOperationException(
			"BucketSort does not return a separate AggregationResult");
	}

	@Override
	public <T> T accept(
		PipelineAggregationVisitor<T> pipelineAggregationVisitor) {

		return pipelineAggregationVisitor.visit(this);
	}

	public void addSortFields(FieldSort... fieldSorts) {
		Collections.addAll(_fieldSorts, fieldSorts);
	}

	public List<FieldSort> getFieldSorts() {
		return Collections.unmodifiableList(_fieldSorts);
	}

	public Integer getFrom() {
		return _from;
	}

	public GapPolicy getGapPolicy() {
		return _gapPolicy;
	}

	public Integer getSize() {
		return _size;
	}

	public void setFrom(Integer from) {
		_from = from;
	}

	public void setGapPolicy(GapPolicy gapPolicy) {
		_gapPolicy = gapPolicy;
	}

	public void setSize(Integer size) {
		_size = size;
	}

	private List<FieldSort> _fieldSorts = new ArrayList<>();
	private Integer _from;
	private GapPolicy _gapPolicy;
	private Integer _size;

}
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
import com.liferay.portal.search.script.Script;

/**
 * @author Michael C. Han
 */
@ProviderType
public class BucketSelectorPipelineAggregation
	extends BasePipelineAggregation implements PipelineAggregation {

	public BucketSelectorPipelineAggregation(
		String name, Script script, String... bucketsPaths) {

		super(name);

		_script = script;
		_bucketsPaths = bucketsPaths;
	}

	@Override
	public <S extends AggregationResult, T> S accept(
		PipelineAggregationResultTranslator<S, T>
			pipelineAggregationResultTranslator,
		T aggregationResult) {

		throw new UnsupportedOperationException(
			"BucketSelector does not return a separate AggregationResult");
	}

	@Override
	public <T> T accept(
		PipelineAggregationVisitor<T> pipelineAggregationVisitor) {

		return pipelineAggregationVisitor.visit(this);
	}

	public String[] getBucketsPaths() {
		return _bucketsPaths;
	}

	public GapPolicy getGapPolicy() {
		return _gapPolicy;
	}

	public Script getScript() {
		return _script;
	}

	public void setGapPolicy(GapPolicy gapPolicy) {
		_gapPolicy = gapPolicy;
	}

	private final String[] _bucketsPaths;
	private GapPolicy _gapPolicy;
	private final Script _script;

}
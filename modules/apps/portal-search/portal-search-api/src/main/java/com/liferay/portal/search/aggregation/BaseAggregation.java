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

package com.liferay.portal.search.aggregation;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.search.aggregation.pipeline.PipelineAggregation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Michael C. Han
 */
@ProviderType
public abstract class BaseAggregation implements Aggregation {

	public BaseAggregation(String name) {
		_name = name;
	}

	@Override
	public void addChildAggregation(Aggregation aggregation) {
		_childAggregation.add(aggregation);
	}

	@Override
	public void addChildAggregations(Aggregation... aggregations) {
		Collections.addAll(_childAggregation, aggregations);
	}

	@Override
	public void addPipelineAggregation(
		PipelineAggregation pipelineAggregation) {

		_pipelineAggregations.add(pipelineAggregation);
	}

	@Override
	public void addPipelineAggregations(
		PipelineAggregation... pipelineAggregations) {

		Collections.addAll(_pipelineAggregations, pipelineAggregations);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if ((object == null) || (getClass() != object.getClass())) {
			return false;
		}

		final BaseAggregation baseAggregation = (BaseAggregation)object;

		return Objects.equals(_name, baseAggregation._name);
	}

	@Override
	public Collection<Aggregation> getChildAggregations() {
		return Collections.unmodifiableCollection(_childAggregation);
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public Collection<PipelineAggregation> getPipelineAggregations() {
		return Collections.unmodifiableCollection(_pipelineAggregations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_name);
	}

	@Override
	public void removeChildAggregation(Aggregation aggregation) {
		_childAggregation.remove(aggregation);
	}

	@Override
	public void removePipelineAggregation(
		PipelineAggregation pipelineAggregation) {

		_pipelineAggregations.remove(pipelineAggregation);
	}

	private Set<Aggregation> _childAggregation = new LinkedHashSet<>();
	private final String _name;
	private Set<PipelineAggregation> _pipelineAggregations =
		new LinkedHashSet<>();

}
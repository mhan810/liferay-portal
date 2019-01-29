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

package com.liferay.portal.search.aggregation.metrics;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.AggregationResultTranslator;
import com.liferay.portal.search.aggregation.AggregationVisitor;
import com.liferay.portal.search.aggregation.BaseAggregation;
import com.liferay.portal.search.script.Script;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael C. Han
 */
@ProviderType
public class ScriptedMetricAggregation extends BaseAggregation {

	public ScriptedMetricAggregation(String name) {
		super(name);
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

	public void clearParameter(String paramName) {
		_parameters.remove(paramName);
	}

	public void clearParameters() {
		_parameters.clear();
	}

	public Script getCombineScript() {
		return _combineScript;
	}

	public Script getInitScript() {
		return _initScript;
	}

	public Script getMapScript() {
		return _mapScript;
	}

	public Map<String, Object> getParameters() {
		return Collections.unmodifiableMap(_parameters);
	}

	public Script getReduceScript() {
		return _reduceScript;
	}

	public void putParameter(String paramName, Object paramValue) {
		_parameters.put(paramName, paramValue);
	}

	public void setCombineScript(Script combineScript) {
		_combineScript = combineScript;
	}

	public void setInitScript(Script initScript) {
		_initScript = initScript;
	}

	public void setMapScript(Script mapScript) {
		_mapScript = mapScript;
	}

	public void setParameters(Map<String, Object> parameters) {
		_parameters = parameters;
	}

	public void setReduceScript(Script reduceScript) {
		_reduceScript = reduceScript;
	}

	private Script _combineScript;
	private Script _initScript;
	private Script _mapScript;
	private Map<String, Object> _parameters = new HashMap<>();
	private Script _reduceScript;

}
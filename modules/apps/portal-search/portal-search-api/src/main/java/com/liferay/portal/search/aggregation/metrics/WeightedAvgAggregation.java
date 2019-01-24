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
import com.liferay.portal.search.aggregation.ValueType;
import com.liferay.portal.search.script.Script;

/**
 * @author Michael C. Han
 */
@ProviderType
public class WeightedAvgAggregation extends BaseAggregation {

	public WeightedAvgAggregation(
		String name, String valueField, String weightField) {

		super(name);

		_valueField = valueField;
		_weightField = weightField;
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

	public String getFormat() {
		return _format;
	}

	public String getValueField() {
		return _valueField;
	}

	public Object getValueMissing() {
		return _valueMissing;
	}

	public Script getValueScript() {
		return _valueScript;
	}

	public ValueType getValueType() {
		return _valueType;
	}

	public String getWeightField() {
		return _weightField;
	}

	public Object getWeightMissing() {
		return _weightMissing;
	}

	public Script getWeightScript() {
		return _weightScript;
	}

	public void setFormat(String format) {
		_format = format;
	}

	public void setValueMissing(Object valueMissing) {
		_valueMissing = valueMissing;
	}

	public void setValueScript(Script valueScript) {
		_valueScript = valueScript;
	}

	public void setValueType(ValueType valueType) {
		_valueType = valueType;
	}

	public void setWeightMissing(Object weightMissing) {
		_weightMissing = weightMissing;
	}

	public void setWeightScript(Script weightScript) {
		_weightScript = weightScript;
	}

	private String _format;
	private final String _valueField;
	private Object _valueMissing;
	private Script _valueScript;
	private ValueType _valueType;
	private final String _weightField;
	private Object _weightMissing;
	private Script _weightScript;

}
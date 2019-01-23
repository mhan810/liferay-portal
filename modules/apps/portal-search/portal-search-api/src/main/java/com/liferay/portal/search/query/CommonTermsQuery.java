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

package com.liferay.portal.search.query;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Michael C. Han
 */
@ProviderType
public class CommonTermsQuery extends BaseQueryImpl implements Query {

	public CommonTermsQuery(String field, String text) {
		_field = field;
		_text = text;
	}

	@Override
	public <T> T accept(QueryVisitor<T> queryVisitor) {
		return queryVisitor.visit(this);
	}

	public String getAnalyzer() {
		return _analyzer;
	}

	public Float getCutoffFrequency() {
		return _cutoffFrequency;
	}

	public String getField() {
		return _field;
	}

	public String getHighFreqMinimumShouldMatch() {
		return _highFreqMinimumShouldMatch;
	}

	public Operator getHighFreqOperator() {
		return _highFreqOperator;
	}

	public String getLowFreqMinimumShouldMatch() {
		return _lowFreqMinimumShouldMatch;
	}

	public Operator getLowFreqOperator() {
		return _lowFreqOperator;
	}

	public String getText() {
		return _text;
	}

	public void setAnalyzer(String analyzer) {
		_analyzer = analyzer;
	}

	public void setCutoffFrequency(Float cutoffFrequency) {
		_cutoffFrequency = cutoffFrequency;
	}

	public void setHighFreqMinimumShouldMatch(
		String highFreqMinimumShouldMatch) {

		_highFreqMinimumShouldMatch = highFreqMinimumShouldMatch;
	}

	public void setHighFreqOperator(Operator highFreqOperator) {
		_highFreqOperator = highFreqOperator;
	}

	public void setLowFreqMinimumShouldMatch(String lowFreqMinimumShouldMatch) {
		_lowFreqMinimumShouldMatch = lowFreqMinimumShouldMatch;
	}

	public void setLowFreqOperator(Operator lowFreqOperator) {
		_lowFreqOperator = lowFreqOperator;
	}

	private String _analyzer;
	private Float _cutoffFrequency;
	private final String _field;
	private String _highFreqMinimumShouldMatch;
	private Operator _highFreqOperator = Operator.OR;
	private String _lowFreqMinimumShouldMatch;
	private Operator _lowFreqOperator = Operator.OR;
	private final String _text;

}
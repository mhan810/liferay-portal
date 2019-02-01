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

import com.liferay.portal.search.query.function.CombineFunction;
import com.liferay.portal.search.query.function.score.ScoreFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael C. Han
 */
@ProviderType
public class FunctionScoreQuery extends BaseQueryImpl {

	public FunctionScoreQuery(Query query) {
		_query = query;
	}

	@Override
	public <T> T accept(QueryVisitor<T> queryVisitor) {
		return queryVisitor.visit(this);
	}

	public void addFilterQueryScoreFunctionHolder(
		Query filterQuery, ScoreFunction scoreFunction) {

		_filterQueryScoreFunctionHolders.add(
			new FilterQueryScoreFunctionHolder(filterQuery, scoreFunction));
	}

	public CombineFunction getCombineFunction() {
		return _combineFunction;
	}

	public List<FilterQueryScoreFunctionHolder>
		getFilterQueryScoreFunctionHolders() {

		return Collections.unmodifiableList(_filterQueryScoreFunctionHolders);
	}

	public Float getMaxBoost() {
		return _maxBoost;
	}

	public Float getMinScore() {
		return _minScore;
	}

	public Query getQuery() {
		return _query;
	}

	public ScoreMode getScoreMode() {
		return _scoreMode;
	}

	public void setCombineFunction(CombineFunction combineFunction) {
		_combineFunction = combineFunction;
	}

	public void setMaxBoost(Float maxBoost) {
		_maxBoost = maxBoost;
	}

	public void setMinScore(Float minScore) {
		_minScore = minScore;
	}

	public void setScoreMode(ScoreMode scoreMode) {
		_scoreMode = scoreMode;
	}

	public class FilterQueryScoreFunctionHolder {

		public FilterQueryScoreFunctionHolder(
			Query filterQuery, ScoreFunction scoreFunction) {

			_filterQuery = filterQuery;
			_scoreFunction = scoreFunction;
		}

		public FilterQueryScoreFunctionHolder(ScoreFunction scoreFunction) {
			_filterQuery = null;
			_scoreFunction = scoreFunction;
		}

		public Query getFilterQuery() {
			return _filterQuery;
		}

		public ScoreFunction getScoreFunction() {
			return _scoreFunction;
		}

		private final Query _filterQuery;
		private final ScoreFunction _scoreFunction;

	}

	public enum ScoreMode {

		AVG, FIRST, MAX, MIN, MULTIPLY, SUM

	}

	private CombineFunction _combineFunction;
	private List<FilterQueryScoreFunctionHolder>
		_filterQueryScoreFunctionHolders = new ArrayList<>();
	private Float _maxBoost;
	private Float _minScore;
	private final Query _query;
	private ScoreMode _scoreMode;

}
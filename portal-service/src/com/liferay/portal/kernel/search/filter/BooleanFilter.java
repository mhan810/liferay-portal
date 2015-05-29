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

package com.liferay.portal.kernel.search.filter;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.generic.BooleanClauseImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael C. Han
 */
public class BooleanFilter extends BaseFilter {

	@Override
	public <T> T accept(FilterVisitor<T> filterVisitor) {
		return filterVisitor.visit(this);
	}

	public Filter add(Filter filter, BooleanClauseOccur booleanClauseOccur) {
		_booleanClauses.add(
			new BooleanClauseImpl<>(filter, booleanClauseOccur));

		return filter;
	}

	public Filter addRequiredTerm(String field, boolean value) {
		return addRequiredTerm(field, String.valueOf(value));
	}

	public Filter addRequiredTerm(String field, Boolean value) {
		return addRequiredTerm(field, String.valueOf(value));
	}

	public Filter addRequiredTerm(String field, double value) {
		return addRequiredTerm(field, String.valueOf(value));
	}

	public Filter addRequiredTerm(String field, Double value) {
		return addRequiredTerm(field, String.valueOf(value));
	}

	public Filter addRequiredTerm(String field, int value) {
		return addRequiredTerm(field, String.valueOf(value));
	}

	public Filter addRequiredTerm(String field, Integer value) {
		return addRequiredTerm(field, String.valueOf(value));
	}

	public Filter addRequiredTerm(String field, long value) {
		return addRequiredTerm(field, String.valueOf(value));
	}

	public Filter addRequiredTerm(String field, Long value) {
		return addRequiredTerm(field, String.valueOf(value));
	}

	public Filter addRequiredTerm(String field, short value) {
		return addRequiredTerm(field, String.valueOf(value));
	}

	public Filter addRequiredTerm(String field, Short value) {
		return addRequiredTerm(field, String.valueOf(value));
	}

	public Filter addRequiredTerm(String field, String value) {
		TermFilter termFilter = new TermFilter(field, value);

		return add(termFilter, BooleanClauseOccur.MUST);
	}

	public Filter addTerm(String field, boolean value) {
		return addTerm(field, String.valueOf(value));
	}

	public Filter addTerm(String field, Boolean value) {
		return addTerm(field, String.valueOf(value));
	}

	public Filter addTerm(String field, double value) {
		return addTerm(field, String.valueOf(value));
	}

	public Filter addTerm(String field, Double value) {
		return addTerm(field, String.valueOf(value));
	}

	public Filter addTerm(String field, int value) {
		return addTerm(field, String.valueOf(value));
	}

	public Filter addTerm(String field, Integer value) {
		return addTerm(field, String.valueOf(value));
	}

	public Filter addTerm(String field, long value) {
		return addTerm(field, String.valueOf(value));
	}

	public Filter addTerm(String field, Long value) {
		return addTerm(field, String.valueOf(value));
	}

	public Filter addTerm(String field, short value) {
		return addTerm(field, String.valueOf(value));
	}

	public Filter addTerm(String field, Short value) {
		return addTerm(field, String.valueOf(value));
	}

	public Filter addTerm(String field, String value) {
		return addTerm(field, value, BooleanClauseOccur.SHOULD);
	}

	public Filter addTerm(
		String field, String value, BooleanClauseOccur booleanClauseOccur) {

		TermFilter termFilter = new TermFilter(field, value);

		return add(termFilter, booleanClauseOccur);
	}

	public boolean hasClauses() {
		return !_booleanClauses.isEmpty();
	}

	private static final Log _log = LogFactoryUtil.getLog(BooleanFilter.class);

	private final List<BooleanClause<Filter>> _booleanClauses =
		new ArrayList<>();

}
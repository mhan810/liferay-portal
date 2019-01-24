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
import com.liferay.portal.search.highlight.Highlight;
import com.liferay.portal.search.script.ScriptField;
import com.liferay.portal.search.sort.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael C. Han
 */
@ProviderType
public class TopHitsAggregation extends BaseAggregation {

	public TopHitsAggregation(String name) {
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

	public void addScriptFields(ScriptField... scriptFields) {
		Collections.addAll(_scriptFields, scriptFields);
	}

	public void addSelectedFields(String... fields) {
		Collections.addAll(_selectedFields, fields);
	}

	public void addSortFields(Sort... sortFields) {
		Collections.addAll(_sortFields, sortFields);
	}

	public Boolean getExplain() {
		return _explain;
	}

	public Boolean getFetchSource() {
		return _fetchSource;
	}

	public String[] getFetchSourceExclude() {
		return _fetchSourceExclude;
	}

	public String[] getFetchSourceInclude() {
		return _fetchSourceInclude;
	}

	public Integer getFrom() {
		return _from;
	}

	public Highlight getHighlight() {
		return _highlight;
	}

	public List<ScriptField> getScriptFields() {
		return _scriptFields;
	}

	public List<String> getSelectedFields() {
		return Collections.unmodifiableList(_selectedFields);
	}

	public Integer getSize() {
		return _size;
	}

	public List<Sort> getSortFields() {
		return Collections.unmodifiableList(_sortFields);
	}

	public Boolean getTrackScores() {
		return _trackScores;
	}

	public Boolean getVersion() {
		return _version;
	}

	public void setExplain(Boolean explain) {
		_explain = explain;
	}

	public void setFetchSource(Boolean fetchSource) {
		_fetchSource = fetchSource;
	}

	public void setFetchSourceIncludeExclude(
		String[] fetchSourceInclude, String[] fetchSourceExclude) {

		_fetchSourceInclude = fetchSourceInclude;
		_fetchSourceExclude = fetchSourceExclude;

		if ((_fetchSourceExclude != null) || (_fetchSourceInclude != null)) {
			_fetchSource = Boolean.TRUE;
		}
	}

	public void setFrom(Integer from) {
		_from = from;
	}

	public void setHighlight(Highlight highlight) {
		_highlight = highlight;
	}

	public void setSize(Integer size) {
		_size = size;
	}

	public void setTrackScores(Boolean trackScores) {
		_trackScores = trackScores;
	}

	public void setVersion(Boolean version) {
		_version = version;
	}

	private Boolean _explain;
	private Boolean _fetchSource;
	private String[] _fetchSourceExclude;
	private String[] _fetchSourceInclude;
	private Integer _from;
	private Highlight _highlight;
	private List<ScriptField> _scriptFields = new ArrayList<>();
	private List<String> _selectedFields = new ArrayList<>();
	private Integer _size;
	private List<Sort> _sortFields = new ArrayList<>();
	private Boolean _trackScores;
	private Boolean _version;

}
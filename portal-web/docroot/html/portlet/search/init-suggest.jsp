<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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
--%>

<%@ page import="com.liferay.portal.kernel.search.KeywordsFormatter" %>

<%
boolean autocompleteSuggestionsEnabled = GetterUtil.getBoolean(portletPreferences.getValue("autocompleteSuggestionsEnabled", null), PropsValues.INDEX_SEARCH_AUTOCOMPLETE_SUGGESTIONS_ENABLED);
int autocompleteSuggestionsMax = GetterUtil.getInteger(portletPreferences.getValue("autocompleteSuggestionsMax", null), PropsValues.INDEX_SEARCH_AUTOCOMPLETE_SUGGESTIONS_MAX);
int autocompleteSuggestionsMinQueryLength = GetterUtil.getInteger(portletPreferences.getValue("autocompleteSuggestionsMinQueryLength", null), PropsValues.INDEX_SEARCH_AUTOCOMPLETE_SUGGESTIONS_MIN_QUERY_LENGTH);
int autocompleteSuggestionsQueryDelayMillis = GetterUtil.getInteger(portletPreferences.getValue("autocompleteSuggestionsQueryDelayMillis", null), PropsValues.INDEX_SEARCH_AUTOCOMPLETE_SUGGESTIONS_QUERY_DELAY_MILLIS);

if(autocompleteSuggestionsMax <= 0) {
	autocompleteSuggestionsMax = PropsValues.INDEX_SEARCH_AUTOCOMPLETE_SUGGESTIONS_MAX;
}
if(autocompleteSuggestionsMinQueryLength <= 0) {
	autocompleteSuggestionsMinQueryLength = PropsValues.INDEX_SEARCH_AUTOCOMPLETE_SUGGESTIONS_MIN_QUERY_LENGTH;
}
if(autocompleteSuggestionsQueryDelayMillis <= 0) {
	autocompleteSuggestionsQueryDelayMillis = PropsValues.INDEX_SEARCH_AUTOCOMPLETE_SUGGESTIONS_QUERY_DELAY_MILLIS;
}

int collatedSpellCheckResultDisplayThreshold = GetterUtil.getInteger(portletPreferences.getValue("collatedSpellCheckResultDisplayThreshold", null), PropsValues.INDEX_SEARCH_COLLATED_SPELL_CHECK_RESULT_SCORING_THRESHOLD);
boolean collatedSpellCheckResultEnabled = GetterUtil.getBoolean(portletPreferences.getValue("collatedSpellCheckResultEnabled", null), PropsValues.INDEX_SEARCH_COLLATED_SPELL_CHECK_RESULT_ENABLED);

if(collatedSpellCheckResultDisplayThreshold < 0) {
	collatedSpellCheckResultDisplayThreshold = PropsValues.INDEX_SEARCH_COLLATED_SPELL_CHECK_RESULT_SCORING_THRESHOLD;
}

int querySuggestionsDisplayThreshold = GetterUtil.getInteger(portletPreferences.getValue("querySuggestionsDisplayThreshold", null), PropsValues.INDEX_SEARCH_QUERY_SUGGESTION_SCORING_THRESHOLD);
boolean querySuggestionsEnabled = GetterUtil.getBoolean(portletPreferences.getValue("querySuggestionsEnabled", null), PropsValues.INDEX_SEARCH_QUERY_SUGGESTION_ENABLED);
int querySuggestionsMax = GetterUtil.getInteger(portletPreferences.getValue("querySuggestionsMax", null), PropsValues.INDEX_SEARCH_QUERY_SUGGESTION_MAX);

if (querySuggestionsDisplayThreshold < 0) {
	querySuggestionsDisplayThreshold = PropsValues.INDEX_SEARCH_QUERY_SUGGESTION_SCORING_THRESHOLD;
}
if (querySuggestionsMax <= 0) {
	querySuggestionsMax = PropsValues.INDEX_SEARCH_QUERY_SUGGESTION_MAX;
}

boolean queryIndexingEnabled = GetterUtil.getBoolean(portletPreferences.getValue("queryIndexingEnabled", null), PropsValues.INDEX_SEARCH_QUERY_INDEXING_ENABLED);
int queryIndexingThreshold = GetterUtil.getInteger(portletPreferences.getValue("queryIndexingThreshold", null), PropsValues.INDEX_SEARCH_QUERY_INDEXING_THRESHOLD);

if (queryIndexingThreshold < 0) {
	queryIndexingThreshold = PropsValues.INDEX_SEARCH_QUERY_INDEXING_THRESHOLD;
}
%>
<%--~
  ~ Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
  ~
  ~ This library is free software; you can redistribute it and/or modify it under
  ~ the terms of the GNU Lesser General Public License as published by the Free
  ~ Software Foundation; either version 2.1 of the License, or (at your option)
  ~ any later version.
  ~
  ~ This library is distributed in the hope that it will be useful, but WITHOUT
  ~ ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  ~ FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
  ~ details.
  --%>

<%@ page import="com.liferay.portal.kernel.search.KeywordsFormatter" %>

<%
int collatedSpellCheckResultDisplayThreshold = GetterUtil.getInteger(portletPreferences.getValue("collatedSpellCheckResultDisplayThreshold", null), PropsValues.INDEX_SEARCH_COLLATED_SPELL_CHECK_RESULT_SCORING_THRESHOLD);
boolean collatedSpellCheckResultEnabled = GetterUtil.getBoolean(portletPreferences.getValue("collatedSpellCheckResultEnabled", null), PropsValues.INDEX_SEARCH_COLLATED_SPELL_CHECK_RESULT_ENABLED);

if (collatedSpellCheckResultDisplayThreshold < 0) {
	collatedSpellCheckResultDisplayThreshold = PropsValues.INDEX_SEARCH_COLLATED_SPELL_CHECK_RESULT_SCORES_THRESHOLD;
}

int querySuggestionsDisplayThreshold = GetterUtil.getInteger(portletPreferences.getValue("querySuggestionsDisplayThreshold", null), PropsValues.INDEX_SEARCH_QUERY_SUGGESTION_SCORES_THRESHOLD);
boolean querySuggestionEnabled = GetterUtil.getBoolean(portletPreferences.getValue("querySuggestionEnabled", null), PropsValues.INDEX_SEARCH_QUERY_SUGGESTION_ENABLED);
int querySuggestionMax = GetterUtil.getInteger(portletPreferences.getValue("querySuggestionMax", null), PropsValues.INDEX_SEARCH_QUERY_SUGGESTION_MAX);

if (querySuggestionsDisplayThreshold < 0) {
	querySuggestionsDisplayThreshold = PropsValues.INDEX_SEARCH_QUERY_SUGGESTION_SCORING_THRESHOLD;
}
if (querySuggestionMax <= 0) {
	querySuggestionMax = PropsValues.INDEX_SEARCH_QUERY_SUGGESTION_MAX;
}

boolean queryIndexingEnabled = GetterUtil.getBoolean(portletPreferences.getValue("queryIndexingEnabled", null), PropsValues.INDEX_SEARCH_QUERY_INDEXING_ENABLED);
int queryIndexingThreshold = GetterUtil.getInteger(portletPreferences.getValue("queryIndexingThreshold", null), PropsValues.INDEX_SEARCH_QUERY_INDEXING_THRESHOLD);

if (queryIndexingThreshold < 0) {
	queryIndexingThreshold = PropsValues.INDEX_SEARCH_QUERY_INDEXING_THRESHOLD;
}
%>
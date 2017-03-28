<%--
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
--%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>

<%@ page import="com.liferay.portal.kernel.util.Constants" %>
<%@ page import="com.liferay.portal.search.web.internal.portlet.results.SearchResultsPortletPreferences" %>
<%@ page import="com.liferay.portal.search.web.internal.util.PortletPreferencesJspUtil" %>

<portlet:defineObjects />

<%
com.liferay.portal.search.web.internal.portlet.results.SearchResultsPortletPreferences searchResultsPortletPreferences = new com.liferay.portal.search.web.internal.portlet.results.SearchResultsPortletPreferencesImpl(java.util.Optional.ofNullable(portletPreferences));
%>

<liferay-portlet:actionURL portletConfiguration="<%= true %>" var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="<%= true %>" var="configurationRenderURL" />

<aui:form action="<%= configurationActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= configurationRenderURL %>" />

	<div class="portlet-configuration-body-content">
		<div class="container-fluid-1280">
			<aui:input label="portlet.search-results.enable-highlights" name="<%= PortletPreferencesJspUtil.getInputName(SearchResultsPortletPreferences.PREFERENCE_HIGHLIGHT_ENABLED) %>" type="checkbox" value="<%= searchResultsPortletPreferences.isHighlightEnabled() %>" />

			<aui:input label="portlet.search-results.view-in-context" name="<%= PortletPreferencesJspUtil.getInputName(SearchResultsPortletPreferences.PREFERENCE_VIEW_IN_CONTEXT) %>" type="checkbox" value="<%= searchResultsPortletPreferences.isViewInContext() %>" />

			<aui:input label="portlet.search-results.display-in-document-form" name="<%= PortletPreferencesJspUtil.getInputName(SearchResultsPortletPreferences.PREFERENCE_DISPLAY_IN_DOCUMENT_FORM) %>" type="checkbox" value="<%= searchResultsPortletPreferences.isDisplayInDocumentForm() %>" />

			<aui:input label="portlet.search-results.pagination-start-parameter-name" name="<%= PortletPreferencesJspUtil.getInputName(SearchResultsPortletPreferences.PREFERENCE_PAGINATION_START_PARAMETER_NAME) %>" value="<%= searchResultsPortletPreferences.getPaginationStartParameterName() %>" />

			<aui:input label="portlet.search-results.pagination-delta" name="<%= PortletPreferencesJspUtil.getInputName(SearchResultsPortletPreferences.PREFERENCE_PAGINATION_DELTA) %>" type="text" value="<%= searchResultsPortletPreferences.getPaginationDelta() %>" />

			<aui:input label="portlet.search-results.pagination-delta-parameter-name" name="<%= PortletPreferencesJspUtil.getInputName(SearchResultsPortletPreferences.PREFERENCE_PAGINATION_DELTA_PARAMETER_NAME) %>" type="text" value="<%= searchResultsPortletPreferences.getPaginationDeltaParameterName() %>" />
		</div>
	</div>

	<aui:button-row>
		<aui:button cssClass="btn-lg" type="submit" />
	</aui:button-row>
</aui:form>
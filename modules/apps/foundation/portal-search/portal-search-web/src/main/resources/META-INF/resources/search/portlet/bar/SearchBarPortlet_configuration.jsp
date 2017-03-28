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
<%@ page import="com.liferay.portal.search.web.internal.portlet.bar.SearchBarPortletPreferences" %>
<%@ page import="com.liferay.portal.search.web.internal.util.PortletPreferencesJspUtil" %>

<portlet:defineObjects />

<%
com.liferay.portal.search.web.internal.portlet.bar.SearchBarPortletPreferences searchBarPortletPreferences = new com.liferay.portal.search.web.internal.portlet.bar.SearchBarPortletPreferencesImpl(java.util.Optional.ofNullable(portletPreferences));
%>

<liferay-portlet:actionURL portletConfiguration="<%= true %>" var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="<%= true %>" var="configurationRenderURL" />

<aui:form action="<%= configurationActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= configurationRenderURL %>" />

	<div class="portlet-configuration-body-content">
		<div class="container-fluid-1280">
			<aui:fieldset>
				<aui:input label="portlet.search-bar.keywords-parameter-name" name="<%= PortletPreferencesJspUtil.getInputName(SearchBarPortletPreferences.PREFERENCE_KEYWORDS_PARAMETER_NAME) %>" value="<%= searchBarPortletPreferences.getKeywordsParameterNameString() %>" />

				<aui:select label="portlet.search-bar.scope" name="<%= PortletPreferencesJspUtil.getInputName(SearchBarPortletPreferences.PREFERENCE_SCOPE) %>" value="<%= searchBarPortletPreferences.getScopeString() %>">
					<aui:option label="portlet.search-bar.this-site" value="this-site" />
					<aui:option label="portlet.search-bar.everything" value="everything" />
					<aui:option label="portlet.search-bar.let-the-user-choose" value="let-the-user-choose" />
				</aui:select>

				<aui:input label="portlet.search-bar.destination-page" name="<%= PortletPreferencesJspUtil.getInputName(SearchBarPortletPreferences.PREFERENCE_DESTINATION) %>" value="<%= searchBarPortletPreferences.getDestinationString() %>" />
			</aui:fieldset>
		</div>
	</div>

	<aui:button-row>
		<aui:button cssClass="btn-lg" type="submit" />
	</aui:button-row>
</aui:form>
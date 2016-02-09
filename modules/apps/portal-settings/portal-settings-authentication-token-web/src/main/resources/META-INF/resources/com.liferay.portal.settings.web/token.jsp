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

<%@ include file="/com.liferay.portal.settings.web/init.jsp" %>

<%
TokenConfiguration tokenConfiguration = ConfigurationProviderUtil.getConfiguration(TokenConfiguration.class, new ParameterMapSettingsLocator(request.getParameterMap(), "token--", new CompanyServiceSettingsLocator(company.getCompanyId(), TokenConstants.SERVICE_NAME)));

String[] authenticationCookies = tokenConfiguration.authenticationCookies();
boolean enabled = tokenConfiguration.enabled();
boolean importFromLDAP = tokenConfiguration.importFromLDAP();
String logoutRedirectURL = tokenConfiguration.logoutRedirectURL();
TokenLocation tokenLocation;
String tokenLocationString;
try {
	tokenLocation = tokenConfiguration.tokenLocation();
	tokenLocationString = tokenLocation.name();
}
catch (Exception e) {
	tokenLocationString = StringPool.BLANK;
}
String userTokenName = tokenConfiguration.userTokenName();
%>

<aui:fieldset>

	<liferay-ui:error key="userAuthenticationCookiesInvalid" message="the-authentication-cookies-are-invalid" />
	<liferay-ui:error key="tokenLogoutURLInvalid" message="the-token-logout-url-is-invalid" />
	<liferay-ui:error key="userTokenNameInvalid" message="the-user-token-name-is-invalid" />
	<liferay-ui:error key="userTokenLocationInvalid" message="the-token-location-is-invalid" />

	<aui:input name="<%= ActionRequest.ACTION_NAME %>" type="hidden" value="/portal_settings/token" />

	<aui:input cssClass="lfr-input-text-container" helpMessage="authentication-cookies-help" label="authentication-cookies" name="token--authenticationCookies" type="text" value="<%= StringUtil.merge(authenticationCookies, StringPool.COMMA) %>" />

	<aui:input label="enabled" name="token--enabled" type="checkbox" value="<%= enabled %>" />

	<aui:input helpMessage="import-token-users-from-ldap-help" label="import-token-users-from-ldap" name="token--importFromLDAP" type="checkbox" value="<%= importFromLDAP %>" />

	<aui:input cssClass="lfr-input-text-container" helpMessage="logout-redirect-url-help" label="logout-redirect-url" name="token--logoutRedirectURL" type="text" value="<%= logoutRedirectURL %>" />

	<aui:input cssClass="lfr-input-text-container" helpMessage="token-location-help" label="token-location" name="token--tokenLocation" type="text" value="<%= tokenLocationString %>" />

	<aui:input cssClass="lfr-input-text-container" helpMessage="user-token-name-help" label="user-token-name" name="token--userTokenName" type="text" value="<%= userTokenName %>" />

</aui:fieldset>
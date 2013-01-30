<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

<%@ include file="/html/portlet/portal_settings/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");
String backURL = ParamUtil.getString(request, "backURL", redirect);

String casServerId = ParamUtil.getString(request, "casServerId");

CASServer casServer = CASManagerUtil.getCASServer(company.getCompanyId(), casServerId);

String casLoginURL = PrefsPropsUtil.getString(company.getCompanyId(), PropsKeys.CAS_LOGIN_URL, PropsValues.CAS_LOGIN_URL);
String casLogoutURL = PrefsPropsUtil.getString(company.getCompanyId(), PropsKeys.CAS_LOGOUT_URL, PropsValues.CAS_LOGOUT_URL);
String casServerURL = PrefsPropsUtil.getString(company.getCompanyId(), PropsKeys.CAS_SERVER_URL, PropsValues.CAS_SERVER_URL);
String casServiceURL = PrefsPropsUtil.getString(company.getCompanyId(), PropsKeys.CAS_SERVICE_URL, PropsValues.CAS_SERVICE_URL);

boolean add = Validator.isNull(casServerId);

if (!add) {
	casLoginURL = casServer.getLoginUrl();
	casLogoutURL = casServer.getLogoutUrl();
	casServerURL = casServer.getServerUrl();
	casServiceURL = casServer.getServiceUrl();
}
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title='<%= (add) ? "add-cas-server" : "edit-cas-server" %>'
/>

<portlet:actionURL var="editCASServerURL">
	<portlet:param name="struts_action" value="/portal_settings/edit_cas_server" />
</portlet:actionURL>

<aui:form action="<%= editCASServerURL %>" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveEntry(false);" %>'>

	<aui:fieldset>
		<liferay-ui:error key="ServerIdDuplicate" message="the-cas-server-id-already-exists" />
		<liferay-ui:error key="ServerIdDoesNotExist" message="the-cas-server-id-does-not-exist" />
		<liferay-ui:error key="ServerIdInvalid" message="the-cas-server-id-must-not-be-blank" />
		<liferay-ui:error key="ServerURLInvalid" message="the-cas-server-url-is-invalid" />
		<liferay-ui:error key="ServiceURLInvalid" message="the-cas-service-url-is-invalid" />
		<liferay-ui:error key="LoginURLInvalid" message="the-cas-login-url-is-invalid" />
		<liferay-ui:error key="LogoutURLInvalid" message="the-cas-logout-url-is-invalid" />

		<aui:input name="<%= Constants.CMD %>" type="hidden" />
		<aui:input name="redirect" type="hidden" value="<%= redirect %>" />

		<aui:input cssClass="lfr-input-text-container" helpMessage="cas-server-id-help" label="server-id" name="casServerId" type='<%= (add) ? "text" : "hidden" %>' value="<%= casServerId %>" />

		<aui:input cssClass="lfr-input-text-container" helpMessage="cas-login-url-help" label="login-url" name='<%= "settings--" + PropsKeys.CAS_LOGIN_URL + "--" %>' type="text" value="<%= casLoginURL %>" />

		<aui:input cssClass="lfr-input-text-container" helpMessage="cas-logout-url-help" label="logout-url" name='<%= "settings--" + PropsKeys.CAS_LOGOUT_URL + "--" %>' type="text" value="<%= casLogoutURL %>" />

		<aui:input cssClass="lfr-input-text-container" helpMessage="cas-server-url-help" label="server-url" name='<%= "settings--" + PropsKeys.CAS_SERVER_URL + "--" %>' type="text" value="<%= casServerURL %>" />

		<aui:input cssClass="lfr-input-text-container" helpMessage="cas-service-url-help" label="service-url" name='<%= "settings--" + PropsKeys.CAS_SERVICE_URL + "--" %>' type="text" value="<%= casServiceURL %>" />

		<aui:button-row>

			<%
			String taglibOnClick = renderResponse.getNamespace() + "testCasSettings();";
			%>

			<aui:button onClick="<%= taglibOnClick %>" value="test-cas-configuration" />

			<%
				taglibOnClick = renderResponse.getNamespace() + "saveCas();";
			%>

			<aui:button name="saveButton" onClick="<%= taglibOnClick %>" value="save" />

			<aui:button href="<%= redirect %>" name="cancelButton" type="cancel" />
		</aui:button-row>
	</aui:fieldset>
</aui:form>

<aui:script>
	function <portlet:namespace />saveCas() {
		document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = "<%= add ? Constants.ADD : Constants.UPDATE %>";

		submitForm(document.<portlet:namespace />fm);
	}

	Liferay.provide(
		window,
		'<portlet:namespace />testCasSettings',
		function() {
			var A = AUI();

			var title = 'CAS';

			var data = {};

			data.<portlet:namespace />casLoginURL = document.<portlet:namespace />fm['<portlet:namespace />settings--<%= PropsKeys.CAS_LOGIN_URL %>--'].value;
			data.<portlet:namespace />casLogoutURL = document.<portlet:namespace />fm['<portlet:namespace />settings--<%= PropsKeys.CAS_LOGOUT_URL %>--'].value;
			data.<portlet:namespace />casServerURL = document.<portlet:namespace />fm['<portlet:namespace />settings--<%= PropsKeys.CAS_SERVER_URL %>--'].value;
			data.<portlet:namespace />casServiceURL = document.<portlet:namespace />fm['<portlet:namespace />settings--<%= PropsKeys.CAS_SERVICE_URL %>--'].value;

			var url = "<portlet:renderURL windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>"><portlet:param name="struts_action" value="/portal_settings/test_cas_configuration" /></portlet:renderURL>";

			var dialog = new A.Dialog(
				{
					align: Liferay.Util.Window.ALIGN_CENTER,
					destroyOnClose: true,
					modal: true,
					title: Liferay.Language.get(title),
					width: 600
				}
			).render();

			dialog.plug(
				A.Plugin.IO,
				{
					data: data,
					uri: url
				}
			);
		},
		['aui-dialog', 'aui-io']
	);
</aui:script>

<%
	PortalUtil.addPortletBreadcrumbEntry(request, (add) ? LanguageUtil.get(pageContext, "add-cas-server") : casServerId, currentURL);
%>
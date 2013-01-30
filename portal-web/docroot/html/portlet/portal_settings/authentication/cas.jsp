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
String authenticationURL = currentURL + "#_LFR_FN_authentication";

boolean casAuthEnabled = PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.CAS_AUTH_ENABLED, PropsValues.CAS_AUTH_ENABLED);
boolean casAuthStrict = PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.CAS_AUTH_STRICT, PropsValues.CAS_AUTH_STRICT);
boolean casAutoRedirect = PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.CAS_AUTO_REDIRECT, PropsValues.CAS_AUTO_REDIRECT);
String casAutoRedirectServerId = PrefsPropsUtil.getString(company.getCompanyId(), PropsKeys.CAS_AUTO_REDIRECT_SERVER_ID, PropsValues.CAS_AUTO_REDIRECT_SERVER_ID);
boolean casImportFromLdap = PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.CAS_IMPORT_FROM_LDAP, PropsValues.CAS_IMPORT_FROM_LDAP);
String casNoSuchUserRedirectURL = PrefsPropsUtil.getString(company.getCompanyId(), PropsKeys.CAS_NO_SUCH_USER_REDIRECT_URL, PropsValues.CAS_NO_SUCH_USER_REDIRECT_URL);
String[] casServerIds = CASManagerUtil.getCASServerIds(company.getCompanyId());
%>

<aui:fieldset>
	<liferay-ui:error key="casNoSuchUserURLInvalid" message="the-cas-no-such-user-url-is-invalid" />

	<aui:input label="enabled" name='<%= "settings--" + PropsKeys.CAS_AUTH_ENABLED + "--" %>' type="checkbox" value="<%= casAuthEnabled %>" />

	<aui:input helpMessage="import-cas-users-from-ldap-help" label="import-cas-users-from-ldap" name='<%= "settings--" + PropsKeys.CAS_IMPORT_FROM_LDAP + "--" %>' type="checkbox" value="<%= casImportFromLdap %>" />

	<aui:input helpMessage="cas-auth-strict-help" label="cas-auth-strict" name='<%= "settings--" + PropsKeys.CAS_AUTH_STRICT + "--" %>' type="checkbox" value="<%= casAuthStrict %>" />

	<aui:input helpMessage="cas-auto-redirect-help" label="cas-auto-redirect" name='<%= "settings--" + PropsKeys.CAS_AUTO_REDIRECT + "--" %>' type="checkbox" value="<%= casAutoRedirect %>" />

	<aui:input cssClass="lfr-input-text-container" helpMessage="cas-auto-redirect-server-id-help" label="cas-auto-redirect-server-id" name='<%= "settings--" + PropsKeys.CAS_AUTO_REDIRECT_SERVER_ID + "--" %>' type="text" value="<%= casAutoRedirectServerId %>" />

	<aui:input cssClass="lfr-input-text-container" helpMessage="cas-no-such-user-redirect-url-help" label="no-such-user-redirect-url" name='<%= "settings--" + PropsKeys.CAS_NO_SUCH_USER_REDIRECT_URL + "--" %>' type="text" value="<%= casNoSuchUserRedirectURL %>" />

	<aui:button-row>

		<%
		PortletURL addServerURL = renderResponse.createRenderURL();

		addServerURL.setParameter("struts_action", "portal_settings/edit_cas_server");
		addServerURL.setParameter("redirect", authenticationURL);
		%>

		<aui:button href="<%= addServerURL.toString() %>" name="addButton" value="add" />
	</aui:button-row>

	<aui:input name="settings--cas.server.ids--" type="hidden" value="<%= StringUtil.merge(casServerIds) %>" />

	<c:if test="<%= casServerIds.length > 0 %>">
		<br /><br />

		<div class="results-grid cas-servers">
			<table class="taglib-search-iterator">
				<tr class="results-header">
					<th>
						<liferay-ui:message key="cas-server-id" />
					</th>
					<th>
						<liferay-ui:message key="cas-server-url" />
					</th>
					<th></th>
				</tr>

				<%
				for (int i = 0; i < casServerIds.length; i++) {
					String casServerId = casServerIds[i];

					CASServer casServer = CASManagerUtil.getCASServer(company.getCompanyId(), casServerId);

					String className = "portlet-section-body results-row";

					if (MathUtil.isEven(i)) {
						className = "portlet-section-alternate results-row alt";
					}
				%>

					<tr class="<%= className %>">
						<td>
							<%= casServerId %>
						</td>
						<td>
							<%= casServer.getServerUrl() %>
						</td>
						<td align="right">
							<div class="control">
								<portlet:renderURL var="editURL">
									<portlet:param name="struts_action" value="/portal_settings/edit_cas_server" />
									<portlet:param name="redirect" value="<%= authenticationURL %>" />
									<portlet:param name="casServerId" value="<%= String.valueOf(casServerId) %>" />
								</portlet:renderURL>

								<liferay-ui:icon
									image="edit"
									url="<%= editURL %>"
								/>

								<portlet:actionURL var="deleteURL">
									<portlet:param name="struts_action" value="/portal_settings/edit_cas_server" />
									<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.DELETE %>" />
									<portlet:param name="redirect" value="<%= authenticationURL %>" />
									<portlet:param name="casServerId" value="<%= String.valueOf(casServerId) %>" />
								</portlet:actionURL>

								<liferay-ui:icon-delete url="<%= deleteURL %>" />
							</div>
						</td>
					</tr>

				<%
				}
				%>

			</table>
		</div>
	</c:if>
</aui:fieldset>

<aui:script>
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
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

<%@ include file="/init.jsp" %>

<%
String deleteConfirmationText = (String)request.getAttribute("deleteConfirmationText");
String deleteMVCActionCommandName = (String)request.getAttribute("deleteMVCActionCommandName");
String jspPath = (String)request.getAttribute("jspPath");
String saveMVCActionCommandName = (String)request.getAttribute("saveMVCActionCommandName");
String title = (String)request.getAttribute("title");
ServletContext servletContext = (ServletContext)request.getAttribute("servletContext");
%>

<portlet:actionURL name="<%= saveMVCActionCommandName %>" var="editCompanyURL" />

<div class="sheet sheet-lg">
	<h2><%= title %></h2>

	<aui:form action="<%= editCompanyURL %>" data-senna-off="true" method="post" name="fm">
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

		<liferay-util:include page="<%= jspPath %>" servletContext="<%= servletContext %>" />

		<aui:button-row>
			<aui:button type="submit" value="save" />

			<c:if test="<%= Validator.isNotNull(deleteMVCActionCommandName) %>">
				<portlet:actionURL name="<%= deleteMVCActionCommandName %>" var="resetValuesURL" />

				<%
				String taglibOnClick = "if (confirm('" + deleteConfirmationText + "')) {submitForm(document.hrefFm, '" + resetValuesURL.toString() + "');}";
				%>

				<aui:button onClick="<%= taglibOnClick %>" value="reset-values" />
			</c:if>
		</aui:button-row>
	</aui:form>
</div>
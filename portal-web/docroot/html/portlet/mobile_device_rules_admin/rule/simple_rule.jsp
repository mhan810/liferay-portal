
<%--
/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

<%@ include file="/html/portlet/mobile_device_rules_admin/init.jsp" %>

<%
MDRRule rule = (MDRRule) request.getAttribute(WebKeys.MOBILE_DEVICE_RULES_RULE);

Set<String> oses;
int tablet = 0;

if (rule != null) {
	UnicodeProperties typeSettings = rule.getTypeSettingsProperties();

	String osString = typeSettings.get("os");
	String tabletStr = typeSettings.get("tablet");

	String[] osArray = StringUtil.split(osString);

	oses = SetUtil.fromArray(osArray);

	if (StringPool.TRUE.equals(tabletStr)) {
		tablet = 1;
	}
	else if (StringPool.FALSE.equals(tabletStr)) {
		tablet = 2;
	}
}
else {
	oses = Collections.emptySet();
}
%>

<aui:select name="os" multiple="<%= true %>">
	<aui:option label="any-os" value="" selected="<%= oses.isEmpty() %>"/>
	<%
	Set<VersionableName> knownOSes = DeviceDetectionUtil.getKnownOperatingSystems();

	for (VersionableName versionableName : knownOSes) {
	%>
		<aui:option label="<%= versionableName.getName() %>" selected="<%= oses.contains(versionableName.getName()) %>"/>
	<%
	}
	%>
</aui:select>

<aui:select name="tablet">
	<aui:option label="any" value="" selected="<%= tablet == 0 %>"/>
	<aui:option label="true" selected="<%= tablet == 1 %>"/>
	<aui:option label="false" selected="<%= tablet == 2 %>"/>
</aui:select>
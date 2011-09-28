<%@ page import="com.liferay.portal.util.PortletKeys" %>
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
String redirect = ParamUtil.getString(request, "redirect");

MDRRuleGroupInstance ruleGroupInstance = (MDRRuleGroupInstance) renderRequest.getAttribute(WebKeys.MOBILE_DEVICE_RULES_RULE_GROUP_INSTANCE);
MDRRuleGroup ruleGroup = (MDRRuleGroup) renderRequest.getAttribute(WebKeys.MOBILE_DEVICE_RULES_RULE_GROUP);
String className = ParamUtil.getString(renderRequest, "className");
long classPK = ParamUtil.getLong(renderRequest, "classPK");

boolean isAdd = Validator.isNull(ruleGroupInstance);

long ruleGroupInstanceId = 0;
long ruleGroupId = 0;

if (!isAdd) {
	ruleGroupInstanceId = ruleGroupInstance.getRuleGroupInstanceId();
	ruleGroupId = ruleGroupInstance.getRuleGroupId();
	className = ruleGroupInstance.getClassName();
	classPK = ruleGroupInstance.getClassPK();
}

Layout selLayout = null;
LayoutSet selLayoutSet = null;

if (className.equals(LayoutSet.class.getName())) {
	selLayoutSet = LayoutSetLocalServiceUtil.getLayoutSet(classPK);
	groupId = selLayoutSet.getGroupId();
}
else if (className.equals(Layout.class.getName())) {
	selLayout = LayoutLocalServiceUtil.getLayout(classPK);
	groupId = selLayout.getGroupId();
}
%>

<liferay-ui:header
	title="edit-rule-group-instance"
	backURL="<%= redirect %>"
/>

<liferay-ui:icon
	image="add"
	label="<%= true %>"
	message="select-rule-group"
	url='<%= "javascript:" + renderResponse.getNamespace() + "openSelector();" %>'
/>

<portlet:actionURL var="editRuleGroupInstanceURL">
	<portlet:param name="struts_action" value="/mobile_device_rules_admin/edit_rule_group_instance" />
    <portlet:param name="redirect" value="<%= redirect %>" />
</portlet:actionURL>

<aui:form action="<%= editRuleGroupInstanceURL %>" enctype="multipart/form-data" method="post" name="fm" onSubmit='<%= renderResponse.getNamespace() + "saveRuleGroupInstance();" %>'>
	<aui:input name="<%= Constants.CMD %>" type="hidden" />
	<aui:input name="ruleGroupId" type="hidden" value="<%= ruleGroupId %>" />
	<aui:input name="groupId" type="hidden" value="<%= groupId %>" />
	<aui:input name="className" type="hidden" value="<%= className %>" />
	<aui:input name="classPK" type="hidden" value="<%= classPK %>" />

	<liferay-ui:error exception="<%= NoSuchRuleGroupException.class %>" message="rule-group-does-not-exist"/>

	<div id="<%= renderResponse.getNamespace() %>editor">
		<c:choose>
			<c:when test="<%= isAdd %>">
				<h3><%= LanguageUtil.get(pageContext, "no-rule-group-selected") %></h3>
			</c:when>
			<c:otherwise>
				<c:import url="/html/portlet/mobile_device_rules_admin/device_rule_group_instance_editor.jsp" />
			</c:otherwise>
		</c:choose>
	</div>
</aui:form>

<portlet:resourceURL var="editorURL">
    <portlet:param name="struts_action" value="/mobile_device_rules_admin/edit_rule_group_instance_editor" />
</portlet:resourceURL>

<aui:script>
	Liferay.provide(
		window,
        '<portlet:namespace />openSelector',
        function(groupId) {
            var config = {
                chooseCallback: '<portlet:namespace />finishCall',
                dialog: {
                    stack: false,
                    width: 820,
                    align: Liferay.Util.Window.ALIGN_CENTER
                }
            };

            var mdrURL = Liferay.PortletURL.createRenderURL();

            mdrURL.setEscapeXML(false);

            mdrURL.setParameter('struts_action', '/mobile_device_rules_admin/view');
            mdrURL.setParameter("groupId", groupId);
			mdrURL.setParameter('chooseCallback', config.chooseCallback);
			mdrURL.setParameter("groupId", '<%= groupId %>');

            mdrURL.setPortletId(<%= PortletKeys.MOBILE_DEVICE_SITE_ADMIN %>);
            mdrURL.setWindowState('pop_up');

            config.uri = mdrURL.toString();

            Liferay.Util.openWindow(config);
        },
        ['liferay-portlet-url']
    );

	Liferay.provide(
		window,
		'<portlet:namespace />finishCall',
        function(ruleGroupId, dialog) {
			var A = AUI();

			document.<portlet:namespace />fm.<portlet:namespace />ruleGroupId.value = ruleGroupId;

            if (dialog) {
                dialog.close();
            }

			<portlet:namespace />changeDisplay();
        },
		['aui-base']
	);

	Liferay.provide(
		window,
        '<portlet:namespace />changeDisplay',
        function() {
            var A = AUI();

            var ruleGroupId = document.<portlet:namespace />fm.<portlet:namespace />ruleGroupId.value;

            A.io(
                '<%= editorURL.toString() %>',
                {
                    data: {
						className: '<%= className %>',
						classPK: '<%= classPK %>',
						ruleGroupId: ruleGroupId,
						redirect: '<%= redirect %>'
                    },
                    method: AUI.defaults.io.method,
                    on: {
                        complete: <portlet:namespace />displayEditor
                    }
                }
            )
        },
		['aui-base']
    );

    function <portlet:namespace />displayEditor(id, obj) {
        document.getElementById('<portlet:namespace />editor').innerHTML=obj.responseText;
    };

	function <portlet:namespace />saveRuleGroupInstance() {
		var ruleGroupInstanceId = document.<portlet:namespace />fm.<portlet:namespace />ruleGroupInstanceId.value;

		if (ruleGroupInstanceId != null && ruleGroupInstanceId > 0) {
			document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = "<%= Constants.UPDATE %>";
		}
		else {
			document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = "<%= Constants.ADD %>";
		}

		submitForm(document.<portlet:namespace />fm);
	};
</aui:script>
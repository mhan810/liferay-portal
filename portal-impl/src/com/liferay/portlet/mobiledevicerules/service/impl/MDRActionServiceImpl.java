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

package com.liferay.portlet.mobiledevicerules.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.mobiledevicerules.model.MDRAction;
import com.liferay.portlet.mobiledevicerules.permission.MDRRuleGroupPermissionUtil;
import com.liferay.portlet.mobiledevicerules.service.base.MDRActionServiceBaseImpl;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 * @author Edward C. Han
 */
public class MDRActionServiceImpl extends MDRActionServiceBaseImpl {

	public MDRAction addMDRAction(
			long groupId, String className, long classPK, long ruleGroupId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			String type, UnicodeProperties typeSettingsProperties,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		MDRRuleGroupPermissionUtil.check(
			getPermissionChecker(), groupId, ruleGroupId, ActionKeys.UPDATE);

		return mdrActionLocalService.addAction(
			groupId, className, classPK, ruleGroupId, nameMap, descriptionMap,
			type, typeSettingsProperties, serviceContext);
	}

	public MDRAction addMDRAction(
			long groupId, String className, long classPK, long ruleGroupId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			String type, String typeSettings, ServiceContext serviceContext)
		throws PortalException, SystemException {

		MDRRuleGroupPermissionUtil.check(
			getPermissionChecker(), groupId, ruleGroupId, ActionKeys.UPDATE);

		return mdrActionLocalService.addAction(
			groupId, className, classPK, ruleGroupId, nameMap, descriptionMap,
			type, typeSettings, serviceContext);
	}

	public void deleteAction(MDRAction action)
		throws PortalException, SystemException {

		MDRRuleGroupPermissionUtil.check(
			getPermissionChecker(), action.getGroupId(),
			action.getRuleGroupId(), ActionKeys.UPDATE);

		mdrActionLocalService.deleteAction(action);
	}

	public void deleteActions(String className, long classPK)
		throws PortalException, SystemException {

		Collection<MDRAction> actions = mdrActionLocalService.getActions(
			className, classPK);

		for (MDRAction action : actions) {
			MDRRuleGroupPermissionUtil.check(
				getPermissionChecker(), action.getGroupId(),
				action.getRuleGroupId(), ActionKeys.UPDATE);

			mdrActionLocalService.deleteAction(action);
		}
	}

	public MDRAction fetchAction(long actionId)
		throws PortalException, SystemException {

		MDRAction action = mdrActionLocalService.fetchAction(actionId);

		if (action != null) {
			MDRRuleGroupPermissionUtil.check(
				getPermissionChecker(), action.getGroupId(),
				action.getRuleGroupId(), ActionKeys.VIEW);
		}

		return action;
	}

	public MDRAction getAction(long actionId)
		throws PortalException, SystemException {

		MDRAction action = mdrActionLocalService.getMDRAction(actionId);

		MDRRuleGroupPermissionUtil.check(
			getPermissionChecker(), action.getGroupId(),
			action.getRuleGroupId(), ActionKeys.VIEW);

		return action;
	}

	public MDRAction updateAction(
			long actionId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, String type,
			UnicodeProperties typeSettingsProperties,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		MDRAction action = mdrActionLocalService.getMDRAction(actionId);

		MDRRuleGroupPermissionUtil.check(
			getPermissionChecker(), action.getGroupId(),
			action.getRuleGroupId(), ActionKeys.UPDATE);

		return mdrActionLocalService.updateAction(
			actionId, nameMap, descriptionMap, type, typeSettingsProperties,
			serviceContext);
	}

	public MDRAction updateMDRAction(
			long actionId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, String type,
			String typeSettings, ServiceContext serviceContext)
		throws PortalException, SystemException {

		MDRAction action = mdrActionLocalService.getMDRAction(actionId);

		MDRRuleGroupPermissionUtil.check(
			getPermissionChecker(), action.getGroupId(),
			action.getRuleGroupId(), ActionKeys.UPDATE);

		return mdrActionLocalService.updateAction(
			actionId, nameMap, descriptionMap, type, typeSettings,
			serviceContext);
	}

}
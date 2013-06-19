/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.systemevents.SystemEventHierarchyEntry;
import com.liferay.portal.kernel.systemevents.SystemEventHierarchyEntryThreadLocal;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.SystemEvent;
import com.liferay.portal.model.SystemEventConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.service.base.SystemEventLocalServiceBaseImpl;
import com.liferay.portal.util.PortalUtil;

import java.util.Date;
import java.util.List;

/**
 * @author Zsolt Berentey
 */
public class SystemEventLocalServiceImpl
	extends SystemEventLocalServiceBaseImpl {

	@Override
	public void addSystemEvent(
			long userId, long groupId, long companyId, long classNameId,
			long classPK, String classUuid, long referrerClassNameId, int type,
			String extraData)
		throws PortalException, SystemException {

		SystemEventHierarchyEntry systemEventHierarchyEntry =
			SystemEventHierarchyEntryThreadLocal.peek();

		long action = SystemEventConstants.ACTION_NONE;

		if (systemEventHierarchyEntry != null) {
			action = systemEventHierarchyEntry.getAction();

			if ((action == SystemEventConstants.ACTION_SKIP) &&
				!systemEventHierarchyEntry.isCurrentAsset(
					classNameId, classPK)) {

				return;
			}
		}

		if (userId == 0) {
			userId = PrincipalThreadLocal.getUserId();
		}

		String userName = StringPool.BLANK;

		if ((companyId == 0) && (userId > 0)) {
			User user = userPersistence.findByPrimaryKey(userId);

			companyId = user.getCompanyId();
			userName = user.getFullName();
		}

		if (companyId == 0) {
			if (groupId > 0) {
				Group group = groupPersistence.findByPrimaryKey(groupId);

				companyId = group.getCompanyId();
			}
			else {
				throw new IllegalArgumentException(
					"Unable to determine company");
			}
		}

		Company company = companyLocalService.getCompany(companyId);

		Group companyGroup = company.getGroup();

		if (companyGroup.getGroupId() == groupId) {
			groupId = 0;
		}

		long systemEventId = 0;

		if ((systemEventHierarchyEntry != null) &&
			systemEventHierarchyEntry.isCurrentAsset(classNameId, classPK)) {

			systemEventId = systemEventHierarchyEntry.getSystemEventId();
		}
		else {
			systemEventId = counterLocalService.increment();
		}

		SystemEvent systemEvent = systemEventPersistence.create(systemEventId);

		systemEvent.setGroupId(groupId);
		systemEvent.setCompanyId(companyId);
		systemEvent.setUserId(userId);
		systemEvent.setUserName(userName);
		systemEvent.setCreateDate(new Date());
		systemEvent.setClassNameId(classNameId);
		systemEvent.setClassPK(classPK);
		systemEvent.setClassUuid(classUuid);
		systemEvent.setReferrerClassNameId(referrerClassNameId);
		systemEvent.setType(type);
		systemEvent.setExtraData(extraData);

		if ((action == SystemEventConstants.ACTION_GROUP) ||
			(action == SystemEventConstants.ACTION_HIERARCHY)) {

			systemEvent.setEventSetId(
				systemEventHierarchyEntry.getEventSetId());
		}
		else {
			systemEvent.setEventSetId(counterLocalService.increment());
		}

		if (action == SystemEventConstants.ACTION_HIERARCHY) {
			if (systemEventHierarchyEntry.isCurrentAsset(
					classNameId, classPK)) {

				systemEvent.setParentSystemEventId(
					systemEventHierarchyEntry.getParentSystemEventId());
			}
			else {
				systemEvent.setParentSystemEventId(
					systemEventHierarchyEntry.getSystemEventId());
			}
		}

		systemEventPersistence.update(systemEvent);
	}

	@Override
	public void addSystemEvent(
			long userId, long groupId, long companyId, String className,
			long classPK, String classUuid, String referrerClassName, int type,
			String extraData)
		throws PortalException, SystemException {

		long referrerClassNameId = 0;

		if (Validator.isNotNull(referrerClassName)) {
			referrerClassNameId = PortalUtil.getClassNameId(referrerClassName);
		}

		addSystemEvent(
			userId, groupId, companyId, PortalUtil.getClassNameId(className),
			classPK, classUuid, referrerClassNameId, type, extraData);
	}

	@Override
	public void deleteSystemEvents(long groupId) throws SystemException {
		systemEventPersistence.removeByGroupId(groupId);
	}

	@Override
	public SystemEvent fetchSystemEvent(
			long groupId, long classNameId, long classPK, int type)
		throws SystemException {

		return systemEventPersistence.fetchByG_C_C_T_First(
			groupId, classNameId, classPK, type, null);
	}

	@Override
	public List<SystemEvent> getSystemEvents(
			long groupId, long classNameId, long classPK)
		throws SystemException {

		return systemEventPersistence.findByG_C_C(
			groupId, classNameId, classPK);
	}

	@Override
	public List<SystemEvent> getSystemEvents(
			long groupId, long classNameId, long classPK, int type)
		throws SystemException {

		return systemEventPersistence.findByG_C_C_T(
			groupId, classNameId, classPK, type);
	}

}
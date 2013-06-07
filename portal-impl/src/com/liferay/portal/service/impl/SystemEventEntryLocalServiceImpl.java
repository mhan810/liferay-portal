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
import com.liferay.portal.model.Group;
import com.liferay.portal.model.SystemEventEntry;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.service.base.SystemEventEntryLocalServiceBaseImpl;

import java.util.List;

/**
 * @author Zsolt Berentey
 */
public class SystemEventEntryLocalServiceImpl
	extends SystemEventEntryLocalServiceBaseImpl {

	@Override
	public void addEvent(
			long groupId, long userId, int eventType, long classNameId,
			long classPK, String classUuid)
		throws PortalException, SystemException {

		long companyId = 0;
		String userName;

		if (userId == 0) {
			userId = PrincipalThreadLocal.getUserId();
		}

		if (userId > 0) {
			User user = userPersistence.findByPrimaryKey(userId);

			companyId = user.getCompanyId();
			userName = user.getFullName();
		}
		else {
			userName = "system";
		}

		if (companyId == 0) {
			if (groupId > 0) {
				Group group = groupLocalService.getGroup(groupId);

				companyId = group.getCompanyId();
			}
			else {
				throw new IllegalArgumentException();
			}
		}

		long systemEventId = counterLocalService.increment();

		SystemEventEntry entry = systemEventEntryPersistence.create(
			systemEventId);

		entry.setGroupId(groupId);
		entry.setCompanyId(companyId);
		entry.setUserId(userId);
		entry.setUserName(userName);
		entry.setEventType(eventType);
		entry.setClassNameId(classNameId);
		entry.setClassPK(classPK);
		entry.setClassUuid(classUuid);

		systemEventEntryPersistence.update(entry);
	}

	@Override
	public void deleteEvents(long groupId) throws SystemException {
		systemEventEntryPersistence.removeByGroupId(groupId);
	}

	@Override
	public SystemEventEntry fetchEvent(
			long groupId, int eventType, long classNameId, long classPK)
		throws SystemException {

		return systemEventEntryPersistence.fetchByG_E_C_C_First(
			groupId, eventType, classNameId, classPK, null);
	}

	@Override
	public List<SystemEventEntry> findEvents(
			long groupId, int eventType, long classNameId, long classPK)
		throws SystemException {

		return systemEventEntryPersistence.findByG_E_C_C(
			groupId, eventType, classNameId, classPK);
	}

	@Override
	public List<SystemEventEntry> findEvents(
			long groupId, long classNameId, long classPK)
		throws SystemException {

		return systemEventEntryPersistence.findByG_C_C(
			groupId, classNameId, classPK);
	}

}
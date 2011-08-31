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

package com.liferay.portlet.social.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.social.NoSuchRequestException;
import com.liferay.portlet.social.RequestUserIdException;
import com.liferay.portlet.social.model.SocialRequest;
import com.liferay.portlet.social.model.SocialRequestConstants;
import com.liferay.portlet.social.service.base.SocialRequestLocalServiceBaseImpl;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
public class SocialRequestLocalServiceImpl
	extends SocialRequestLocalServiceBaseImpl {

	public SocialRequest addRequest(
			long userId, long groupId, String className, long classPK,
			int type, String extraData, long receiverUserId)
		throws PortalException, SystemException {

		User user = userPersistence.findByPrimaryKey(userId);
		long classNameId = PortalUtil.getClassNameId(className);
		User receiverUser = userPersistence.findByPrimaryKey(receiverUserId);
		long now = System.currentTimeMillis();

		if ((userId == receiverUserId) || (user.isDefaultUser()) ||
			(receiverUser.isDefaultUser()) ||
			(user.getCompanyId() != receiverUser.getCompanyId())) {

			throw new RequestUserIdException();
		}

		try {
			socialRequestPersistence.removeByU_C_C_T_R(
				userId, classNameId, classPK, type, receiverUserId);
		}
		catch (NoSuchRequestException nsre) {
		}

		long requestId = counterLocalService.increment(
			SocialRequest.class.getName());

		SocialRequest request = socialRequestPersistence.create(requestId);

		request.setGroupId(groupId);
		request.setCompanyId(user.getCompanyId());
		request.setUserId(user.getUserId());
		request.setCreateDate(now);
		request.setModifiedDate(now);
		request.setClassNameId(classNameId);
		request.setClassPK(classPK);
		request.setType(type);
		request.setExtraData(extraData);
		request.setReceiverUserId(receiverUserId);
		request.setStatus(SocialRequestConstants.STATUS_PENDING);

		socialRequestPersistence.update(request, false);

		return request;
	}

	public void deleteReceiverUserRequests(long receiverUserId)
		throws SystemException {

		List<SocialRequest> requests =
			socialRequestPersistence.findByReceiverUserId(receiverUserId);

		for (SocialRequest request : requests) {
			deleteRequest(request);
		}
	}

	public void deleteRequest(long requestId)
		throws PortalException, SystemException {

		SocialRequest request = socialRequestPersistence.findByPrimaryKey(
			requestId);

		deleteRequest(request);
	}

	public void deleteRequest(SocialRequest request) throws SystemException {
		socialRequestPersistence.remove(request);
	}

	public void deleteUserRequests(long userId) throws SystemException {
		List<SocialRequest> requests = socialRequestPersistence.findByUserId(
			userId);

		for (SocialRequest request : requests) {
			deleteRequest(request);
		}
	}

	public List<SocialRequest> getReceiverUserRequests(
			long receiverUserId, int start, int end)
		throws SystemException {

		return socialRequestPersistence.findByReceiverUserId(
			receiverUserId, start, end);
	}

	public List<SocialRequest> getReceiverUserRequests(
			long receiverUserId, int status, int start, int end)
		throws SystemException {

		return socialRequestPersistence.findByR_S(
			receiverUserId, status, start, end);
	}

	public int getReceiverUserRequestsCount(long receiverUserId)
		throws SystemException {

		return socialRequestPersistence.countByReceiverUserId(receiverUserId);
	}

	public int getReceiverUserRequestsCount(long receiverUserId, int status)
		throws SystemException {

		return socialRequestPersistence.countByR_S(receiverUserId, status);
	}

	public List<SocialRequest> getUserRequests(long userId, int start, int end)
		throws SystemException {

		return socialRequestPersistence.findByUserId(userId, start, end);
	}

	public List<SocialRequest> getUserRequests(
			long userId, int status, int start, int end)
		throws SystemException {

		return socialRequestPersistence.findByU_S(userId, status, start, end);
	}

	public int getUserRequestsCount(long userId) throws SystemException {
		return socialRequestPersistence.countByUserId(userId);
	}

	public int getUserRequestsCount(long userId, int status)
		throws SystemException {

		return socialRequestPersistence.countByU_S(userId, status);
	}

	public SocialRequest getRequest(long requestId)
		throws PortalException, SystemException {

		return socialRequestPersistence.findByPrimaryKey(requestId);
	}

	public boolean hasRequest(
			long userId, String className, long classPK, int type, int status)
		throws SystemException {

		long classNameId = PortalUtil.getClassNameId(className);

		if (socialRequestPersistence.countByU_C_C_T_S(
				userId, classNameId, classPK, type, status) <= 0) {

			return false;
		}
		else {
			return true;
		}
	}

	public boolean hasRequest(
			long userId, String className, long classPK, int type,
			long receiverUserId, int status)
		throws SystemException {

		long classNameId = PortalUtil.getClassNameId(className);

		SocialRequest socialRequest =
			socialRequestPersistence.fetchByU_C_C_T_R(
				userId, classNameId, classPK, type, receiverUserId);

		if ((socialRequest == null) || (socialRequest.getStatus() != status)) {
			return false;
		}
		else {
			return true;
		}
	}

	public SocialRequest updateRequest(
			long requestId, int status, ThemeDisplay themeDisplay)
		throws PortalException, SystemException {

		SocialRequest request = socialRequestPersistence.findByPrimaryKey(
			requestId);

		request.setModifiedDate(System.currentTimeMillis());
		request.setStatus(status);

		socialRequestPersistence.update(request, false);

		if (status == SocialRequestConstants.STATUS_CONFIRM) {
			socialRequestInterpreterLocalService.processConfirmation(
				request, themeDisplay);
		}
		else if (status == SocialRequestConstants.STATUS_IGNORE) {
			socialRequestInterpreterLocalService.processRejection(
				request, themeDisplay);
		}

		return request;
	}

}
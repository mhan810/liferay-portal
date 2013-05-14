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

package com.liferay.portlet.messageboards.social;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityConstants;
import com.liferay.portlet.social.model.SocialActivitySet;
import com.liferay.portlet.social.service.SocialActivitySetLocalServiceUtil;
import com.liferay.portlet.social.service.permission.SocialActivitySetPermissionUtil;

/**
 * @author Zsolt Berentey
 */
public class MBThreadActivityInterpreter extends BaseSocialActivityInterpreter {

	@Override
	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	@Override
	protected Object doGetEntity(
			SocialActivity activity, ServiceContext serviceContext)
		throws SystemException {

		MBThread thread = MBThreadLocalServiceUtil.fetchThread(
			activity.getClassPK());

		if (thread != null) {
			return MBMessageLocalServiceUtil.fetchMBMessage(
				thread.getRootMessageId());
		}

		return null;
	}

	@Override
	protected String getBody(
			SocialActivity activity, ServiceContext serviceContext)
		throws Exception {

		MBMessage message = (MBMessage)getEntity();

		if ((message == null) || (message.getCategoryId() <= 0)) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(serviceContext.getPortalURL());
		sb.append(serviceContext.getPathMain());
		sb.append("/message_boards/find_category?mbCategoryId=");
		sb.append(message.getCategoryId());

		String categoryLink = sb.toString();

		return wrapLink(categoryLink, "go-to-category", serviceContext);
	}

	@Override
	protected String getPath(
			SocialActivity activity, ServiceContext serviceContext)
		throws Exception {

		MBMessage message = (MBMessage)getEntity();

		if (message != null) {
			return "/message_boards/find_message?messageId=" +
				message.getMessageId();
		}

		return null;
	}

	@Override
	protected Object[] getTitleArguments(
		String groupName, SocialActivity activity, String link, String title,
		ServiceContext serviceContext) {

		String userName = getUserName(activity.getUserId(), serviceContext);
		String receiverUserName = StringPool.BLANK;

		if (activity.getReceiverUserId() > 0) {
			receiverUserName = getUserName(
				activity.getReceiverUserId(), serviceContext);
		}

		return new Object[] {
			groupName, userName, receiverUserName, wrapLink(link, title)
		};
	}

	@Override
	protected String getTitlePattern(
		String groupName, SocialActivity activity) {

		int activityType = activity.getType();

		if (activityType == SocialActivityConstants.TYPE_MOVE_TO_TRASH) {
			if (Validator.isNull(groupName)) {
				return "activity-message-boards-thread-move-to-trash";
			}
			else {
				return "activity-message-boards-thread-move-to-trash-in";
			}
		}
		else if (activityType ==
					SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) {

			if (Validator.isNull(groupName)) {
				return "activity-message-boards-thread-restore-from-trash";
			}
			else {
				return "activity-message-boards-thread-restore-from-trash-in";
			}
		}
		else if (activityType == SocialActivityConstants.TYPE_DELETE) {
			if (Validator.isNull(groupName)) {
				return "activity-message-boards-thread-delete";
			}
			else {
				return "activity-message-boards-thread-delete-in";
			}
		}

		return null;
	}

	@Override
	protected boolean hasPermissions(
			PermissionChecker permissionChecker, SocialActivity activity,
			String actionId, ServiceContext serviceContext)
		throws Exception {

		SocialActivitySet activitySet =
			SocialActivitySetLocalServiceUtil.fetchAssetActivitySet(
				PortalUtil.getClassNameId(MBMessage.class),
				GetterUtil.getLong(
					activity.getExtraDataValue("rootMessageId")));

		if (activitySet == null) {
			activitySet =
				SocialActivitySetLocalServiceUtil.fetchAssetActivitySet(
					PortalUtil.getClassNameId(MBThread.class),
					activity.getClassPK());

			if (activitySet == null) {
				return false;
			}
		}

		return SocialActivitySetPermissionUtil.contains(
			permissionChecker, activitySet, actionId);
	}

	private static final String[] _CLASS_NAMES = {MBThread.class.getName()};

}
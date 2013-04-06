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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.StagedGroupedModel;
import com.liferay.portal.model.StagedModel;

import java.io.Serializable;

/**
 * @author Mate Thurzo
 * @author Daniel Kocsis
 */
public class StagedModelPathUtil {

	public static String getPath(
		PortletDataContext portletDataContext, String className, long classPK) {

		return getPath(portletDataContext, className, classPK, null);
	}

	public static String getPath(
		PortletDataContext portletDataContext, String className, long classPK,
		String dependentFileName) {

		return getPath(
			portletDataContext.getSourceGroupId(), _GROUPS_PATH_PREFIX,
			className, classPK, dependentFileName);
	}

	public static String getPath(StagedModel stagedModel) {
		return getPath(stagedModel, null);
	}

	public static String getPath(
		StagedModel stagedModel, String dependentFileName) {

		if (stagedModel instanceof StagedGroupedModel) {
			StagedGroupedModel stagedGroupedModel =
				(StagedGroupedModel)stagedModel;

			return getPath(
				stagedGroupedModel.getGroupId(), _GROUPS_PATH_PREFIX,
				stagedModel.getModelClassName(), stagedModel.getPrimaryKeyObj(),
				dependentFileName);
		}
		else {
			return getPath(
				stagedModel.getCompanyId(), _COMPANY_PATH_PREFIX,
				stagedModel.getModelClassName(), stagedModel.getPrimaryKeyObj(),
				dependentFileName);
		}
	}

	protected static String getPath(
			long companyId, String pathPrefix, String className,
			Serializable primaryKeyObj,
		String dependentFileName) {

		StringBundler sb = new StringBundler(11);

		sb.append(StringPool.FORWARD_SLASH);
		sb.append(pathPrefix);
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(companyId);
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(className);
		sb.append(StringPool.FORWARD_SLASH);
		sb.append(primaryKeyObj.toString());

		if (dependentFileName == null) {
			sb.append(".xml");
		}
		else {
			sb.append(StringPool.FORWARD_SLASH);
			sb.append(dependentFileName);
		}

		return sb.toString();
	}

	private static final String _COMPANY_PATH_PREFIX = "company";
	private static final String _GROUPS_PATH_PREFIX = "groups";

}
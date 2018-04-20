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

package com.liferay.portal.search.internal.test;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.search.SearchEngineHelperUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Wade Cao
 * @author Eric Yan
 */
public class JournalArticleFieldsFixture {

	public JournalArticleFieldsFixture(
		ResourcePermissionLocalService resourcePermissionLocalService,
		RoleLocalService roleLocalService) {

		_resourcePermissionLocalService = resourcePermissionLocalService;
		_roleLocalService = roleLocalService;
	}

	public long getRoleId(long companyId, String name) throws PortalException {
		Role role = _roleLocalService.getRole(companyId, name);

		return role.getRoleId();
	}

	public boolean isSearchEngineElasticsearch() {
		SearchEngine searchEngine = SearchEngineHelperUtil.getSearchEngine(
			SearchEngineHelperUtil.getDefaultSearchEngineId());

		String vendor = searchEngine.getVendor();

		return vendor.equals("Elasticsearch");
	}

	public boolean isSearchEngineSolr() {
		SearchEngine searchEngine = SearchEngineHelperUtil.getSearchEngine(
			SearchEngineHelperUtil.getDefaultSearchEngineId());

		String vendor = searchEngine.getVendor();

		return vendor.equals("Solr");
	}

	public void populateRoleIdFields(
			long companyId, String className, long classPK, long groupId,
			String viewActionId, Map<String, String> fieldValues)
		throws PortalException {

		if (Validator.isNull(viewActionId)) {
			viewActionId = ActionKeys.VIEW;
		}

		List<Role> roles = _resourcePermissionLocalService.getRoles(
			companyId, className, ResourceConstants.SCOPE_INDIVIDUAL,
			Long.toString(classPK), viewActionId);

		List<String> groupRoleIds = new ArrayList<>();
		List<Long> roleIds = new ArrayList<>();

		for (Role role : roles) {
			if ((role.getType() == RoleConstants.TYPE_ORGANIZATION) ||
				(role.getType() == RoleConstants.TYPE_SITE)) {

				groupRoleIds.add(groupId + StringPool.DASH + role.getRoleId());
			}
			else {
				roleIds.add(role.getRoleId());
			}
		}

		if (groupRoleIds.size() == 1) {
			fieldValues.put(Field.GROUP_ROLE_ID, groupRoleIds.get(0));
		}
		else if (groupRoleIds.size() > 1) {
			fieldValues.put(Field.GROUP_ROLE_ID, groupRoleIds.toString());
		}

		if (roleIds.size() == 1) {
			fieldValues.put(Field.ROLE_ID, String.valueOf(roleIds.get(0)));
		}
		else if (roleIds.size() > 1) {
			fieldValues.put(Field.ROLE_ID, roleIds.toString());
		}
	}

	public void populateUID(
		long id, String modelClassName, Map<String, String> fieldValues) {

		String uid = modelClassName + "_PORTLET_" + id;

		fieldValues.put(Field.UID, uid);
	}

	public void postProcessDocument(Document document) {
		if (isSearchEngineSolr()) {
			document.remove("score");
		}
	}

	private final ResourcePermissionLocalService
		_resourcePermissionLocalService;
	private final RoleLocalService _roleLocalService;

}
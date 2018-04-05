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

import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalArticleDisplay;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.search.test.journal.util.JournalArticleContent;
import com.liferay.portal.service.test.ServiceTestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Wade Cao
 * @author Eric Yan
 */
public class JournalArticleFixture {

	public JournalArticleFixture(
		JournalArticleLocalService journalArticleLocalService,
		List<Group> groups, List<User> users) {

		_journalArticleLocalService = journalArticleLocalService;

		_groups = groups;
		_users = users;
	}

	public Group addGroup() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.add(group);

		return group;
	}

	public JournalArticle addJournalArticle(
			String title, Locale locale, String content,
			ServiceContext serviceContext)
		throws Exception {

		Map<Locale, String> titleMap = new HashMap<>();

		titleMap.put(locale, title);

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		if (!locale.equals(defaultLocale)) {
			titleMap.put(defaultLocale, title);
		}

		JournalArticleContent journalArticleContent =
			new JournalArticleContent() {
				{
					name = "content";
					defaultLocale = LocaleUtil.US;

					put(locale, content);
				}
			};

		String ddmStructureKey = "BASIC-WEB-CONTENT";
		String ddmTemplateKey = "BASIC-WEB-CONTENT";

		return _journalArticleLocalService.addArticle(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, titleMap, null,
			journalArticleContent.getContentString(), ddmStructureKey,
			ddmTemplateKey, serviceContext);
	}

	public User addUser() throws Exception {
		User user = UserTestUtil.addUser();

		_users.add(user);

		return user;
	}

	public String getActualContent(
			JournalArticle journalArticle, String languageId)
		throws Exception {

		JournalArticleDisplay journalArticleDisplay =
			_journalArticleLocalService.getArticleDisplay(
				journalArticle.getGroupId(), journalArticle.getArticleId(),
				Constants.VIEW, languageId,
				getServiceContext().getThemeDisplay());

		return journalArticleDisplay.getContent();
	}

	public ServiceContext getServiceContext() throws PortalException {
		return ServiceContextTestUtil.getServiceContext(
			_group.getGroupId(), getUserId());
	}

	public void setGroup(Group group) {
		_group = group;
	}

	public void setUp() throws Exception {
		ServiceTestUtil.setUser(TestPropsValues.getUser());

		CompanyThreadLocal.setCompanyId(TestPropsValues.getCompanyId());
	}

	public void setUser(User user) {
		_user = user;
	}

	protected long getUserId() throws PortalException {
		if (_user != null) {
			return _user.getUserId();
		}

		return TestPropsValues.getUserId();
	}

	private Group _group;
	private final List<Group> _groups;
	private final JournalArticleLocalService _journalArticleLocalService;
	private User _user;
	private final List<User> _users;

}
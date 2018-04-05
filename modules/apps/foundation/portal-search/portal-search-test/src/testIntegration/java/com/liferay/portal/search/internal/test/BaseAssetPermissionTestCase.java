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

import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.test.rule.Inject;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

/**
 * @author Wade Cao
 * @author Eric Yan
 */
public abstract class BaseAssetPermissionTestCase {

	@Before
	public void setUp() throws Exception {
		journalArticleFixture = createJournalArticleFixture();

		journalArticleFixture.setUp();

		journalArticleFieldsFixture = createJournalArticleFieldsFixture();
		journalArticleSearchFixture = createJournalArticleSearchFixture();
	}

	protected JournalArticleFieldsFixture createJournalArticleFieldsFixture() {
		return new JournalArticleFieldsFixture(
			_resourcePermissionLocalService, _roleLocalService);
	}

	protected JournalArticleFixture createJournalArticleFixture() {
		return new JournalArticleFixture(
			_journalArticleLocalService, _groups, _users);
	}

	protected JournalArticleSearchFixture createJournalArticleSearchFixture() {
		return new JournalArticleSearchFixture(_indexerRegistry);
	}

	protected void setGroup(Group group) {
		journalArticleFixture.setGroup(group);
		journalArticleSearchFixture.setGroup(group);
	}

	protected void setIndexerClass(Class<?> clazz) {
		journalArticleSearchFixture.setIndexerClass(clazz);
	}

	protected void setUser(User user) {
		journalArticleFixture.setUser(user);
		journalArticleSearchFixture.setUser(user);
	}

	protected JournalArticleFieldsFixture journalArticleFieldsFixture;
	protected JournalArticleFixture journalArticleFixture;
	protected JournalArticleSearchFixture journalArticleSearchFixture;

	@DeleteAfterTestRun
	private final List<Group> _groups = new ArrayList<>(1);

	@Inject
	private IndexerRegistry _indexerRegistry;

	@Inject
	private JournalArticleLocalService _journalArticleLocalService;

	@Inject
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Inject
	private RoleLocalService _roleLocalService;

	@DeleteAfterTestRun
	private final List<User> _users = new ArrayList<>(1);

}
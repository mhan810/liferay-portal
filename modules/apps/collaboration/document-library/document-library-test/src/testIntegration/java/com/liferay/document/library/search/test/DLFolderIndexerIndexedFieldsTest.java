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

package com.liferay.document.library.search.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.test.util.FieldValuesAssert;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;

import java.text.Format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Wade Cao
 * @author Eric Yan
 */
@RunWith(Arquillian.class)
@Sync
public class DLFolderIndexerIndexedFieldsTest extends BaseDLIndexerTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerTestRule.INSTANCE,
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		super.setUp();

		setGroup(dlFixture.addGroup());
		setIndexerClass(DLFolder.class);
		setUser(dlFixture.addUser());
	}

	@Test
	public void testIndexedFields() throws Exception {
		ServiceContext serviceContext = dlFixture.getServiceContext();

		DLFolder parentFolder = dlFixture.addFolder(
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString(_FOLDER_NAME_MAX_LENGTH),
			RandomTestUtil.randomString(), serviceContext);

		String folderName = RandomTestUtil.randomString();

		DLFolder childFolder = dlFixture.addFolder(
			parentFolder.getFolderId(), folderName,
			RandomTestUtil.randomString(), serviceContext);

		Document document = dlSearchFixture.searchOnlyOne(
			folderName, LocaleUtil.US);

		dlFieldsFixture.postProcessDocument(document);

		Map<String, String> map = new HashMap<>();

		populateExpectedFieldValues(childFolder, map);

		FieldValuesAssert.assertFieldValues(map, document, folderName);
	}

	protected void populateDateFields(
		DLFolder dlFolder, Map<String, String> map) {

		Format dateFormat = FastDateFormatFactoryUtil.getSimpleDateFormat(
			PropsUtil.get(PropsKeys.INDEX_DATE_FORMAT_PATTERN));

		Date expirationDate = new Date(Long.MAX_VALUE);

		if (dlFieldsFixture.isSearchEngineElasticsearch()) {
			dateFormat = FastDateFormatFactoryUtil.getSimpleDateFormat(
				"yyyyMMddHHmmss");

			map.put(Field.EXPIRATION_DATE, "99950812133000");
		}
		else {
			map.put(Field.EXPIRATION_DATE, dateFormat.format(expirationDate));
		}

		map.put(Field.CREATE_DATE, dateFormat.format(dlFolder.getCreateDate()));
		map.put(
			Field.CREATE_DATE.concat("_sortable"),
			String.valueOf(dlFolder.getCreateDate().getTime()));
		map.put(
			Field.EXPIRATION_DATE.concat("_sortable"),
			String.valueOf(expirationDate.getTime()));
		map.put(
			Field.MODIFIED_DATE, dateFormat.format(dlFolder.getModifiedDate()));
		map.put(
			Field.MODIFIED_DATE.concat("_sortable"),
			String.valueOf(dlFolder.getModifiedDate().getTime()));
		map.put(
			Field.PUBLISH_DATE, dateFormat.format(dlFolder.getCreateDate()));
		map.put(
			Field.PUBLISH_DATE.concat("_sortable"),
			String.valueOf(dlFolder.getCreateDate().getTime()));
	}

	protected Map<String, String> populateExpectedFieldValues(
			DLFolder dlFolder, Map<String, String> map)
		throws PortalException {

		map.put(Field.COMPANY_ID, String.valueOf(dlFolder.getCompanyId()));
		map.put(Field.DESCRIPTION, dlFolder.getDescription());
		map.put(Field.ENTRY_CLASS_NAME, dlFolder.getModelClassName());
		map.put(Field.ENTRY_CLASS_PK, String.valueOf(dlFolder.getFolderId()));
		map.put(Field.FOLDER_ID, String.valueOf(dlFolder.getParentFolderId()));
		map.put(Field.GROUP_ID, String.valueOf(dlFolder.getGroupId()));
		map.put(Field.HIDDEN, String.valueOf(dlFolder.isHidden()));
		map.put(Field.PRIORITY, "0.0");

		if (dlFieldsFixture.isSearchEngineSolr()) {
			map.put(Field.PRIORITY.concat("_sortable"), "0.0");
		}

		map.put(Field.SCOPE_GROUP_ID, String.valueOf(dlFolder.getGroupId()));
		map.put(Field.STAGING_GROUP, "false");
		map.put(Field.STATUS, String.valueOf(dlFolder.getStatus()));
		map.put(Field.TITLE, dlFolder.getName());
		map.put(
			Field.TITLE.concat("_sortable"),
			StringUtil.lowerCase(dlFolder.getName()));
		map.put(Field.USER_ID, String.valueOf(dlFolder.getUserId()));
		map.put(Field.USER_NAME, StringUtil.lowerCase(dlFolder.getUserName()));
		map.put("visible", "true");

		populateDateFields(dlFolder, map);
		populateLocalizedTitleFields(dlFolder, map);
		populateTreePath(dlFolder, map);

		dlFieldsFixture.populateRoleIdFields(
			dlFolder.getCompanyId(), dlFolder.getModelClassName(),
			dlFolder.getPrimaryKey(), dlFolder.getGroupId(), null, map);
		dlFieldsFixture.populateUID(
			dlFolder.getFolderId(), dlFolder.getModelClassName(), map);

		return map;
	}

	protected void populateLocalizedTitleFields(
		DLFolder dlFolder, Map<String, String> map) {

		String title = StringUtil.lowerCase(dlFolder.getName());

		map.put("localized_title", title);

		for (Locale availableLocale :
				LanguageUtil.getAvailableLocales(dlFolder.getGroupId())) {

			if (!map.containsKey(availableLocale) ||
				Validator.isNull(map.get(availableLocale))) {

				String key =
					"localized_title_" + String.valueOf(availableLocale);

				map.put(key, title);
				map.put(key.concat("_sortable"), title);
			}
		}
	}

	protected void populateTreePath(
		DLFolder dlFolder, Map<String, String> map) {

		ArrayList<String> treePathValues = new ArrayList<>(
			Arrays.asList(
				StringUtil.split(dlFolder.getTreePath(), CharPool.SLASH)));

		if (treePathValues.size() == 1) {
			map.put(Field.TREE_PATH, treePathValues.get(0));
		}
		else if (treePathValues.size() > 1) {
			map.put(Field.TREE_PATH, treePathValues.toString());
		}
	}

	private static final int _FOLDER_NAME_MAX_LENGTH = 100;

}
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

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.journal.model.JournalArticle;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.search.test.util.FieldValuesAssert;
import com.liferay.portal.search.test.util.IdempotentRetryAssert;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;

import java.text.Format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
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
public class AssetPermissionTest extends BaseAssetPermissionTestCase {

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

		setGroup(journalArticleFixture.addGroup());
		setIndexerClass(JournalArticle.class);
		setUser(journalArticleFixture.addUser());
	}

	@Test
	public void testJournalArticle() throws Exception {
		String content = RandomTestUtil.randomString();
		Locale locale = LocaleUtil.BRAZIL;
		String title = RandomTestUtil.randomString();

		JournalArticle journalArticle = journalArticleFixture.addJournalArticle(
			title, locale, content, journalArticleFixture.getServiceContext());

		Map<String, String> documentMap = new HashMap<>();

		populateExpectedFieldValues(journalArticle, documentMap);

		assertJournalArticle(title, locale, documentMap);
	}

	@Test
	public void testJournalArticlePermissions() throws Exception {
		String content = RandomTestUtil.randomString();
		Locale locale = LocaleUtil.BRAZIL;
		String title = RandomTestUtil.randomString();

		JournalArticle journalArticle = journalArticleFixture.addJournalArticle(
			title, locale, content, journalArticleFixture.getServiceContext());

		Map<String, String> documentMap = new HashMap<>();

		populateExpectedFieldValues(journalArticle, documentMap);

		assertJournalArticle(title, locale, documentMap);

		RoleTestUtil.removeResourcePermission(
			RoleConstants.GUEST, journalArticle.getModelClassName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(journalArticle.getResourcePrimKey()),
			ActionKeys.VIEW);

		String documentMapRoleId = documentMap.get(Field.ROLE_ID);
		String expectedRoleId = String.valueOf(
			journalArticleFieldsFixture.getRoleId(
				journalArticle.getCompanyId(), RoleConstants.OWNER));

		Assert.assertNotEquals(documentMapRoleId, expectedRoleId);
		Assert.assertTrue(
			documentMap.replace(
				Field.ROLE_ID, documentMapRoleId, expectedRoleId));

		assertJournalArticle(title, locale, documentMap);

		RoleTestUtil.removeResourcePermission(
			RoleConstants.SITE_MEMBER, journalArticle.getModelClassName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(journalArticle.getResourcePrimKey()),
			ActionKeys.VIEW);

		Assert.assertTrue(documentMap.containsKey(Field.GROUP_ROLE_ID));

		documentMap.remove(Field.GROUP_ROLE_ID);

		Assert.assertFalse(documentMap.containsKey(Field.GROUP_ROLE_ID));

		assertJournalArticle(title, locale, documentMap);
	}

	@Test
	public void testJournalArticleReindex() throws Exception {
		String content = RandomTestUtil.randomString();
		Locale locale = LocaleUtil.BRAZIL;
		String title = RandomTestUtil.randomString();

		JournalArticle journalArticle = journalArticleFixture.addJournalArticle(
			title, locale, content, journalArticleFixture.getServiceContext());

		Map<String, String> documentMap = new HashMap<>();

		populateExpectedFieldValues(journalArticle, documentMap);

		assertJournalArticle(title, locale, documentMap);

		journalArticleSearchFixture.reindex(journalArticle);

		Thread.sleep(3000);

		assertJournalArticle(title, locale, documentMap);
	}

	protected void assertJournalArticle(
			String keywords, Locale locale, Map<String, String> expectedValues)
		throws Exception {

		IdempotentRetryAssert.retryAssert(
			3, TimeUnit.SECONDS,
			() -> doAssertJournalArticle(keywords, locale, expectedValues));
	}

	protected Void doAssertJournalArticle(
		String keywords, Locale locale, Map<String, String> expectedValues) {

		Document document = journalArticleSearchFixture.searchOnlyOne(
			keywords, locale);

		journalArticleFieldsFixture.postProcessDocument(document);

		FieldValuesAssert.assertFieldValues(expectedValues, document, keywords);

		return null;
	}

	protected void populateExpectedFieldValues(
			JournalArticle journalArticle, Map<String, String> map)
		throws Exception {

		map.put(Field.ARTICLE_ID, journalArticle.getArticleId());
		map.put(
			Field.ARTICLE_ID.concat("_String_sortable"),
			journalArticle.getArticleId());
		map.put(Field.CLASS_NAME_ID, "0");
		map.put(Field.CLASS_PK, "0");

		DDMStructure ddmStructure = journalArticle.getDDMStructure();

		map.put(
			Field.CLASS_TYPE_ID, String.valueOf(ddmStructure.getStructureId()));

		map.put(
			Field.COMPANY_ID, String.valueOf(journalArticle.getCompanyId()));
		map.put("ddmStructureKey", journalArticle.getDDMStructureKey());
		map.put("ddmTemplateKey", journalArticle.getDDMTemplateKey());
		map.put(Field.ENTRY_CLASS_NAME, journalArticle.getModelClassName());
		map.put(
			Field.ENTRY_CLASS_PK,
			String.valueOf(journalArticle.getResourcePrimKey()));
		map.put(Field.FOLDER_ID, String.valueOf(journalArticle.getFolderId()));
		map.put(Field.GROUP_ID, String.valueOf(journalArticle.getGroupId()));
		map.put("head", "true");
		map.put("headListable", "true");
		map.put("latest", "true");
		map.put(Field.LAYOUT_UUID, journalArticle.getLayoutUuid());
		map.put(Field.PRIORITY, "0.0");

		if (journalArticleFieldsFixture.isSearchEngineSolr()) {
			map.put(Field.PRIORITY.concat("_sortable"), "0.0");
		}

		map.put(
			Field.ROOT_ENTRY_CLASS_PK,
			String.valueOf(journalArticle.getResourcePrimKey()));
		map.put(
			Field.SCOPE_GROUP_ID, String.valueOf(journalArticle.getGroupId()));
		map.put(Field.STAGING_GROUP, "false");
		map.put(Field.STATUS, String.valueOf(journalArticle.getStatus()));

		ArrayList<String> treePathValues = new ArrayList<>(
			Arrays.asList(
				StringUtil.split(
					journalArticle.getTreePath(), CharPool.SLASH)));

		if (treePathValues.size() == 1) {
			map.put(Field.TREE_PATH, treePathValues.get(0));
		}
		else if (treePathValues.size() > 1) {
			map.put(Field.TREE_PATH, treePathValues.toString());
		}

		map.put(Field.USER_ID, String.valueOf(journalArticle.getUserId()));
		map.put(
			Field.USER_NAME,
			StringUtil.lowerCase(journalArticle.getUserName()));
		map.put(Field.VERSION, String.valueOf(journalArticle.getVersion()));
		map.put("visible", "true");

		populateJournalArticleContent(journalArticle, map);
		populateJournalArticleDateFields(journalArticle, map);
		populateJournalArticleLocalizedTitleFields(journalArticle, map);
		populateJournalArticleTitleFields(journalArticle, map);

		journalArticleFieldsFixture.populateRoleIdFields(
			journalArticle.getCompanyId(), journalArticle.getModelClassName(),
			journalArticle.getResourcePrimKey(), journalArticle.getGroupId(),
			null, map);
		journalArticleFieldsFixture.populateUID(
			journalArticle.getId(), journalArticle.getModelClassName(), map);
	}

	protected void populateJournalArticleContent(
			JournalArticle journalArticle, Map<String, String> map)
		throws Exception {

		String content = journalArticle.getContent();

		com.liferay.portal.kernel.xml.Document document = SAXReaderUtil.read(
			content);

		Element rootElement = document.getRootElement();

		List<String> availableLocales = Arrays.asList(
			StringUtil.split(rootElement.attributeValue("available-locales")));

		String defaultLanguageId = LanguageUtil.getLanguageId(
			LocaleUtil.getDefault());

		DDMStructure ddmStructure = journalArticle.getDDMStructure();

		long ddmStructureId = ddmStructure.getStructureId();

		for (String languageId : journalArticle.getAvailableLanguageIds()) {
			if (availableLocales.contains(languageId)) {
				String actualContent = journalArticleFixture.getActualContent(
					journalArticle, languageId);
				String fieldName = StringBundler.concat(
					Field.CONTENT, StringPool.UNDERLINE, languageId);

				if (languageId.equals(defaultLanguageId)) {
					fieldName = Field.CONTENT;
				}

				map.put(fieldName, actualContent);

				String key = StringBundler.concat(
					"ddm__text__", String.valueOf(ddmStructureId),
					StringPool.DOUBLE_UNDERLINE, fieldName);

				map.put(key, actualContent);
				map.put(
					key.concat("_String_sortable"),
					StringUtil.lowerCase(actualContent));
			}
		}
	}

	protected void populateJournalArticleDateFields(
		JournalArticle journalArticle, Map<String, String> map) {

		Format dateFormat = FastDateFormatFactoryUtil.getSimpleDateFormat(
			PropsUtil.get(PropsKeys.INDEX_DATE_FORMAT_PATTERN));

		Date expirationDate = new Date(Long.MAX_VALUE);

		if (journalArticleFieldsFixture.isSearchEngineElasticsearch()) {
			dateFormat = FastDateFormatFactoryUtil.getSimpleDateFormat(
				"yyyyMMddHHmmss");

			map.put(Field.EXPIRATION_DATE, "99950812133000");
		}
		else {
			map.put(Field.EXPIRATION_DATE, dateFormat.format(expirationDate));
		}

		map.put(
			Field.CREATE_DATE,
			dateFormat.format(journalArticle.getCreateDate()));
		map.put(
			Field.CREATE_DATE.concat("_sortable"),
			String.valueOf(journalArticle.getCreateDate().getTime()));
		map.put(
			"displayDate", dateFormat.format(journalArticle.getDisplayDate()));
		map.put(
			"displayDate_sortable",
			String.valueOf(journalArticle.getDisplayDate().getTime()));
		map.put(
			Field.EXPIRATION_DATE.concat("_sortable"),
			String.valueOf(expirationDate.getTime()));
		map.put(
			Field.MODIFIED_DATE,
			dateFormat.format(journalArticle.getModifiedDate()));
		map.put(
			Field.MODIFIED_DATE.concat("_sortable"),
			String.valueOf(journalArticle.getModifiedDate().getTime()));

		Date publishDate = journalArticle.getDisplayDate();

		map.put(Field.PUBLISH_DATE, dateFormat.format(publishDate));
		map.put(
			Field.PUBLISH_DATE.concat("_sortable"),
			String.valueOf(publishDate.getTime()));
	}

	protected void populateJournalArticleLocalizedTitleFields(
		JournalArticle journalArticle, Map<String, String> map) {

		for (Locale locale :
				LanguageUtil.getAvailableLocales(journalArticle.getGroupId())) {

			String title = StringUtil.lowerCase(
				journalArticle.getTitle(locale));

			map.put("localized_title", title);

			String key = StringBundler.concat(
				"localized_title_", LanguageUtil.getLanguageId(locale));

			map.put(key, title);
			map.put(key.concat("_sortable"), title);
		}
	}

	protected void populateJournalArticleTitleFields(
		JournalArticle journalArticle, Map<String, String> map) {

		String[] languageIds = LocalizationUtil.getAvailableLanguageIds(
			journalArticle.getDocument());

		for (String languageId : languageIds) {
			map.put(
				LocalizationUtil.getLocalizedName(Field.TITLE, languageId),
				journalArticle.getTitle(languageId));
		}
	}

}
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
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileEntryMetadata;
import com.liferay.document.library.kernel.model.DLFileVersion;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.test.util.FieldValuesAssert;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;

import java.text.Format;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class DLFileEntryIndexerIndexedFieldsTest extends BaseDLIndexerTestCase {

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
		setIndexerClass(DLFileEntry.class);
		setUser(dlFixture.addUser());
	}

	@Test
	public void testIndexedFields() throws Exception {
		dlFixture.updateDisplaySettings(LocaleUtil.JAPAN);

		String fileName_jp = "content_search.txt";

		String searchTerm = "新規";

		FileEntry fileEntry = dlFixture.addFileEntry(
			fileName_jp, dlFixture.getServiceContext());

		Document document = dlSearchFixture.searchOnlyOne(
			searchTerm, LocaleUtil.JAPAN);

		dlFieldsFixture.postProcessDocument(document);

		Map<String, String> map = new HashMap<>();

		populateExpectedFieldValues(fileEntry, map);

		FieldValuesAssert.assertFieldValues(map, document, searchTerm);
	}

	protected void populateDateFields(
		FileEntry fileEntry, Map<String, String> map) {

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

		map.put(
			Field.CREATE_DATE, dateFormat.format(fileEntry.getCreateDate()));
		map.put(
			Field.CREATE_DATE.concat("_sortable"),
			String.valueOf(fileEntry.getCreateDate().getTime()));
		map.put(
			Field.EXPIRATION_DATE.concat("_sortable"),
			String.valueOf(expirationDate.getTime()));
		map.put(
			Field.MODIFIED_DATE,
			dateFormat.format(fileEntry.getModifiedDate()));
		map.put(
			Field.MODIFIED_DATE.concat("_sortable"),
			String.valueOf(fileEntry.getModifiedDate().getTime()));

		Date publishDate = new Date(0);

		map.put(Field.PUBLISH_DATE, dateFormat.format(publishDate));
		map.put(
			Field.PUBLISH_DATE.concat("_sortable"),
			String.valueOf(publishDate.getTime()));
	}

	protected void populateDDMFields(
			FileEntry fileEntry, Map<String, String> map)
		throws PortalException {

		long ddmStructureId = 0;

		DLFileEntry dlFileEntry = (DLFileEntry)fileEntry.getModel();

		DLFileVersion dlFileVersion = dlFileEntry.getFileVersion();

		List<DLFileEntryMetadata> dlFileEntryMetadatas =
			dlFileEntryMetadataLocalService.getFileVersionFileEntryMetadatas(
				dlFileVersion.getFileVersionId());

		for (DLFileEntryMetadata dlFileEntryMetadata : dlFileEntryMetadatas) {
			if (dlFileEntryMetadata != null) {
				ddmStructureId = dlFileEntryMetadata.getDDMStructureId();
			}
		}

		String languageId = LocaleUtil.toLanguageId(LocaleUtil.getDefault());

		map.put(
			"ddm__text__" + ddmStructureId + "__HttpHeaders_CONTENT_ENCODING_" +
				languageId,
			"UTF-8");

		map.put(
			"ddm__text__" + ddmStructureId + "__HttpHeaders_CONTENT_ENCODING_" +
				languageId + "_String_sortable",
			"utf-8");

		map.put(
			"ddm__text__" + ddmStructureId + "__HttpHeaders_CONTENT_TYPE_" +
				languageId,
			"text/plain; charset=UTF-8");

		map.put(
			"ddm__text__" + ddmStructureId + "__HttpHeaders_CONTENT_TYPE_" +
				languageId + "_String_sortable",
			"text/plain; charset=utf-8");
	}

	protected void populateExpectedFieldValues(
			FileEntry fileEntry, Map<String, String> map)
		throws PortalException {

		map.put(Field.CLASS_NAME_ID, "0");
		map.put(Field.CLASS_PK, "0");
		map.put("classTypeId", "0");
		map.put(Field.COMPANY_ID, String.valueOf(fileEntry.getCompanyId()));

		String contents = FileUtil.extractText(
			fileEntry.getContentStream(), fileEntry.getTitle());

		map.put("content_ja_JP", contents.trim());

		map.put(
			"dataRepositoryId", String.valueOf(fileEntry.getRepositoryId()));
		map.put("ddmContent", "text/plain; charset=UTF-8 UTF-8");
		map.put(
			Field.ENTRY_CLASS_PK, String.valueOf(fileEntry.getFileEntryId()));
		map.put(Field.ENTRY_CLASS_NAME, DLFileEntry.class.getName());
		map.put("extension", fileEntry.getExtension());
		map.put("fileEntryTypeId", "0");
		map.put(Field.FOLDER_ID, String.valueOf(fileEntry.getFolderId()));
		map.put(Field.GROUP_ID, String.valueOf(fileEntry.getGroupId()));
		map.put("hidden", "false");
		map.put("mimeType", fileEntry.getMimeType().replaceAll("/", "_"));
		map.put("path", fileEntry.getTitle());
		map.put("readCount", String.valueOf(fileEntry.getReadCount()));
		map.put(Field.PRIORITY, "0.0");

		if (dlFieldsFixture.isSearchEngineSolr()) {
			map.put(Field.PRIORITY.concat("_sortable"), "0.0");
		}

		map.put(
			Field.PROPERTIES, FileUtil.stripExtension(fileEntry.getTitle()));
		map.put(Field.SCOPE_GROUP_ID, String.valueOf(fileEntry.getGroupId()));
		map.put("size", String.valueOf(fileEntry.getSize()));
		map.put(Field.STAGING_GROUP, "false");
		map.put(Field.TITLE, fileEntry.getTitle());
		map.put(Field.TITLE + "_sortable", fileEntry.getTitle());
		map.put(
			Field.USER_NAME, StringUtil.toLowerCase(fileEntry.getUserName()));
		map.put(Field.STATUS, "0");
		map.put(Field.TREE_PATH, "");
		map.put(Field.USER_ID, String.valueOf(fileEntry.getUserId()));
		map.put("visible", "true");

		populateDateFields(fileEntry, map);
		populateDDMFields(fileEntry, map);
		populateLocalizedTitleFields(fileEntry, map);

		dlFieldsFixture.populateRoleIdFields(
			fileEntry.getCompanyId(), DLFileEntry.class.getName(),
			fileEntry.getPrimaryKey(), fileEntry.getGroupId(), null, map);
		dlFieldsFixture.populateUID(
			fileEntry.getFileEntryId(), DLFileEntry.class.getName(), map);
	}

	protected void populateLocalizedTitleFields(
		FileEntry fileEntry, Map<String, String> map) {

		String title = fileEntry.getTitle();

		map.put("localized_title", title);

		String key =
			"localized_title_" +
				LocaleUtil.toLanguageId(LocaleUtil.getSiteDefault());

		map.put(key, title);
		map.put(key.concat("_sortable"), title);
	}

}
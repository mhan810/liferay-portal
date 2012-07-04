/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.service.persistence.lar;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Image;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.RepositoryEntry;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.persistence.LayoutUtil;
import com.liferay.portal.service.persistence.impl.BaseDataHandlerImpl;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.documentlibrary.lar.DLLegacyPortletDataHandlerImpl;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.util.DLUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mate Thurzo
 */
public class JournalArticleDataHandlerImpl
	extends BaseDataHandlerImpl<JournalArticle>
	implements JournalArticleDataHandler {

	@Override
	public void doDigest(JournalArticle article) throws Exception {
		return;
		//if (checkDateRange &&
		/*if(!portletDataContext.isWithinDateRange(article.getModifiedDate())) {
			return;
		}

		if ((article.getStatus() != WorkflowConstants.STATUS_APPROVED) &&
				(article.getStatus() != WorkflowConstants.STATUS_EXPIRED)) {

			return;
		}

		String path = getArticlePath(portletDataContext, article);

		if (!portletDataContext.isPathNotProcessed(path)) {
			return;
		}

		// Clone this article to make sure changes to its content are never
		// persisted

		article = (JournalArticle)article.clone();

		if (Validator.isNotNull(article.getStructureId())) {
			JournalStructure structure =
				JournalStructureLocalServiceUtil.getStructure(
					article.getGroupId(), article.getStructureId(), true);

			journalStructureLarPersistence.digest(
				structure, digest, portletDataContext);
		}

		String templateId = article.getTemplateId();

		// ToDo: some kind of context is necessary to have these variables
		/*if (Validator.isNotNull(preferenceTemplateId)) {
			templateId = preferenceTemplateId;
		} */

		/*if (Validator.isNotNull(templateId)) {
			JournalTemplate template =
				JournalTemplateLocalServiceUtil.getTemplate(
					article.getGroupId(), templateId, true);

			journalTemplateLarPersistence.digest(
				template, digest, portletDataContext);
		}

		Image smallImage = ImageUtil.fetchByPrimaryKey(
			article.getSmallImageId());

		if (article.isSmallImage() && (smallImage != null)) {
			String smallImagePath = getArticleSmallImagePath(
				portletDataContext, article);

			article.setSmallImageType(smallImage.getType());

			addZipEntry(smallImagePath, smallImage.getTextObj());
		}

		if (portletDataContext.getBooleanParameter(
				JournalPortletDataHandler._NAMESPACE, "images")) {

			List<JournalArticleImage> articleImages =
				JournalArticleImageUtil.findByG_A_V(
					article.getGroupId(), article.getArticleId(),
					article.getVersion());

			for (JournalArticleImage articleImage : articleImages) {
				Image image = null;

				try {
					image = ImageUtil.findByPrimaryKey(
						articleImage.getArticleImageId());
				}
				catch (NoSuchImageException nsie) {
					continue;
				}

				if (image.getTextObj() == null) {
					continue;
				}

				String articleImagePath = getArticleImagePath(
					portletDataContext, article, articleImage, image);

				if (!portletDataContext.isPathNotProcessed(articleImagePath)) {
					continue;
				}

				portletDataContext.addZipEntry(
					articleImagePath, image.getTextObj());
			}
		}

		article.setStatusByUserUuid(article.getStatusByUserUuid());

		if (portletDataContext.getBooleanParameter(
			JournalPortletDataHandler._NAMESPACE, "embedded-assets")) {

			String content = exportReferencedContent(
				portletDataContext, article.getContent(), JournalArticle.class);

			article.setContent(content);
		}

		/*portletDataContext.addClassedModel(
			articleElement, path, article,
			JournalPortletDataHandler._NAMESPACE);*/
	}

	public String exportReferencedContent(
			PortletDataContext portletDataContext, String content, Object clazz)
		throws Exception {

		content = exportDLFileEntries(portletDataContext, content, false);
		content = exportLayoutFriendlyURLs(portletDataContext, content);
		content = exportLinksToLayout(portletDataContext, content);

		if (clazz instanceof JournalArticle) {
			content = StringUtil.replace(
				content, StringPool.AMPERSAND_ENCODED, StringPool.AMPERSAND);
		}

		return content;
	}

	public String importReferencedContent(
			PortletDataContext portletDataContext, Element parentElement,
			String content)
		throws Exception {

		content = importDLFileEntries(
			portletDataContext, parentElement, content);

		Group group = GroupLocalServiceUtil.getGroup(
			portletDataContext.getScopeGroupId());

		content = StringUtil.replace(
			content, "@data_handler_group_friendly_url@",
			group.getFriendlyURL());

		content = importLinksToLayout(portletDataContext, content);

		return content;
	}

	public void importReferencedData(
			PortletDataContext portletDataContext, Element entityElement)
		throws Exception {

		Element dlRepositoriesElement = entityElement.element(
			"dl-repositories");

		List<Element> dlRepositoryElements = Collections.emptyList();

		if (dlRepositoriesElement != null) {
			dlRepositoryElements = dlRepositoriesElement.elements("repository");
		}

		for (Element repositoryElement : dlRepositoryElements) {
			DLLegacyPortletDataHandlerImpl.importRepository(
				portletDataContext, repositoryElement);
		}

		Element dlRepositoryEntriesElement = entityElement.element(
			"dl-repository-entries");

		List<Element> dlRepositoryEntryElements = Collections.emptyList();

		if (dlRepositoryEntriesElement != null) {
			dlRepositoryEntryElements = dlRepositoryEntriesElement.elements(
				"repository-entry");
		}

		for (Element repositoryEntryElement : dlRepositoryEntryElements) {
			DLLegacyPortletDataHandlerImpl.importRepositoryEntry(
				portletDataContext, repositoryEntryElement);
		}

		Element dlFoldersElement = entityElement.element("dl-folders");

		List<Element> dlFolderElements = Collections.emptyList();

		if (dlFoldersElement != null) {
			dlFolderElements = dlFoldersElement.elements("folder");
		}

		for (Element folderElement : dlFolderElements) {
			DLLegacyPortletDataHandlerImpl.importFolder(
				portletDataContext, folderElement);
		}

		Element dlFileEntriesElement = entityElement.element("dl-file-entries");

		List<Element> dlFileEntryElements = Collections.emptyList();

		if (dlFileEntriesElement != null) {
			dlFileEntryElements = dlFileEntriesElement.elements("file-entry");
		}

		for (Element fileEntryElement : dlFileEntryElements) {
			DLLegacyPortletDataHandlerImpl.importFileEntry(
				portletDataContext, fileEntryElement);
		}

		Element dlFileRanksElement = entityElement.element("dl-file-ranks");

		List<Element> dlFileRankElements = Collections.emptyList();

		if (dlFileRanksElement != null) {
			dlFileRankElements = dlFileRanksElement.elements("file-rank");
		}

		for (Element fileRankElement : dlFileRankElements) {
			DLLegacyPortletDataHandlerImpl.importFileRank(
				portletDataContext, fileRankElement);
		}
	}


	public String getArticlePath(
			PortletDataContext portletDataContext, JournalArticle article)
			throws Exception {

		StringBundler sb = new StringBundler(8);

		sb.append(portletDataContext.getPortletPath(PortletKeys.JOURNAL));
		sb.append("/articles/");
		sb.append(article.getArticleResourceUuid());
		sb.append(StringPool.SLASH);
		sb.append(article.getVersion());
		sb.append(StringPool.SLASH);
		sb.append("article.xml");

		return sb.toString();
	}

	private String exportDLFileEntries(
			PortletDataContext portletDataContext, String content,
			boolean checkDateRange)
		throws Exception {

		Group group = GroupLocalServiceUtil.getGroup(
				portletDataContext.getGroupId());

		if (group.isStagingGroup()) {
			group = group.getLiveGroup();
		}

		if (group.isStaged() && !group.isStagedRemotely() &&
				!group.isStagedPortlet(PortletKeys.DOCUMENT_LIBRARY)) {

			return content;
		}

		StringBuilder sb = new StringBuilder(content);

		int beginPos = content.length();
		int currentLocation = -1;

		while (true) {
			currentLocation = content.lastIndexOf(
				"/c/document_library/get_file?", beginPos);

			if (currentLocation == -1) {
				currentLocation = content.lastIndexOf("/documents/", beginPos);
			}

			if (currentLocation == -1) {
				return sb.toString();
			}

			beginPos = currentLocation;

			int endPos1 = content.indexOf(CharPool.APOSTROPHE, beginPos);
			int endPos2 = content.indexOf(CharPool.CLOSE_BRACKET, beginPos);
			int endPos3 = content.indexOf(CharPool.CLOSE_CURLY_BRACE, beginPos);
			int endPos4 = content.indexOf(CharPool.CLOSE_PARENTHESIS, beginPos);
			int endPos5 = content.indexOf(CharPool.LESS_THAN, beginPos);
			int endPos6 = content.indexOf(CharPool.QUESTION, beginPos);
			int endPos7 = content.indexOf(CharPool.QUOTE, beginPos);
			int endPos8 = content.indexOf(CharPool.SPACE, beginPos);

			int endPos = endPos1;

			if ((endPos == -1) || ((endPos2 != -1) && (endPos2 < endPos))) {
				endPos = endPos2;
			}

			if ((endPos == -1) || ((endPos3 != -1) && (endPos3 < endPos))) {
				endPos = endPos3;
			}

			if ((endPos == -1) || ((endPos4 != -1) && (endPos4 < endPos))) {
				endPos = endPos4;
			}

			if ((endPos == -1) || ((endPos5 != -1) && (endPos5 < endPos))) {
				endPos = endPos5;
			}

			if ((endPos == -1) || ((endPos6 != -1) && (endPos6 < endPos))) {
				endPos = endPos6;
			}

			if ((endPos == -1) || ((endPos7 != -1) && (endPos7 < endPos))) {
				endPos = endPos7;
			}

			if ((endPos == -1) || ((endPos8 != -1) && (endPos8 < endPos))) {
				endPos = endPos8;
			}

			if ((beginPos == -1) || (endPos == -1)) {
				break;
			}

			try {
				String oldParameters = content.substring(beginPos, endPos);

				while (oldParameters.contains(StringPool.AMPERSAND_ENCODED)) {
					oldParameters = oldParameters.replace(
						StringPool.AMPERSAND_ENCODED, StringPool.AMPERSAND);
				}

				Map<String, String[]> map = new HashMap<String, String[]>();

				if (oldParameters.startsWith("/documents/")) {
					String[] pathArray = oldParameters.split(StringPool.SLASH);

					map.put("groupId", new String[] {pathArray[2]});

					if (pathArray.length == 4) {
						map.put("uuid", new String[] {pathArray[3]});
					}
					else if (pathArray.length == 5) {
						map.put("folderId", new String[] {pathArray[3]});

						String name = HttpUtil.decodeURL(pathArray[4]);

						int pos = name.indexOf(StringPool.QUESTION);

						if (pos != -1) {
							name = name.substring(0, pos);
						}

						map.put("name", new String[] {name});
					}
					else if (pathArray.length > 5) {
						String uuid = pathArray[5];

						int pos = uuid.indexOf(StringPool.QUESTION);

						if (pos != -1) {
							uuid = uuid.substring(0, pos);
						}

						map.put("uuid", new String[] {uuid});
					}
				}
				else {
					oldParameters = oldParameters.substring(
						oldParameters.indexOf(CharPool.QUESTION) + 1);

					map = HttpUtil.parameterMapFromString(oldParameters);
				}

				FileEntry fileEntry = null;

				String uuid = MapUtil.getString(map, "uuid");

				if (Validator.isNotNull(uuid)) {
					String groupIdString = MapUtil.getString(map, "groupId");

					long groupId = GetterUtil.getLong(groupIdString);

					if (groupIdString.equals("@group_id@")) {
						groupId = portletDataContext.getScopeGroupId();
					}

					fileEntry =
						DLAppLocalServiceUtil.getFileEntryByUuidAndGroupId(
							uuid, groupId);
				}
				else {
					String folderIdString = MapUtil.getString(map, "folderId");

					if (Validator.isNotNull(folderIdString)) {
						long folderId = GetterUtil.getLong(folderIdString);
						String name = MapUtil.getString(map, "name");

						String groupIdString = MapUtil.getString(
							map, "groupId");

						long groupId = GetterUtil.getLong(groupIdString);

						if (groupIdString.equals("@group_id@")) {
							groupId = portletDataContext.getScopeGroupId();
						}

						fileEntry = DLAppLocalServiceUtil.getFileEntry(
							groupId, folderId, name);
					}
				}

				if (fileEntry == null) {
					beginPos--;

					continue;
				}

				// ToDo: Create DLPortletLarPersistence
				/*dlPortletLarPersistence.digest(
					file, larDigest, portletDataContext, checkDateRange);*/

				String path = null;

				if (fileEntry.isDefaultRepository()) {
					// ToDo: check these static methods
					/*path = DLPortletDataHandlerImpl.getFileEntryPath(
						portletDataContext, fileEntry);*/

				}
				else {
					/*path = DLPortletDataHandlerImpl.getRepositoryEntryPath(
						portletDataContext, fileEntry.getFileEntryId());*/
				}

				String dlReference = "[$dl-reference=" + path + "$]";

				sb.replace(beginPos, endPos, dlReference);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}
				else if (_log.isWarnEnabled()) {
					_log.warn(e.getMessage());
				}
			}

			beginPos--;
		}

		return sb.toString();
	}

	private String exportLayoutFriendlyURLs(
		PortletDataContext portletDataContext, String content) {

		Group group = null;

		try {
			group = GroupLocalServiceUtil.getGroup(
				portletDataContext.getScopeGroupId());
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e);
			}

			return content;
		}

		StringBuilder sb = new StringBuilder(content);

		String friendlyURLPrivateGroupPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING;
		String friendlyURLPrivateUserPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PRIVATE_USER_SERVLET_MAPPING;
		String friendlyURLPublicPath =
			PropsValues.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING;

		String href = "href=";

		int beginPos = content.length();

		while (true) {
			int hrefLength = href.length();

			beginPos = content.lastIndexOf(href, beginPos);

			if (beginPos == -1) {
				break;
			}

			char c = content.charAt(beginPos + hrefLength);

			if ((c == CharPool.APOSTROPHE) || (c == CharPool.QUOTE)) {
				hrefLength++;
			}

			int endPos1 = content.indexOf(
				CharPool.APOSTROPHE, beginPos + hrefLength);
			int endPos2 = content.indexOf(
				CharPool.CLOSE_BRACKET, beginPos + hrefLength);
			int endPos3 = content.indexOf(
				CharPool.CLOSE_CURLY_BRACE, beginPos + hrefLength);
			int endPos4 = content.indexOf(
				CharPool.CLOSE_PARENTHESIS, beginPos + hrefLength);
			int endPos5 = content.indexOf(
				CharPool.LESS_THAN, beginPos + hrefLength);
			int endPos6 = content.indexOf(
				CharPool.QUESTION, beginPos + hrefLength);
			int endPos7 = content.indexOf(
				CharPool.QUOTE, beginPos + hrefLength);
			int endPos8 = content.indexOf(
				CharPool.SPACE, beginPos + hrefLength);

			int endPos = endPos1;

			if ((endPos == -1) || ((endPos2 != -1) && (endPos2 < endPos))) {
				endPos = endPos2;
			}

			if ((endPos == -1) || ((endPos3 != -1) && (endPos3 < endPos))) {
				endPos = endPos3;
			}

			if ((endPos == -1) || ((endPos4 != -1) && (endPos4 < endPos))) {
				endPos = endPos4;
			}

			if ((endPos == -1) || ((endPos5 != -1) && (endPos5 < endPos))) {
				endPos = endPos5;
			}

			if ((endPos == -1) || ((endPos6 != -1) && (endPos6 < endPos))) {
				endPos = endPos6;
			}

			if ((endPos == -1) || ((endPos7 != -1) && (endPos7 < endPos))) {
				endPos = endPos7;
			}

			if ((endPos == -1) || ((endPos8 != -1) && (endPos8 < endPos))) {
				endPos = endPos8;
			}

			if (endPos == -1) {
				beginPos--;

				continue;
			}

			String url = content.substring(beginPos + hrefLength, endPos);

			if (!url.startsWith(friendlyURLPrivateGroupPath) &&
				!url.startsWith(friendlyURLPrivateUserPath) &&
				!url.startsWith(friendlyURLPublicPath)) {

				beginPos--;

				continue;
			}

			int beginGroupPos = content.indexOf(
				CharPool.SLASH, beginPos + hrefLength + 1);

			if (beginGroupPos == -1) {
				beginPos--;

				continue;
			}

			int endGroupPos = content.indexOf(
				CharPool.SLASH, beginGroupPos + 1);

			if (endGroupPos == -1) {
				beginPos--;

				continue;
			}

			String groupFriendlyURL = content.substring(
				beginGroupPos, endGroupPos);

			if (groupFriendlyURL.equals(group.getFriendlyURL())) {
				sb.replace(
					beginGroupPos, endGroupPos,
					"@data_handler_group_friendly_url@");
			}

			beginPos--;
		}

		return sb.toString();
	}

	private String exportLinksToLayout(
			PortletDataContext portletDataContext, String content)
		throws Exception {

		List<String> oldLinksToLayout = new ArrayList<String>();
		List<String> newLinksToLayout = new ArrayList<String>();

		Matcher matcher = _exportLinksToLayoutPattern.matcher(content);

		while (matcher.find()) {
			long layoutId = GetterUtil.getLong(matcher.group(1));

			String type = matcher.group(2);

			boolean privateLayout = type.startsWith("private");

			try {
				Layout layout = LayoutLocalServiceUtil.getLayout(
					portletDataContext.getScopeGroupId(), privateLayout,
					layoutId);

				String oldLinkToLayout = matcher.group(0);

				StringBundler sb = new StringBundler(5);

				sb.append(type);
				sb.append(StringPool.AT);
				sb.append(layout.getUuid());
				sb.append(StringPool.AT);
				sb.append(layout.getFriendlyURL());

				String newLinkToLayout = StringUtil.replace(
					oldLinkToLayout, type, sb.toString());

				oldLinksToLayout.add(oldLinkToLayout);
				newLinksToLayout.add(newLinkToLayout);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled() || _log.isWarnEnabled()) {
					String message =
						"Unable to get layout with ID " + layoutId +
							" in group " + portletDataContext.getScopeGroupId();

					if (_log.isWarnEnabled()) {
						_log.warn(message);
					}
					else {
						_log.debug(message, e);
					}
				}
			}
		}

		content = StringUtil.replace(
			content, ArrayUtil.toStringArray(oldLinksToLayout.toArray()),
			ArrayUtil.toStringArray(newLinksToLayout.toArray()));

		return content;
	}

	private String getArticleImagePath(
			PortletDataContext portletDataContext, JournalArticle article)
		throws Exception {

		StringBundler sb = new StringBundler(6);

		sb.append(portletDataContext.getPortletPath(PortletKeys.JOURNAL));
		sb.append("/articles/");
		sb.append(article.getArticleResourceUuid());
		sb.append(StringPool.SLASH);
		sb.append(article.getVersion());
		sb.append(StringPool.SLASH);

		return sb.toString();
	}

	private String getArticleImagePath(
			PortletDataContext portletDataContext, JournalArticle article,
			JournalArticleImage articleImage, Image image)
		throws Exception {

		StringBundler sb = new StringBundler(13);

		sb.append(portletDataContext.getPortletPath(PortletKeys.JOURNAL));
		sb.append("/articles/");
		sb.append(article.getArticleResourceUuid());
		sb.append(StringPool.SLASH);
		sb.append(article.getVersion());
		sb.append(StringPool.SLASH);
		sb.append(articleImage.getElInstanceId());
		sb.append(StringPool.UNDERLINE);
		sb.append(articleImage.getElName());

		if (Validator.isNotNull(articleImage.getLanguageId())) {
			sb.append(StringPool.UNDERLINE);
			sb.append(articleImage.getLanguageId());
		}

		sb.append(StringPool.PERIOD);
		sb.append(image.getType());

		return sb.toString();
	}

	private String getArticleSmallImagePath(
			PortletDataContext portletDataContext, JournalArticle article)
		throws Exception {

		StringBundler sb = new StringBundler(6);

		sb.append(portletDataContext.getPortletPath(PortletKeys.JOURNAL));
		sb.append("/articles/");
		sb.append(article.getArticleResourceUuid());
		sb.append("/thumbnail");
		sb.append(StringPool.PERIOD);
		sb.append(article.getSmallImageType());

		return sb.toString();
	}

	private String importDLFileEntries(
			PortletDataContext portletDataContext, Element parentElement,
			String content)
		throws Exception {

		List<Element> dlReferenceElements = parentElement.elements(
			"dl-reference");

		for (Element dlReferenceElement : dlReferenceElements) {
			String dlReferencePath = dlReferenceElement.attributeValue("path");

			String fileEntryUUID = null;

			try {
				Object zipEntryObject = portletDataContext.getZipEntryAsObject(
					dlReferencePath);

				if (zipEntryObject == null) {
					if (_log.isWarnEnabled()) {
						_log.warn("Unable to reference " + dlReferencePath);
					}

					continue;
				}

				boolean defaultRepository = GetterUtil.getBoolean(
					dlReferenceElement.attributeValue("default-repository"));

				if (defaultRepository) {
					FileEntry fileEntry = (FileEntry)zipEntryObject;

					fileEntryUUID = fileEntry.getUuid();
				}
				else {
					RepositoryEntry repositoryEntry =
						(RepositoryEntry)zipEntryObject;

					fileEntryUUID = repositoryEntry.getUuid();
				}
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}
				else if (_log.isWarnEnabled()) {
					_log.warn(e.getMessage());
				}
			}

			if (fileEntryUUID == null) {
				continue;
			}

			FileEntry fileEntry =
				DLAppLocalServiceUtil.getFileEntryByUuidAndGroupId(
					fileEntryUUID, portletDataContext.getGroupId());

			if (fileEntry == null) {
				continue;
			}

			String dlReference = "[$dl-reference=" + dlReferencePath + "$]";

			String url = DLUtil.getPreviewURL(
				fileEntry, fileEntry.getFileVersion(), null, StringPool.BLANK,
				false, false);

			content = StringUtil.replace(content, dlReference, url);
		}

		return content;
	}

	private String importLinksToLayout(
			PortletDataContext portletDataContext, String content)
		throws Exception {

		List<String> oldLinksToLayout = new ArrayList<String>();
		List<String> newLinksToLayout = new ArrayList<String>();

		Matcher matcher = _importLinksToLayoutPattern.matcher(content);

		while (matcher.find()) {
			long oldLayoutId = GetterUtil.getLong(matcher.group(1));

			long newLayoutId = oldLayoutId;

			String type = matcher.group(2);

			boolean privateLayout = type.startsWith("private");

			String layoutUuid = matcher.group(3);

			String friendlyURL = matcher.group(4);

			try {
				Layout layout = LayoutUtil.fetchByUUID_G(
					layoutUuid, portletDataContext.getScopeGroupId());

				if (layout == null) {
					layout = LayoutUtil.fetchByG_P_F(
						portletDataContext.getScopeGroupId(), privateLayout,
						friendlyURL);
				}

				if (layout == null) {
					layout = LayoutUtil.fetchByG_P_L(
						portletDataContext.getScopeGroupId(), privateLayout,
						oldLayoutId);
				}

				if (layout == null) {
					if (_log.isWarnEnabled()) {
						StringBundler sb = new StringBundler(9);

						sb.append("Unable to get layout with UUID ");
						sb.append(layoutUuid);
						sb.append(", friendly URL ");
						sb.append(friendlyURL);
						sb.append(", or ");
						sb.append("layoutId ");
						sb.append(oldLayoutId);
						sb.append(" in group ");
						sb.append(portletDataContext.getScopeGroupId());

						_log.warn(sb.toString());
					}
				}
				else {
					newLayoutId = layout.getLayoutId();
				}
			}
			catch (SystemException se) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to get layout in group " +
							portletDataContext.getScopeGroupId(), se);
				}
			}

			String oldLinkToLayout = matcher.group(0);

			StringBundler sb = new StringBundler(4);

			sb.append(StringPool.AT);
			sb.append(layoutUuid);
			sb.append(StringPool.AT);
			sb.append(friendlyURL);

			String newLinkToLayout = StringUtil.replace(
				oldLinkToLayout,
				new String[] {sb.toString(), String.valueOf(oldLayoutId)},
				new String[] {StringPool.BLANK, String.valueOf(newLayoutId)});

			oldLinksToLayout.add(oldLinkToLayout);
			newLinksToLayout.add(newLinkToLayout);
		}

		content = StringUtil.replace(
			content, ArrayUtil.toStringArray(oldLinksToLayout.toArray()),
			ArrayUtil.toStringArray(newLinksToLayout.toArray()));

		return content;
	}

	private static Log _log = LogFactoryUtil.getLog(
		JournalArticleDataHandlerImpl.class);

	private static Pattern _exportLinksToLayoutPattern = Pattern.compile(
		"\\[([0-9]+)@(public|private\\-[a-z]*)\\]");

	private static Pattern _importLinksToLayoutPattern = Pattern.compile(
		"\\[([0-9]+)@(public|private\\-[a-z]*)@(\\p{XDigit}{8}\\-" +
			"(?:\\p{XDigit}{4}\\-){3}\\p{XDigit}{12})@([^\\]]*)\\]");

}

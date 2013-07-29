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

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.lar.MissingReferences;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Repository;
import com.liferay.portal.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.base.StagingLocalServiceBaseImpl;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.documentlibrary.NoSuchFolderException;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.store.DLStoreUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.Map;

/**
 * @author Michael C. Han
 * @author Mate Thurzo
 */
public class StagingLocalServiceImpl extends StagingLocalServiceBaseImpl {

	@Override
	public void cleanUpStagingRequest(long stagingRequestId)
		throws PortalException, SystemException {

		try {
			PortletFileRepositoryUtil.deleteFolder(stagingRequestId);
		}
		catch (NoSuchFolderException nsfe) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unable to clean up staging request " + stagingRequestId,
					nsfe);
			}
		}
	}

	@Override
	public long createStagingRequest(long userId, long groupId, String checksum)
		throws PortalException, SystemException {

		ServiceContext serviceContext = new ServiceContext();

		Repository repository = PortletFileRepositoryUtil.addPortletRepository(
			groupId, PortletKeys.SITES_ADMIN, serviceContext);

		Folder folder = PortletFileRepositoryUtil.addPortletFolder(
			userId, repository.getRepositoryId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, checksum,
			serviceContext);

		return folder.getFolderId();
	}

	@Override
	public void publishStagingRequest(
			long userId, long groupId, long stagingRequestId,
			boolean privateLayout, Map<String, String[]> parameterMap)
		throws PortalException, SystemException {

		File file = checkStagingRequestFile(userId, stagingRequestId);

		layoutLocalService.importLayouts(
			userId, groupId, privateLayout, parameterMap, file);
	}

	@Override
	public void updateStagingRequest(
			long userId, long stagingRequestId, String fileName, byte[] bytes)
		throws PortalException, SystemException {

		Folder folder = PortletFileRepositoryUtil.getPortletFolder(
			stagingRequestId);

		fileName += PortletFileRepositoryUtil.getPortletFileEntriesCount(
			folder.getGroupId(), folder.getFolderId());

		PortletFileRepositoryUtil.addPortletFileEntry(
			folder.getGroupId(), userId, Group.class.getName(),
			folder.getGroupId(), PortletKeys.SITES_ADMIN, folder.getFolderId(),
			new UnsyncByteArrayInputStream(bytes), fileName,
			ContentTypes.APPLICATION_ZIP, false);
	}

	@Override
	public MissingReferences validateStagingRequest(
			long userId, long groupId, long stagingRequestId,
			boolean privateLayout, Map<String, String[]> parameterMap)
		throws PortalException, SystemException {

		File file = checkStagingRequestFile(userId, stagingRequestId);

		return layoutLocalService.validateImportLayoutsFile(
			userId, groupId, privateLayout, parameterMap, file);
	}

	protected File checkStagingRequestFile(long userId, long stagingRequestId)
		throws PortalException, SystemException {

		Folder folder = PortletFileRepositoryUtil.getPortletFolder(
			stagingRequestId);

		File file = getStagingRequestFile(stagingRequestId, folder);

		if (file != null) {
			return file;
		}

		FileOutputStream fileOutputStream = null;

		try {
			file = FileUtil.createTempFile("lar");

			fileOutputStream = new FileOutputStream(file);

			List<FileEntry> fileEntries =
				PortletFileRepositoryUtil.getPortletFileEntries(
					folder.getGroupId(), folder.getFolderId());

			for (FileEntry fileEntry : fileEntries) {
				InputStream inputStream = fileEntry.getContentStream();

				try {
					StreamUtil.transfer(inputStream, fileOutputStream, false);
				}
				finally {
					StreamUtil.cleanUp(inputStream);

					PortletFileRepositoryUtil.deletePortletFileEntry(
						fileEntry.getFileEntryId());
				}
			}

			String checksum = FileUtil.getMD5Checksum(file);

			if (!checksum.equals(folder.getName())) {
				throw new SystemException("Invalid checksum for LAR file");
			}

			storeStagingRequestFile(userId, stagingRequestId, folder, file);

			return getStagingRequestFile(stagingRequestId, folder);
		}
		catch (IOException ioe) {
			throw new SystemException("Unable to reassemble LAR file", ioe);
		}
		finally {
			StreamUtil.cleanUp(fileOutputStream);

			FileUtil.delete(file);
		}
	}

	protected File getStagingRequestFile(
		long stagingRequestId, Folder parentFolder) {

		try {
			FileEntry fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(
				parentFolder.getGroupId(), parentFolder.getFolderId(),
				_ASSEMBLED_LAR_PREFIX + String.valueOf(stagingRequestId));

			return DLStoreUtil.getFile(
				fileEntry.getCompanyId(), fileEntry.getRepositoryId(),
				fileEntry.getTitle());
		}
		catch (Exception e) {
			return null;
		}
	}

	protected void storeStagingRequestFile(
			long userId, long stagingRequestId, Folder parentFolder, File file)
		throws PortalException, SystemException {

		PortletFileRepositoryUtil.addPortletFileEntry(
			parentFolder.getGroupId(), userId, Group.class.getName(),
			parentFolder.getGroupId(), PortletKeys.SITES_ADMIN,
			parentFolder.getFolderId(), file,
			_ASSEMBLED_LAR_PREFIX + String.valueOf(stagingRequestId),
			ContentTypes.APPLICATION_ZIP, false);
	}

	private static final String _ASSEMBLED_LAR_PREFIX = "assembled_";

	private static Log _log = LogFactoryUtil.getLog(
		StagingLocalServiceImpl.class);

}
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

import com.liferay.portal.LARFileException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.security.auth.HttpPrincipal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.base.StagingLocalServiceBaseImpl;
import com.liferay.portal.service.http.StagingServiceHttp;
import com.liferay.portal.util.PropsValues;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

/**
 * The implementation of the staging local service.
 *
 * @author Brian Wing Shun Chan
 * @author Mate Thurzo
 * @see    com.liferay.portal.service.base.StagingLocalServiceBaseImpl
 * @see    com.liferay.portal.service.StagingLocalServiceUtil
 */
public class StagingLocalServiceImpl extends StagingLocalServiceBaseImpl {

	@Override
	public String getLarChecksum(File file) {
		try {
			return DigestUtils.md5Hex(FileUtil.getBytes(file));
		}
		catch (Exception e) {
			return StringPool.BLANK;
		}
	}

	@Override
	public void importTransferredLayouts(
			long userId, long groupId, boolean privateLayout,
			Map<String, String[]> parameterMap, String fileName)
		throws PortalException, SystemException {

		File file = getTransferredLayoutsFileDescriptor(fileName);

		try {
			layoutLocalService.importLayouts(
				userId, groupId, privateLayout, parameterMap, file);
		}
		finally {
			FileUtil.delete(file);
		}
	}

	@Override
	public void receiveLayouts(String fileName, byte[] bytes)
		throws PortalException, SystemException {

		File transferredFile = getTransferredLayoutsFileDescriptor(fileName);

		try {
			FileUtil.write(transferredFile, bytes, true);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public void transferLayouts(File file, ServiceContext serviceContext)
		throws PortalException, SystemException {

		HttpPrincipal httpPrincipal =
			(HttpPrincipal)serviceContext.getAttribute("httpPrincipal");
		long groupId = serviceContext.getScopeGroupId();

		InputStream inputStream = null;

		try {
			int bufferSize = PropsValues.STAGING_REMOTE_TRANSFER_BUFFER_SIZE;

			byte[] bytes = new byte[bufferSize];

			inputStream = new FileInputStream(file);

			int readBytes = inputStream.read(bytes);

			while (readBytes > 0) {
				byte[] bytesToSend = bytes;

				if (readBytes < bufferSize) {
					bytesToSend = new byte[readBytes];
					System.arraycopy(bytes, 0, bytesToSend, 0, readBytes);
				}

				StagingServiceHttp.receiveLayouts(
					httpPrincipal, groupId, file.getName(), bytesToSend);

				readBytes = inputStream.read(bytes);
			}
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	@Override
	public void validateTransferredLayouts(String fileName, String checksum)
		throws PortalException, SystemException {

		File transferredFile = getTransferredLayoutsFileDescriptor(fileName);

		if (!transferredFile.exists()) {
			throw new LARFileException("Transferred LAR file does not exists");
		}

		String transferredFileChecksum = getLarChecksum(transferredFile);

		if (!transferredFileChecksum.equals(checksum)) {
			FileUtil.delete(transferredFile);

			throw new LARFileException(
				"Transferred LAR's checksum doesn't match with source" +
					" checksum");
		}
	}

	protected File getTransferredLayoutsFileDescriptor(String fileName) {
		StringBundler sb = new StringBundler(4);

		sb.append(SystemProperties.get(SystemProperties.TMP_DIR));
		sb.append(File.separator);
		sb.append("remote_publish");
		sb.append(File.separator);

		File tempDirectory = new File(sb.toString());

		if (!tempDirectory.exists()) {
			tempDirectory.mkdir();
		}

		File file = new File(tempDirectory, fileName);

		return file;
	}

}
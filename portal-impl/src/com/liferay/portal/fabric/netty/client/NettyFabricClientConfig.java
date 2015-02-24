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

package com.liferay.portal.fabric.netty.client;

import com.liferay.portal.fabric.netty.fileserver.CompressionLevel;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.SystemProperties;

import java.io.File;
import java.io.Serializable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Shuyang Zhou
 */
public class NettyFabricClientConfig implements Serializable {

	public NettyFabricClientConfig(String id, Properties properties) {
		_id = id;
		_properties = properties;

		_repositoryFolder = new File(
			SystemProperties.get(SystemProperties.TMP_DIR),
			"NettyFabricClient-repository-" + id);

		String propertyValue = GetterUtil.getString(
			_properties.getProperty(
				PropsKeys.PORTAL_FABRIC_SERVER_INET_SOCKET_ADDRESSES),
				"localhost:8923");

		String[] fabricServerInetSocketAddresses = StringUtil.split(
			propertyValue, CharPool.COMMA);

		List<InetSocketAddress> inetSocketAddresses = new ArrayList<>();

		for (String fabricServerInetSocketAddress :
				fabricServerInetSocketAddresses) {

			fabricServerInetSocketAddress =
				fabricServerInetSocketAddress.trim();

			String[] parts = StringUtil.split(
				fabricServerInetSocketAddress, CharPool.COLON);

			if (parts.length != 2) {
				throw new IllegalStateException(
					"Unable to parse : " + propertyValue + ", " +
						fabricServerInetSocketAddress +
							" is not in form of \"hostname:port\"");
			}

			try {
				inetSocketAddresses.add(
					new InetSocketAddress(
						InetAddress.getByName(parts[0]),
						GetterUtil.getIntegerStrict(parts[1])));
			}
			catch (UnknownHostException uhe) {
				throw new IllegalArgumentException(
					"Unable to parse : " + propertyValue + ", " + parts[0] +
						" failed to resolve", uhe);
			}
			catch (NumberFormatException nfe) {
				throw new IllegalArgumentException(
					"Unable to parse : " + propertyValue + ", " + parts[1] +
						" can not be parsed to int value", nfe);
			}
			catch (IllegalArgumentException iae) {
				throw new IllegalArgumentException(
					"Unable to parse : " + propertyValue, iae);
			}
		}

		_fabricServerInetSocketAddresses = inetSocketAddresses;
	}

	public int getEventLoopGroupThreadCount() {
		return GetterUtil.getInteger(
			_properties.getProperty(
				PropsKeys.PORTAL_FABRIC_CLIENT_EVENT_LOOP_GROUP_THREAD_COUNT),
			1);
	}

	public int getExecutionGroupThreadCount() {
		return GetterUtil.getInteger(
			_properties.getProperty(
				PropsKeys.PORTAL_FABRIC_CLIENT_EXECUTION_GROUP_THREAD_COUNT),
			1);
	}

	public long getExecutionTimeout() {
		return GetterUtil.getLong(
			_properties.getProperty(
				PropsKeys.PORTAL_FABRIC_CLIENT_EXECUTION_TIMEOUT),
			600000);
	}

	public List<InetSocketAddress> getFabricServerInetSocketAddresses() {
		return _fabricServerInetSocketAddresses;
	}

	public CompressionLevel getFileServerFolderCompressionLevel() {
		return CompressionLevel.getCompressionLevel(
			GetterUtil.getInteger(
				_properties.getProperty(
					PropsKeys.PORTAL_FABRIC_CLIENT_FILE_SERVER_FOLDER_COMPRESSION_LEVEL),
				1));
	}

	public int getFileServerGroupThreadCount() {
		return GetterUtil.getInteger(
			_properties.getProperty(
				PropsKeys.PORTAL_FABRIC_CLIENT_FILE_SERVER_GROUP_THREAD_COUNT),
			1);
	}

	public int getReconnectCount() {
		int reconnectCount = GetterUtil.getInteger(
			_properties.getProperty(
				PropsKeys.PORTAL_FABRIC_CLIENT_RECONNECT_COUNT),
			3);

		if (reconnectCount < 0) {
			reconnectCount = Integer.MAX_VALUE;
		}

		return reconnectCount;
	}

	public long getReconnectInterval() {
		return GetterUtil.getLong(
			_properties.getProperty(
				PropsKeys.PORTAL_FABRIC_CLIENT_RECONNECT_INTERVAL),
			10000);
	}

	public long getRepositoryGetFileTimeout() {
		return GetterUtil.getLong(
			_properties.getProperty(
				PropsKeys.PORTAL_FABRIC_CLIENT_REPOSITORY_GET_FILE_TIMEOUT),
			600000);
	}

	public Path getRepositoryPath() {
		return _repositoryFolder.toPath();
	}

	public int getRPCGroupThreadCount() {
		return GetterUtil.getInteger(
			_properties.getProperty(
				PropsKeys.PORTAL_FABRIC_CLIENT_RPC_GROUP_THREAD_COUNT),
			1);
	}

	public long getShutdownQuietPeriod() {
		return GetterUtil.getLong(
			_properties.getProperty(
				PropsKeys.PORTAL_FABRIC_SHUTDOWN_QUIET_PERIOD),
			0);
	}

	public long getShutdownTimeout() {
		return GetterUtil.getLong(
			_properties.getProperty(PropsKeys.PORTAL_FABRIC_SHUTDOWN_TIMEOUT),
			60000);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(27);

		sb.append("{eventLoopGroupThreadCount=");
		sb.append(getEventLoopGroupThreadCount());
		sb.append(", executionGroupThreadCount=");
		sb.append(getExecutionGroupThreadCount());
		sb.append(", executionTimeout=");
		sb.append(getExecutionTimeout());
		sb.append(", fileServerFolderCompressionLevel=");
		sb.append(getFileServerFolderCompressionLevel());
		sb.append(", fileServerGroupThreadCount=");
		sb.append(getFileServerGroupThreadCount());
		sb.append(", id=");
		sb.append(_id);
		sb.append(", reconnectCount=");
		sb.append(getReconnectCount());
		sb.append(", reconnectInterval=");
		sb.append(getReconnectInterval());
		sb.append(", repositoryGetFileTimeout=");
		sb.append(getRepositoryGetFileTimeout());
		sb.append(", repositoryPath=");
		sb.append(getRepositoryPath());
		sb.append(", rpcGroupThreadCount=");
		sb.append(getRPCGroupThreadCount());
		sb.append(", shutdownQuietPeriod=");
		sb.append(getShutdownQuietPeriod());
		sb.append(", shutdownTimeout=");
		sb.append(getShutdownTimeout());
		sb.append("}");

		return sb.toString();
	}

	private static final long serialVersionUID = 1L;

	private final List<InetSocketAddress> _fabricServerInetSocketAddresses;
	private final String _id;
	private final Properties _properties;
	private final File _repositoryFolder;

}
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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.cluster.BaseClusterResponseCallback;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.cluster.ClusterNodeResponse;
import com.liferay.portal.kernel.cluster.ClusterNodeResponses;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactoryUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Tina Tian
 * @author Shuyang Zhou
 * @author László Csontos
 */
public class ThreadUtil {

	public static Thread[] getThreads() {
		Thread currentThread = Thread.currentThread();

		ThreadGroup threadGroup = currentThread.getThreadGroup();

		while (threadGroup.getParent() != null) {
			threadGroup = threadGroup.getParent();
		}

		int threadCountGuess = threadGroup.activeCount();

		Thread[] threads = new Thread[threadCountGuess];

		int threadCountActual = threadGroup.enumerate(threads);

		while (threadCountActual == threadCountGuess) {
			threadCountGuess *= 2;

			threads = new Thread[threadCountGuess];

			threadCountActual = threadGroup.enumerate(threads);
		}

		return threads;
	}

	public static ThreadDump threadDump() {
		String threadDump = _getThreadDumpFromJstack();

		if (Validator.isNull(threadDump)) {
			threadDump = _getThreadDumpFromStackTrace();
		}

		return new ThreadDump(threadDump);
	}

	public static void writeThreadDump(boolean clusterWide) {
		if (clusterWide) {
			ClusterRequest clusterRequest =
				ClusterRequest.createMulticastRequest(
					new MethodHandler(_threadDumpMethodKey), false);

			ClusterExecutorUtil.execute(
				clusterRequest, new ThreadDumpClusterResponseCallback(), 10,
				TimeUnit.SECONDS);

			if (_log.isInfoEnabled()) {
				_log.info(
					"Cluster wide thread dump request has been submitted.");
			}

			return;
		}

		ThreadDump threadDump = threadDump();

		File threadDumpFile = _getThreadDumpFile(
			threadDump.getTakenAt(), threadDump.getTargetHost(), false);

		try {
			FileUtil.write(threadDumpFile, threadDump.getThreadDump());

			if (_log.isInfoEnabled()) {
				_log.info("Thread dump has been written to " + threadDumpFile);
			}
		}
		catch (IOException ioe) {
			_log.error(ioe);
		}
}

	private static File _getThreadDumpFile(
		Date takenAt, String targetHost, boolean clusterWide) {

		int size = 5;

		if (Validator.isNotNull(targetHost)) {
			size = 7;
		}

		StringBundler sb = new StringBundler(size);

		sb.append("threadDump");

		if (takenAt == null) {
			takenAt = new Date();
		}

		sb.append(StringPool.DASH);
		sb.append(DateUtil.getISOFormat().format(takenAt));

		if (Validator.isNotNull(targetHost)) {
			sb.append(StringPool.DASH);
			sb.append(targetHost);
		}

		sb.append(StringPool.PERIOD);

		String extension = "txt";

		if (clusterWide) {
			extension = "zip";
		}

		sb.append(extension);

		File threadDumpFile = new File(_getThreadDumpDestDir(), sb.toString());

		return threadDumpFile;
	}

	private static String _getThreadDumpDestDir() {
		String destDir = PropsUtil.get(PropsKeys.THREAD_DUMP_DEST_DIR);

		if (Validator.isBlank(destDir)) {
			destDir = SystemProperties.get(SystemProperties.TMP_DIR);
		}

		if (!FileUtil.exists(destDir)) {
			FileUtil.mkdirs(destDir);
		}

		return destDir;
	}

	private static String _getThreadDumpFromJstack() {
		UnsyncByteArrayOutputStream outputStream =
			new UnsyncByteArrayOutputStream();

		try {
			String vendorURL = System.getProperty("java.vendor.url");

			if (!vendorURL.equals("http://java.oracle.com/") &&
				!vendorURL.equals("http://java.sun.com/")) {

				return StringPool.BLANK;
			}

			RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

			String name = runtimeMXBean.getName();

			if (Validator.isNull(name)) {
				return StringPool.BLANK;
			}

			int pos = name.indexOf(CharPool.AT);

			if (pos == -1) {
				return StringPool.BLANK;
			}

			String pidString = name.substring(0, pos);

			if (!Validator.isNumber(pidString)) {
				return StringPool.BLANK;
			}

			Runtime runtime = Runtime.getRuntime();

			int pid = GetterUtil.getInteger(pidString);

			String[] cmd = new String[] {"jstack", String.valueOf(pid)};

			Process process = runtime.exec(cmd);

			InputStream inputStream = process.getInputStream();

			StreamUtil.transfer(inputStream, outputStream);
		}
		catch (Exception e) {
		}

		return outputStream.toString();
	}

	private static String _getThreadDumpFromStackTrace() {
		String jvm =
			System.getProperty("java.vm.name") + " " +
				System.getProperty("java.vm.version");

		StringBundler sb = new StringBundler(
			"Full thread dump of " + jvm + " on " + String.valueOf(new Date()) +
				"\n\n");

		Map<Thread, StackTraceElement[]> stackTraces =
			Thread.getAllStackTraces();

		for (Map.Entry<Thread, StackTraceElement[]> entry :
				stackTraces.entrySet()) {

			Thread thread = entry.getKey();
			StackTraceElement[] elements = entry.getValue();

			sb.append(StringPool.QUOTE);
			sb.append(thread.getName());
			sb.append(StringPool.QUOTE);

			if (thread.getThreadGroup() != null) {
				sb.append(StringPool.SPACE);
				sb.append(StringPool.OPEN_PARENTHESIS);
				sb.append(thread.getThreadGroup().getName());
				sb.append(StringPool.CLOSE_PARENTHESIS);
			}

			sb.append(", priority=");
			sb.append(thread.getPriority());
			sb.append(", id=");
			sb.append(thread.getId());
			sb.append(", state=");
			sb.append(thread.getState());
			sb.append("\n");

			for (int i = 0; i < elements.length; i++) {
				sb.append("\t");
				sb.append(elements[i]);
				sb.append("\n");
			}

			sb.append("\n");
		}

		return sb.toString();
	}

	private static Log _log = LogFactoryUtil.getLog(ThreadUtil.class);

	private static MethodKey _threadDumpMethodKey = new MethodKey(
		ThreadUtil.class, "threadDump");

	private static class ThreadDumpClusterResponseCallback
		extends BaseClusterResponseCallback {

		@Override
		public void callback(ClusterNodeResponses clusterNodeResponses) {
			ZipWriter zipWriter = ZipWriterFactoryUtil.getZipWriter();

			for (
				ClusterNodeResponse clusterNodeResponse :
					clusterNodeResponses.getClusterResponses()) {

				ClusterNode clusterNode = clusterNodeResponse.getClusterNode();

				if (_log.isDebugEnabled()) {
					_log.debug("Processing response of node " + clusterNode);
				}

				try {
					ThreadDump threadDump =
						(ThreadDump)clusterNodeResponse.getResult();

					File threadDumpFile = _getThreadDumpFile(
						threadDump.getTakenAt(), threadDump.getTargetHost(),
						false);

					zipWriter.addEntry(
						StringPool.SLASH + threadDumpFile.getName(),
						threadDump.getThreadDump());
				}
				catch (Exception e) {
					_log.error("Exception occured on node " + clusterNode, e);
				}
			}

			boolean success = false;

			try {
				File threadDumpsFile = _getThreadDumpFile(null, null, true);

				success = FileUtil.move(zipWriter.getFile(), threadDumpsFile);

				if (_log.isInfoEnabled() && success) {
					_log.info(
						"Cluster wide thread dump has been written to " +
							threadDumpsFile);
				}
			}
			catch (Exception e) {
				success = false;

				if (_log.isDebugEnabled()) {
					_log.debug(e);
				}
			}

			if (!success) {
				_log.error("Cluster wide thread dump generation has failed.");
			}
		}

		@Override
		public void processTimeoutException(TimeoutException timeoutException) {
			if (_log.isWarnEnabled()) {
				_log.warn("Generating a cluster wide thread dump timed out.");
			}
		}
	}

}
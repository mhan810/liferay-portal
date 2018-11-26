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

package com.liferay.portal.search.engine.adapter.snapshot;

import aQute.bnd.annotation.ProviderType;

import com.liferay.petra.string.StringPool;

/**
 * @author Michael C. Han
 */
@ProviderType
public class RestoreSnapshotRequest
	implements SnapshotRequest<RestoreSnapshotResponse> {

	public RestoreSnapshotRequest() {
		_indexNames = StringPool.EMPTY_ARRAY;
	}

	public RestoreSnapshotRequest(String... indexNames) {
		_indexNames = indexNames;
	}

	@Override
	public RestoreSnapshotResponse accept(
		SnapshotRequestExecutor snapshotRequestExecutor) {

		return snapshotRequestExecutor.executeSnapshotRequest(this);
	}

	public String[] getIndexNames() {
		return _indexNames;
	}

	public boolean isIncludeAliases() {
		return _includeAliases;
	}

	public boolean isPartialRestore() {
		return _partialRestore;
	}

	public boolean isRestoreGlobalState() {
		return _restoreGlobalState;
	}

	public boolean isWaitForCompletion() {
		return _waitForCompletion;
	}

	public void setIncludeAliases(boolean includeAliases) {
		_includeAliases = includeAliases;
	}

	public void setPartialRestore(boolean partialRestore) {
		_partialRestore = partialRestore;
	}

	public void setRestoreGlobalState(boolean restoreGlobalState) {
		_restoreGlobalState = restoreGlobalState;
	}

	public void setWaitForCompletion(boolean waitForCompletion) {
		_waitForCompletion = waitForCompletion;
	}

	private boolean _includeAliases = true;
	private final String[] _indexNames;
	private boolean _partialRestore = true;
	private boolean _restoreGlobalState = true;
	private boolean _waitForCompletion = true;

}
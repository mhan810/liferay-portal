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

package com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.snapshot;

import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.engine.adapter.snapshot.SnapshotRequestExecutor;

/**
 * @author Michael C. Han
 */
public class SnapshotRequestExecutorFixture {

	public SnapshotRequestExecutorFixture(
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		_elasticsearchConnectionManager = elasticsearchConnectionManager;
	}

	public SnapshotRequestExecutor createExecutor() {
		return new ElasticsearchSnapshotRequestExecutor() {
			{

				createSnapshotRepositoryRequestExecutor =
					createCreateSnapshotRepositoryRequestExecutor();
				createSnapshotRequestExecutor =
					createCreateSnapshotRequestExecutor();
				deleteSnapshotRequestExecutor =
					createDeleteSnapshotRequestExecutor();
				getSnapshotRepositoriesRequestExecutor =
					createGetSnapshotRepositoriesRequestExecutor();
				getSnapshotsRequestExecutor =
					createGetSnapshotsRequestExecutor();
				restoreSnapshotRequestExecutor =
					createRestoreSnapshotRequestExecutor();
			}
		};
	}

	protected CreateSnapshotRepositoryRequestExecutor
		createCreateSnapshotRepositoryRequestExecutor() {

		return new CreateSnapshotRepositoryRequestExecutorImpl() {
			{
				elasticsearchConnectionManager =
					_elasticsearchConnectionManager;
			}
		};
	}

	protected CreateSnapshotRequestExecutor
		createCreateSnapshotRequestExecutor() {

		return new CreateSnapshotRequestExecutorImpl() {
			{
				elasticsearchConnectionManager =
					_elasticsearchConnectionManager;
			}
		};
	}

	protected DeleteSnapshotRequestExecutor
		createDeleteSnapshotRequestExecutor() {

		return new DeleteSnapshotRequestExecutorImpl() {
			{
				elasticsearchConnectionManager =
					_elasticsearchConnectionManager;
			}
		};
	}

	protected GetSnapshotRepositoriesRequestExecutor
		createGetSnapshotRepositoriesRequestExecutor() {

		return new GetSnapshotRepositoriesRequestExecutorImpl() {
			{
				elasticsearchConnectionManager =
					_elasticsearchConnectionManager;
			}
		};
	}

	protected GetSnapshotsRequestExecutor createGetSnapshotsRequestExecutor() {
		return new GetSnapshotsRequestExecutorImpl() {
			{
				elasticsearchConnectionManager =
					_elasticsearchConnectionManager;
			}
		};
	}

	protected RestoreSnapshotRequestExecutor
		createRestoreSnapshotRequestExecutor() {

		return new RestoreSnapshotRequestExecutorImpl() {
			{
				elasticsearchConnectionManager =
					_elasticsearchConnectionManager;
			}
		};
	}

	private final ElasticsearchConnectionManager
		_elasticsearchConnectionManager;

}
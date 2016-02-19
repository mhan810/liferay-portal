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

package com.liferay.portal.search.elasticsearch.internal.cluster;

import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.cluster.ClusterMasterExecutor;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.search.elasticsearch.connection.ElasticsearchConnection;
import com.liferay.portal.search.elasticsearch.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch.connection.OperationMode;

import java.util.List;

import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 */
@Component(immediate = true, service = ElasticsearchCluster.class)
public class ElasticsearchCluster {

	@Activate
	protected void activate() {
		_replicasClusterListener = new ReplicasClusterListener(
			new ReplicasClusterContextImpl());

		clusterExecutor.addClusterEventListener(_replicasClusterListener);

		clusterMasterExecutor.addClusterMasterTokenTransitionListener(
			_replicasClusterListener);
	}

	@Deactivate
	protected void deactivate() {
		clusterExecutor.removeClusterEventListener(_replicasClusterListener);

		clusterMasterExecutor.removeClusterMasterTokenTransitionListener(
			_replicasClusterListener);

		_replicasClusterListener = null;
	}

	@Reference(unbind = "-")
	protected void setCompanyLocalService(
			CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;
	}

	protected class ReplicasClusterContextImpl
		implements ReplicasClusterContext {

		@Override
		public int getClusterSize() {
			List<ClusterNode> clusterNodes = clusterExecutor.getClusterNodes();

			return clusterNodes.size();
		}

		@Override
		public ReplicasManager getReplicasManager() {
			ElasticsearchConnection elasticsearchConnection =
				getActiveElasticsearchConnection();

			Client client = elasticsearchConnection.getClient();

			AdminClient adminClient = client.admin();

			return new ReplicasManagerImpl(adminClient.indices());
		}

		@Override
		public String[] getTargetIndexNames() {
			List<Company> companies = _companyLocalService.getCompanies();

			String[] targetIndexNames = new String[companies.size() + 1];

			for (int i = 0; i < targetIndexNames.length - 1; i++) {
				Company company = companies.get(i);

				targetIndexNames[i] = String.valueOf(company.getCompanyId());
			}

			targetIndexNames[targetIndexNames.length - 1] =
				CompanyConstants.SYSTEM_STRING;

			return targetIndexNames;
		}

		@Override
		public boolean isEmbeddedOperationMode() {
			ElasticsearchConnection elasticsearchConnection =
				getActiveElasticsearchConnection();

			OperationMode operationMode =
				elasticsearchConnection.getOperationMode();

			return (operationMode == OperationMode.EMBEDDED);
		}

		@Override
		public boolean isMaster() {
			return clusterMasterExecutor.isMaster();
		}

		protected ElasticsearchConnection getActiveElasticsearchConnection() {
			return elasticsearchConnectionManager.getElasticsearchConnection();
		}

	}

	@Reference
	protected ClusterExecutor clusterExecutor;

	@Reference
	protected ClusterMasterExecutor clusterMasterExecutor;

	private CompanyLocalService _companyLocalService;

	@Reference
	protected ElasticsearchConnectionManager elasticsearchConnectionManager;

	private ReplicasClusterListener _replicasClusterListener;

}
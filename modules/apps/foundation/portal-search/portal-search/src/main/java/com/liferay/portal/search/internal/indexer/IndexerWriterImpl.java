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

package com.liferay.portal.search.internal.indexer;

import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.search.contributor.model.IndexerWriterMode;
import com.liferay.portal.search.contributor.model.ModelIndexerWriterContributor;
import com.liferay.portal.search.contributor.model.ModelSearchSettings;
import com.liferay.portal.search.index.IndexStatusManager;
import com.liferay.portal.search.indexer.IndexerDocumentBuilder;
import com.liferay.portal.search.indexer.IndexerWriter;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Michael C. Han
 */
public class IndexerWriterImpl<T extends BaseModel>
	implements IndexerWriter<T> {

	public IndexerWriterImpl(
		ModelSearchSettings modelSearchSettings,
		ModelIndexerWriterContributor<T> modelIndexerWriterContributor,
		IndexerDocumentBuilder<T> indexerDocumentBuilder,
		IndexStatusManager indexStatusManager,
		IndexWriterHelper indexWriterHelper, Props props) {

		_modelSearchSettings = modelSearchSettings;
		_modelIndexerWriterContributor = modelIndexerWriterContributor;
		_indexerDocumentBuilder = indexerDocumentBuilder;
		_indexStatusManager = indexStatusManager;
		_indexWriterHelper = indexWriterHelper;
		_props = props;
	}

	@Override
	public void delete(long companyId, String uid) throws SearchException {
		if (!isEnabled()) {
			return;
		}

		try {
			_indexWriterHelper.deleteDocument(
				_modelSearchSettings.getSearchEngineId(), companyId, uid,
				_modelSearchSettings.isCommitImmediately());
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public void delete(T baseModel) throws SearchException {
		if (baseModel == null) {
			return;
		}

		long companyId = _modelIndexerWriterContributor.getCompanyId(baseModel);

		String uid = _indexerDocumentBuilder.getDocumentUID(baseModel);

		delete(companyId, uid);
	}

	@Override
	public boolean isEnabled() {
		//todo NEED TO FIND A MORE ELEGANT SOLUTION

		if (_indexerEnabled == null) {
			String indexerEnabled = _props.get(
				PropsKeys.INDEXER_ENABLED,
				new com.liferay.portal.kernel.configuration.Filter(
					_modelSearchSettings.getClassName()));

			_indexerEnabled = GetterUtil.getBoolean(indexerEnabled, true);

			return _indexerEnabled;
		}

		if (_indexStatusManager.isIndexReadOnly() ||
			_indexStatusManager.isIndexReadOnly(
				_modelSearchSettings.getClassName()) ||
			!_indexerEnabled) {

			return false;
		}

		return true;
	}

	@Override
	public void reindex(Collection<T> baseModels) throws SearchException {
		if (!isEnabled()) {
			return;
		}

		if ((baseModels == null) || baseModels.isEmpty()) {
			return;
		}

		for (T baseModel : baseModels) {
			try {
				reindex(baseModel);
			}
			catch (SearchException se) {
				_log.error("Error indexing: " + baseModel, se);
			}
		}
	}

	@Override
	public void reindex(long classPK) throws SearchException {
		if (!isEnabled()) {
			return;
		}

		if (classPK <= 0) {
			return;
		}

		try {
			Optional<T> baseModelOptional =
				_modelIndexerWriterContributor.getBaseModel(classPK);

			if (!baseModelOptional.isPresent()) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"No entity found: " +
							_modelSearchSettings.getClassName() + "-" +
								classPK);
				}

				return;
			}

			T baseModel = baseModelOptional.get();

			reindex(baseModel);
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public void reindex(String[] ids) throws SearchException {
		if (!isEnabled()) {
			return;
		}

		if (ArrayUtil.isEmpty(ids)) {
			return;
		}

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			_modelIndexerWriterContributor.getIndexableActionableDynamicQuery();

		long companyThreadLocalCompanyId = CompanyThreadLocal.getCompanyId();

		try {
			for (String id : ids) {
				long companyId = GetterUtil.getLong(id);

				CompanyThreadLocal.setCompanyId(companyId);

				indexableActionableDynamicQuery.setCompanyId(companyId);

				indexableActionableDynamicQuery.setSearchEngineId(
					_modelSearchSettings.getSearchEngineId());

				_modelIndexerWriterContributor.customize(
					indexableActionableDynamicQuery, _indexerDocumentBuilder,
					companyId);

				try {
					indexableActionableDynamicQuery.performActions();
				}
				catch (PortalException pe) {
					if (_log.isWarnEnabled()) {
						StringBundler sb = new StringBundler(4);

						sb.append("Error reindexing all ");
						sb.append(_modelSearchSettings.getClassName());
						sb.append(" for company: ");
						sb.append(companyId);

						_log.warn(sb.toString(), pe);
					}
				}
			}
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
		finally {
			CompanyThreadLocal.setCompanyId(companyThreadLocalCompanyId);
		}
	}

	@Override
	public void reindex(T baseModel) throws SearchException {
		if (!isEnabled()) {
			return;
		}

		if (baseModel == null) {
			return;
		}

		try {
			IndexerWriterMode indexerWriterMode =
				_modelIndexerWriterContributor.getIndexerWriterMode(baseModel);

			if ((indexerWriterMode == IndexerWriterMode.UPDATE) ||
				(indexerWriterMode == IndexerWriterMode.PARTIAL_UPDATE)) {

				Document document = _indexerDocumentBuilder.getDocument(
					baseModel);

				_indexWriterHelper.updateDocument(
					_modelSearchSettings.getSearchEngineId(),
					_modelIndexerWriterContributor.getCompanyId(baseModel),
					document, _modelSearchSettings.isCommitImmediately());
			}
			else if (indexerWriterMode == IndexerWriterMode.DELETE) {
				delete(baseModel);
			}
			else if (indexerWriterMode == IndexerWriterMode.SKIP) {
				if (_log.isDebugEnabled()) {
					_log.debug("Skipping model: " + baseModel);
				}
			}

			_modelIndexerWriterContributor.modelIndexed(baseModel);
		}
		catch (SearchException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		_indexerEnabled = enabled;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IndexerWriterImpl.class);

	private final IndexerDocumentBuilder<T> _indexerDocumentBuilder;
	private Boolean _indexerEnabled;
	private final IndexStatusManager _indexStatusManager;
	private final IndexWriterHelper _indexWriterHelper;
	private final ModelIndexerWriterContributor<T>
		_modelIndexerWriterContributor;
	private final ModelSearchSettings _modelSearchSettings;
	private final Props _props;

}
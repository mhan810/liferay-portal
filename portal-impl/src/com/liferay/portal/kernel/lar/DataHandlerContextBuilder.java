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

package com.liferay.portal.kernel.lar;

import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipReaderFactoryUtil;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactoryUtil;
import com.liferay.portal.lar.AlwaysCurrentUserIdStrategy;
import com.liferay.portal.lar.CurrentUserIdStrategy;
import com.liferay.portal.model.User;

import java.io.File;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mate Thurzo
 */
public class DataHandlerContextBuilder {

	public DataHandlerContextBuilder(long companyId, boolean export) {
		_companyId = companyId;
		_export = export;
	}

	public DataHandlerContext build() {
		try {
			validateDateRange(_startDate, _endDate);

			DataHandlerContext context = new DataHandlerContextImpl(_companyId);

			context.setEndDate(_endDate);
			context.setGroupId(_groupId);
			context.setScopeGroupId(_scopeGroupId);
			context.setSourceGroupId(_sourceGroupId);
			context.setParameters(_paramaters);
			context.setPrivateLayout(_privateLayout);
			context.setStartDate(_startDate);
			context.setUser(_user);

			// Data Strategy

			String dataStrategy = MapUtil.getString(
				_paramaters, PortletDataHandlerKeys. DATA_STRATEGY,
				PortletDataHandlerKeys.DATA_STRATEGY_MIRROR);

			context.setDataStrategy(dataStrategy);

			// User Id Strategy

			String userIdStrategy = MapUtil.getString(
				_paramaters, PortletDataHandlerKeys.USER_ID_STRATEGY);

			UserIdStrategy strategy = null;

			if (UserIdStrategy.ALWAYS_CURRENT_USER_ID.equals(userIdStrategy)) {
				strategy = new AlwaysCurrentUserIdStrategy(_user);
			}
			else {
				strategy = new CurrentUserIdStrategy(_user);
			}

			context.setUserIdStrategy(strategy);

			// ZipReader & ZipWriter

			if (_export && (_zipWriter == null)) {
				_zipWriter = ZipWriterFactoryUtil.getZipWriter();

				context.setZipWriter(_zipWriter);
			}
			else if (!_export && (_zipReader == null)) {
				_zipReader = ZipReaderFactoryUtil.getZipReader(_zipFile);

				context.setZipReader(_zipReader);
			}

			return context;
		}
		catch (Exception e) {
			return null;
		}
	}

	public DataHandlerContextBuilder setAttributes(
		Map<String, Object> attributes) {

		_attributes = attributes;

		return this;
	}

	public DataHandlerContextBuilder setEndDate(Date endDate) {
		_endDate = endDate;

		return this;
	}

	public DataHandlerContextBuilder setGroupId(long groupId) {
		_groupId = groupId;

		if (_scopeGroupId <= 0) {
			_scopeGroupId = groupId;
		}

		if (_sourceGroupId <= 0) {
			_sourceGroupId = groupId;
		}

		return this;
	}

	public DataHandlerContextBuilder setParameters(
		Map<String, String[]> parameters) {

		_paramaters = parameters;

		return this;
	}

	public DataHandlerContextBuilder setPrivateLayout(boolean privateLayout) {
		_privateLayout = privateLayout;

		return this;
	}

	public DataHandlerContextBuilder setScopeGroupId(long scopeGroupId) {
		_scopeGroupId = scopeGroupId;

		return this;
	}

	public DataHandlerContextBuilder setStartDate(Date startDate) {
		_startDate = startDate;

		return this;
	}

	public DataHandlerContextBuilder setUser(User user) {
		_user = user;

		return this;
	}

	public DataHandlerContextBuilder setZipFile(File zipFile) {
		_zipFile = zipFile;

		return this;
	}

	public DataHandlerContextBuilder setZipReader(ZipReader zipReader) {
		_zipReader = zipReader;

		return this;
	}

	public DataHandlerContextBuilder setZipWriter(ZipWriter zipWriter) {
		_zipWriter = zipWriter;

		return this;
	}

	protected void validateDateRange(Date startDate, Date endDate)
		throws com.liferay.portal.kernel.lar.PortletDataException {

		if ((startDate == null) && (endDate != null)) {
			throw new PortletDataException(
				PortletDataException.END_DATE_IS_MISSING_START_DATE);
		}
		else if ((startDate != null) && (endDate == null)) {
			throw new PortletDataException(
				PortletDataException.START_DATE_IS_MISSING_END_DATE);
		}

		if (startDate != null) {
			if (startDate.after(endDate) || startDate.equals(endDate)) {
				throw new PortletDataException(
					PortletDataException.START_DATE_AFTER_END_DATE);
			}

			Date now = new Date();

			if (startDate.after(now)) {
				throw new PortletDataException(
					PortletDataException.FUTURE_START_DATE);
			}

			if (endDate.after(now)) {
				throw new PortletDataException(
					PortletDataException.FUTURE_END_DATE);
			}
		}
	}

	private Map<String, Object> _attributes = new HashMap<String, Object>();
	private long _companyId;
	private Date _endDate;
	private boolean _export;
	private long _groupId;
	private Map<String, Map<?, ?>> _newPrimaryKeysMaps =
		new HashMap<String, Map<?, ?>>();
	private Set<String> _notUniquePerLayout = new HashSet<String>();
	private Map<String, String[]> _paramaters;
	private Set<String> _primaryKeys;
	private boolean _privateLayout;
	private long _scopeGroupId;
	private long _sourceGroupId;
	private Date _startDate;
	private Set<String> _storedPaths = new HashSet<String>();
	private User _user;
	private File _zipFile;
	private ZipReader _zipReader;
	private ZipWriter _zipWriter;

}
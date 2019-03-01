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

package com.liferay.digital.signature.internal.model;

import com.liferay.digital.signature.model.DSSealInfo;
import com.liferay.digital.signature.model.DSSealProviderOptions;

import java.util.Objects;

/**
 * @author Michael C. Han
 */
public class DSSealInfoImpl implements DSSealInfo {

	public DSSealInfoImpl(String sealName, String sealProviderName) {
		this(sealName, sealProviderName, false);
	}

	public DSSealInfoImpl(
		String sealName, String sealProviderName,
		Boolean sealDocumentsWithFieldsOnly) {

		_sealName = sealName;
		_sealProviderName = sealProviderName;
		_sealDocumentsWithFieldsOnly = sealDocumentsWithFieldsOnly;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if ((object == null) || (getClass() != object.getClass())) {
			return false;
		}

		final DSSealInfoImpl dsSealInfoImpl = (DSSealInfoImpl)object;

		return Objects.equals(_sealName, dsSealInfoImpl._sealName);
	}

	@Override
	public DSSealProviderOptions getDSSealProviderOptions() {
		return _dsSealProviderOptions;
	}

	@Override
	public Boolean getSealDocumentsWithFieldsOnly() {
		return _sealDocumentsWithFieldsOnly;
	}

	@Override
	public String getSealName() {
		return _sealName;
	}

	@Override
	public String getSealProviderName() {
		return _sealProviderName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_sealName);
	}

	public void setDSSealProviderOptions(
		DSSealProviderOptions dsSealProviderOptions) {

		_dsSealProviderOptions = dsSealProviderOptions;
	}

	private DSSealProviderOptions _dsSealProviderOptions;
	private final Boolean _sealDocumentsWithFieldsOnly;
	private final String _sealName;
	private final String _sealProviderName;

}
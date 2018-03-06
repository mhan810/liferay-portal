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

package com.liferay.portal.search.indexer;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.search.SearchException;

import java.util.Collection;

/**
 * @author Michael C. Han
 */
@ProviderType
public interface IndexerWriter<T> {

	public void delete(long companyId, String uid) throws SearchException;

	public void delete(T baseModel) throws SearchException;

	public boolean isEnabled();

	public void reindex(Collection<T> objects) throws SearchException;

	public void reindex(long classPK) throws SearchException;

	public void reindex(String[] ids) throws SearchException;

	public void reindex(T object) throws SearchException;

	public void setEnabled(boolean enabled);

}
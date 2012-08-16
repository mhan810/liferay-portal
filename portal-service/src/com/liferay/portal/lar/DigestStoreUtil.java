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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.lar.DataHandlerContext;
import com.liferay.portal.kernel.lar.digest.LarDigest;

/**
 * @author Mate Thurzo
 */
public class DigestStoreUtil {

	public static DigestStore createDigestStore() {
		if (_digestStoreFactory != null) {
			_digestStoreImpl = _digestStoreFactory.createDigestStore();

			return _digestStoreImpl;
		}

		return null;
	}

	public static DigestStore createDigestStore(
		DigestStoreFactory factory, DataHandlerContext context) {

		_digestStoreImpl = factory.createDigestStore();

		return _digestStoreImpl;
	}

	public static LarDigest getLarDigest() {
		return _digestStoreImpl.getDigest();
	}

	public void setDigestStoreFactory(DigestStoreFactory factory) {
		_digestStoreFactory = factory;
	}

	private static DigestStoreFactory _digestStoreFactory;
	private static DigestStore _digestStoreImpl;

}
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

package com.liferay.portal.search.internal;

import com.liferay.portal.kernel.search.Bufferable;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.model.ClassedModel;

import java.io.Serializable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Michael C. Han
 */
public class BufferedIndexerInvocationHandler implements InvocationHandler {

	public BufferedIndexerInvocationHandler(Indexer indexer) {
		_indexer = indexer;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {

		Annotation annotation = method.getAnnotation(Bufferable.class);

		IndexerRequestBuffer indexerRequestBuffer = IndexerRequestBuffer.get();

		if ((annotation == null) || (args.length != 1) ||
			(indexerRequestBuffer == null)) {

			return method.invoke(_indexer, args);
		}

		if (args[0] instanceof ClassedModel) {
			ClassedModel classedModel = (ClassedModel)args[0];

			Serializable primaryKeyObj = classedModel.getPrimaryKeyObj();

			if (!(primaryKeyObj instanceof Long)) {
				return method.invoke(_indexer, args);
			}

			IndexerRequest indexerRequest = new IndexerRequest(
				method, classedModel, _indexer);

			indexerRequestBuffer.add(indexerRequest);
		}

		Collection objects = null;

		if (args[0].getClass().isArray()) {
			objects = Arrays.asList(args[0]);
		}
		else if (args[0].getClass().equals(Collection.class)) {
			objects = (Collection)args[0];
		}
		else {
			return method.invoke(_indexer, args);
		}

		for (Object object : objects) {
			if (!(object instanceof ClassedModel)) {
				return method.invoke(_indexer, args);
			}

			ClassedModel classedModel = (ClassedModel)object;

			Serializable primaryKeyObj = classedModel.getPrimaryKeyObj();

			if (!(primaryKeyObj instanceof Long)) {
				return method.invoke(_indexer, args);
			}

			MethodKey methodKey = new MethodKey(
				Indexer.class, method.getName(), Object.class);

			IndexerRequest indexerRequest = new IndexerRequest(
				methodKey.getMethod(), classedModel, _indexer);

			indexerRequestBuffer.add(indexerRequest);
		}

		return null;
	}

	private final Indexer _indexer;

}
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

package com.liferay.portal.kernel.search;

import java.util.List;

/**
 * @author Raymond Augé
 */
public class IndexerRegistryUtil {

	public static Indexer getIndexer(Class<?> clazz) {
		return _indexerRegistry.getIndexer(clazz);
	}

	public static Indexer getIndexer(String className) {
		return _indexerRegistry.getIndexer(className);
	}

	public static List<Indexer> getIndexers() {
		return _indexerRegistry.getIndexers();
	}

	public static Indexer nullSafeGetIndexer(Class<?> clazz) {
		return _indexerRegistry.nullSafeGetIndexer(clazz);
	}

	public static Indexer nullSafeGetIndexer(String className) {
		return _indexerRegistry.nullSafeGetIndexer(className);
	}

	public static void register(Indexer indexer) {
		_indexerRegistry.register(indexer);
	}

	public static void register(String className, Indexer indexer) {
		_indexerRegistry.register(indexer);
	}

	public static void unregister(Indexer indexer) {
		_indexerRegistry.unregister(indexer);
	}

	public static void unregister(String className) {
		_indexerRegistry.unregister(className);
	}

	public void setIndexerRegistry(IndexerRegistry indexerRegistry) {
		_indexerRegistry = indexerRegistry;
	}

	private static IndexerRegistry _indexerRegistry;

}
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.ClassedModel;

import java.io.Serializable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mate Thurzo
 * @author Zsolt Berentey
 */
public class ManifestSummary implements Serializable {

	public void addDeletionCount(
		Class<? extends ClassedModel> clazz, long count) {

		addDeletionCount(clazz.getName(), count);
	}

	public void addDeletionCount(String modelName, long count) {
		_deletionCounters.put(modelName, count);

		_modelNames.add(modelName);
	}

	public void addModelCount(Class<? extends ClassedModel> clazz, long count) {
		addModelCount(clazz.getName(), count);
	}

	public void addModelCount(String modelName, long count) {
		_modelCounters.put(modelName, count);

		_modelNames.add(modelName);
	}

	public long getDeletionCount(Class<? extends ClassedModel> clazz) {
		return getDeletionCount(clazz.getName());
	}

	public long getDeletionCount(String modelName) {
		if (!_deletionCounters.containsKey(modelName)) {
			return -1;
		}

		return _deletionCounters.get(modelName);
	}

	public Map<String, Long> getDeletionCounters() {
		return _deletionCounters;
	}

	public long getModelCount(Class<? extends ClassedModel> clazz) {
		return getModelCount(clazz.getName());
	}

	public long getModelCount(String modelName) {
		if (!_modelCounters.containsKey(modelName)) {
			return -1;
		}

		return _modelCounters.get(modelName);
	}

	public Map<String, Long> getModelCounters() {
		return _modelCounters;
	}

	public Collection<String> getModelNames() {
		return _modelNames;
	}

	public void incrementDeletionCount(Class<? extends ClassedModel> clazz) {
		incrementDeletionCount(clazz.getName());
	}

	public void incrementDeletionCount(String modelName) {
		if (!_deletionCounters.containsKey(modelName)) {
			_deletionCounters.put(modelName, 1L);

			_modelNames.add(modelName);

			return;
		}

		long deletionCounter = _deletionCounters.get(modelName);

		_deletionCounters.put(modelName, deletionCounter + 1);
	}

	public void incrementModelCount(Class<? extends ClassedModel> clazz) {
		incrementModelCount(clazz.getName());
	}

	public void incrementModelCount(String modelName) {
		if (!_modelCounters.containsKey(modelName)) {
			_modelCounters.put(modelName, 1L);

			_modelNames.add(modelName);

			return;
		}

		long modelCounter = _modelCounters.get(modelName);

		_modelCounters.put(modelName, modelCounter + 1);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(3);

		sb.append("{modelCounters=");
		sb.append(MapUtil.toString(_modelCounters));
		sb.append(",{deletionCounters=");
		sb.append(MapUtil.toString(_deletionCounters));
		sb.append("}");

		return sb.toString();
	}

	private Map<String, Long> _deletionCounters = new HashMap<String, Long>();
	private Map<String, Long> _modelCounters = new HashMap<String, Long>();
	private Set<String> _modelNames = new HashSet<String>();

}
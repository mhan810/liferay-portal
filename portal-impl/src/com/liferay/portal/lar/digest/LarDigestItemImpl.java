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

package com.liferay.portal.lar.digest;

import org.mockito.internal.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class LarDigestItemImpl implements LarDigestItem {

	public void addDependency(LarDigestDependency dependency) {
		_dependencies.add(dependency);
	}

	public void addMetadata(LarDigestMetadata metadata) {
		_metadata.add(metadata);
	}

	public void addPermission(LarDigestPermission permission) {
		_permissions.add(permission);
	}

	public int getAction() {
		return _action;
	}

	public List<LarDigestDependency> getDependencies() {
		return _dependencies;
	}

	public List<LarDigestMetadata> getMetadata() {
		return _metadata;
	}

	public List<LarDigestPermission> getPermissions() {
		return _permissions;
	}

	public String getClassPK() {
		return _classPK;
	}

	public List<LarDigestDependency> getDependencies(String className) {
		List<LarDigestDependency> result = new ArrayList<LarDigestDependency>();

		for (LarDigestDependency dependency : _dependencies) {
			String itemClassName = dependency.getClassName();

			if(itemClassName.equals(className)) {
				result.add(dependency);
			}
		}

		return result;
	}

	public List<LarDigestMetadata> getMetadata(String name) {
		List<LarDigestMetadata> result = new ArrayList<LarDigestMetadata>();

		for (LarDigestMetadata metadata : _metadata) {
			String metadataName = metadata.getName();

			if(metadataName.equals(name)) {
				result.add(metadata);
			}
		}

		return result;
	}

	public String getMetadataValue(String name) {
		List<LarDigestMetadata> result = getMetadata(name);

		if (result.isEmpty()) {
			return null;
		}

		return result.get(0).getValue();
	}

	public String getPath() {
		return _path;
	}

	public String getType() {
		return _type;
	}

	public void setAction(int action) {
		_action = action;
	}

	public void setClassPK(String classPK) {
		_classPK = classPK;
	}

	public void setPath(String path) {
		_path = path;
	}

	public void setPermissions(List<LarDigestPermission> permissions) {
		_permissions = permissions;
	}

	public void setType(String type) {
		_type = type;
	}

	private int _action;
	private String _classPK;
	private List<LarDigestDependency> _dependencies =
		new ArrayList<LarDigestDependency>();
	private List<LarDigestMetadata> _metadata =
		new ArrayList<LarDigestMetadata>();
	private String _path;
	private List<LarDigestPermission> _permissions =
		new ArrayList<LarDigestPermission>();
	private String _type;

}
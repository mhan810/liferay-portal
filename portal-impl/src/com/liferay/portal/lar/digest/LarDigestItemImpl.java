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

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

/**
 * @author Mate Thurzo
 */
public class LarDigestItemImpl implements LarDigestItem {

	public LarDigestItemImpl() {
	}

	public LarDigestItemImpl(Element root) {
		Element metadataRootEl = root.element(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		List<Element> metadatasEl = metadataRootEl.elements(
			LarDigesterConstants.NODE_METADATA_LABEL);

		for (Element metadataEl : metadatasEl) {
			addMetadata(new LarDigestMetadataImpl(metadataEl));
		}

		Element permissionsRootEl = root.element(
			LarDigesterConstants.NODE_PERMISSIONS_LABEL);

		List<Element> permissionsEl = permissionsRootEl.elements(
			LarDigesterConstants.NODE_PERMISSION_LABEL);

		for (Element permissionEl : permissionsEl) {
			addPermission(new LarDigestPermissionImpl(permissionEl));
		}

		Element element = root.element(LarDigesterConstants.NODE_ACTION_LABEL);
		_action = (GetterUtil.getInteger(element.getText()));

		element = root.element(LarDigesterConstants.NODE_CLASS_PK_LABEL);
		_classPK = element.getText();

		element = root.element(LarDigesterConstants.NODE_PATH_LABEL);
		_path = element.getText();

		element = root.element(LarDigesterConstants.NODE_TYPE_LABEL);
		_type = element.getText();

		Element dependenciesRootEl = root.element(
			LarDigesterConstants.NODE_DEPENDENCIES_LABEL);

		List<Element> dependenciesEl = dependenciesRootEl.elements(
			LarDigesterConstants.NODE_DEPENDENCY_LABEL);

		for (Element dependencyEl : dependenciesEl) {
			addDependency(new LarDigestDependencyImpl(dependencyEl));
		}
	}

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

	public String getClassPK() {
		return _classPK;
	}

	public List<LarDigestDependency> getDependencies() {
		return _dependencies;
	}

	public List<LarDigestDependency> getDependencies(String className) {
		List<LarDigestDependency> result = new ArrayList<LarDigestDependency>();

		for (LarDigestDependency dependency : _dependencies) {
			String itemClassName = dependency.getClassPK();

			if (itemClassName.equals(className)) {
				result.add(dependency);
			}
		}

		return result;
	}

	public List<LarDigestMetadata> getMetadata() {
		return _metadata;
	}

	public List<LarDigestMetadata> getMetadata(String name) {
		List<LarDigestMetadata> result = new ArrayList<LarDigestMetadata>();

		for (LarDigestMetadata metadata : _metadata) {
			String metadataName = metadata.getName();

			if (metadataName.equals(name)) {
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

	public List<LarDigestPermission> getPermissions() {
		return _permissions;
	}

	public String getType() {
		return _type;
	}

	public String getUuid() {
		return _uuid;
	}

	public void serialize(XMLStreamWriter writer) throws Exception {
		writer.writeStartElement(LarDigesterConstants.NODE_ITEM_LABEL);

		// metadata
		writer.writeStartElement(LarDigesterConstants.NODE_METADATA_SET_LABEL);

		for (LarDigestMetadata metadata : _metadata) {
			metadata.serialize(writer);
		}

		writer.writeEndElement();

		// dependencies

		writer.writeStartElement(LarDigesterConstants.NODE_DEPENDENCIES_LABEL);

		for (LarDigestDependency dependency : _dependencies) {
			dependency.serialize(writer);
		}

		writer.writeEndElement();

		// item fields

		writer.writeStartElement(LarDigesterConstants.NODE_PATH_LABEL);
		if (Validator.isNotNull(_path)) {
			writer.writeCharacters(_path);
		}

		writer.writeEndElement();

		writer.writeStartElement(LarDigesterConstants.NODE_ACTION_LABEL);
		if (_action > 0) {
			writer.writeCharacters(StringUtil.valueOf(_action));
		}

		writer.writeEndElement();

		writer.writeStartElement(LarDigesterConstants.NODE_TYPE_LABEL);
		if (Validator.isNotNull(_type)) {
			writer.writeCharacters(_type);
		}

		writer.writeEndElement();

		writer.writeStartElement(LarDigesterConstants.NODE_CLASS_PK_LABEL);
		if (Validator.isNotNull(_classPK)) {
			writer.writeCharacters(_classPK);
		}

		writer.writeEndElement();

		writer.writeStartElement(LarDigesterConstants.NODE_UUID_LABEL);
		if (Validator.isNotNull(_uuid)) {
			writer.writeCharacters(_uuid);
		}

		writer.writeEndElement();

		// permissions

		writer.writeStartElement(LarDigesterConstants.NODE_PERMISSIONS_LABEL);

		for (LarDigestPermission permission : _permissions) {
			permission.serialize(writer);
		}

		writer.writeEndElement();

		writer.writeEndElement();
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

	public void setUuid(String uuid) {
		_uuid = uuid;
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
	private String _uuid;

}
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

package com.liferay.portal.kernel.lar.digest;

import com.liferay.portal.kernel.lar.LarDigesterConstants;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.lar.AssetAction;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

/**
 * @author Mate Thurzo
 */
public class LarDigestEntryImpl implements LarDigestEntry {

	public LarDigestEntryImpl() {
	}

	public LarDigestEntryImpl(Element root) {
		Element metadataRootEl = root.element(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		List<Element> metadatasEl = metadataRootEl.elements(
			LarDigesterConstants.NODE_METADATA_LABEL);

		for (Element metadataEl : metadatasEl) {
			addMetadata(new LarDigestMetadataImpl(metadataEl));
		}

		Element element = root.element(LarDigesterConstants.NODE_ACTION_LABEL);
		String actionString = element.getText();

		if (Validator.isNotNull(actionString)) {
			_action = AssetAction.valueOf(element.getText());
		}
		else {
			_action = null;
		}

		element = root.element(LarDigesterConstants.NODE_CLASS_PK_LABEL);
		_classPK = element.getText();

		element = root.element(LarDigesterConstants.NODE_PATH_LABEL);
		_path = element.getText();

		element = root.element(LarDigesterConstants.NODE_CLASS_NAME_LABEL);
		_type = element.getText();

		Element dependenciesRootEl = root.element(
			LarDigesterConstants.NODE_DEPENDENCIES_LABEL);

		List<Element> dependenciesEl = dependenciesRootEl.elements(
			LarDigesterConstants.NODE_DEPENDENCY_LABEL);

		for (Element dependencyEl : dependenciesEl) {
			addDependency(new LarDigestEntryDependencyImpl(dependencyEl));
		}
	}

	public void addDependency(LarDigestEntryDependency dependency) {
		_dependencies.add(dependency);
	}

	public void addMetadata(LarDigestMetadata metadata) {
		if (_metadata.contains(metadata)) {
			return;
		}

		_metadata.add(metadata);
	}

	public AssetAction getAction() {
		return _action;
	}

	public String getClassName() {
		return _type;
	}

	public String getClassPK() {
		return _classPK;
	}

	public List<LarDigestEntryDependency> getDependencies() {
		return _dependencies;
	}

	public List<LarDigestEntryDependency> getDependencies(String className) {
		List<LarDigestEntryDependency> result =
			new ArrayList<LarDigestEntryDependency>();

		for (LarDigestEntryDependency dependency : _dependencies) {
			String itemClassName = dependency.getClassName();

			if (itemClassName.equals(className)) {
				result.add(dependency);
			}
		}

		return result;
	}

	public List<LarDigestMetadata> getMetadata() {
		return _metadata;
	}

	public String getMetadataValue(String name) {
		for (LarDigestMetadata metadata : _metadata) {
			String metadataName = metadata.getName();

			if (metadataName.equals(name)) {
				return metadata.getValue();
			}
		}

		return null;
	}

	public String getPath() {
		return _path;
	}

	public String getUuid() {
		return _uuid;
	}

	public void serialize(XMLStreamWriter writer) throws Exception {
		writer.writeStartElement(LarDigesterConstants.NODE_ENTRY_LABEL);

		// metadata
		writer.writeStartElement(LarDigesterConstants.NODE_METADATA_SET_LABEL);

		for (LarDigestMetadata metadata : _metadata) {
			metadata.serialize(writer);
		}

		writer.writeEndElement();

		// dependencies

		writer.writeStartElement(LarDigesterConstants.NODE_DEPENDENCIES_LABEL);

		for (LarDigestEntryDependency dependency : _dependencies) {
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
		if (_action != null) {
			writer.writeCharacters(_action.name());
		}

		writer.writeEndElement();

		writer.writeStartElement(LarDigesterConstants.NODE_CLASS_NAME_LABEL);
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

		writer.writeEndElement();
	}

	public void setAction(AssetAction action) {
		_action = action;
	}

	public void setClassName(String type) {
		_type = type;
	}

	public void setClassPK(String classPK) {
		_classPK = classPK;
	}

	public void setPath(String path) {
		_path = path;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	private AssetAction _action;
	private String _classPK;
	private List<LarDigestEntryDependency> _dependencies =
		new ArrayList<LarDigestEntryDependency>();
	private List<LarDigestMetadata> _metadata =
		new ArrayList<LarDigestMetadata>();
	private String _path;
	private String _type;
	private String _uuid;

}
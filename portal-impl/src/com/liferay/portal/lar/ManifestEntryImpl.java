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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.lar.ManifestEntry;
import com.liferay.portal.kernel.lar.ManifestEntryReference;
import com.liferay.portal.kernel.lar.ManifestWriter;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Attribute;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.StagedGroupedModel;
import com.liferay.portal.model.StagedModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;

/**
 * @author Mate Thurzo
 */
public class ManifestEntryImpl implements ManifestEntry {

	public ManifestEntryImpl(ClassedModel classedModel, String path) {
		setModel(classedModel, path);
	}

	public ManifestEntryImpl(Element entryElement) {
		for (Attribute attribute : entryElement.attributes()) {
			_modelAttributes.put(attribute.getName(), attribute.getValue());
		}

		Element referencesElement = entryElement.element(
			ManifestWriter.ELEMENT_NAME_REFERENCES);

		for (Element referenceElement : referencesElement.elements()) {
			ManifestEntryReference entryReference =
				new ManifestEntryReferenceImpl(referenceElement);

			_referencedEntries.add(entryReference);
		}
	}

	public void addModelAttribute(String key, String value) {
		_modelAttributes.put(key, value);
	}

	public void addReference(ManifestEntryReference manifestEntryReference) {
		_referencedEntries.add(manifestEntryReference);
	}

	public void addReferencedModel(ClassedModel classedModel, int type) {
		addReferencedModel(classedModel, StringPool.BLANK, type);
	}

	public void addReferencedModel(
		ClassedModel classedModel, String path, int type) {

		if (classedModel == null) {
			return;
		}

		ManifestEntryReference entryReference =
			new ManifestEntryReferenceImpl();

		if (classedModel instanceof StagedModel) {
			StagedModel stagedModel = (StagedModel)classedModel;

			entryReference.setUuid(stagedModel.getUuid());
		}

		if (classedModel instanceof StagedGroupedModel) {
			StagedGroupedModel stagedGroupedModel =
				(StagedGroupedModel)classedModel;

			entryReference.setGroupId(stagedGroupedModel.getGroupId());
		}

		entryReference.setPath(path);
		entryReference.setType(type);

		_referencedEntries.add(entryReference);
	}

	public Element asXmlElement() {
		Element entryElement = SAXReaderUtil.createElement(
			ManifestWriter.ELEMENT_NAME_ENTRY);

		for (Entry<String, String> attributeEntry :
				_modelAttributes.entrySet()) {

			entryElement.addAttribute(
				attributeEntry.getKey(), attributeEntry.getValue());
		}

		Element referencesElement = entryElement.addElement(
			ManifestWriter.ELEMENT_NAME_REFERENCES);

		for (ManifestEntryReference entryReference : _referencedEntries) {
			referencesElement.add(entryReference.asXmlElement());
		}

		return entryElement;
	}

	public long getGroupId() {
		return GetterUtil.getLong(_modelAttributes.get("group-id"));
	}

	public ClassedModel getModel() {
		return _model;
	}

	public String getModelAttribute(String key) {
		return _modelAttributes.get(key);
	}

	public Map<String, String> getModelAttributes() {
		return _modelAttributes;
	}

	public String getModelClassName() {
		return _modelClassName;
	}

	public String getPath() {
		return _modelAttributes.get("path");
	}

	public List<ManifestEntryReference> getReferencedEntries() {
		return _referencedEntries;
	}

	public String getUuid() {
		return _modelAttributes.get("uuid");
	}

	public void setModel(ClassedModel classedModel, String path) {
		addModelAttribute("path", path);

		_model = classedModel;
		_modelClassName = classedModel.getModelClassName();

		addModelAttribute("class-name", _modelClassName);
		addModelAttribute(
			"class-pk", String.valueOf(_model.getPrimaryKeyObj()));

		if (classedModel instanceof StagedModel) {
			StagedModel stagedModel = (StagedModel)classedModel;

			addModelAttribute("uuid", stagedModel.getUuid());
		}

		if (classedModel instanceof StagedGroupedModel) {
			StagedGroupedModel stagedGroupedModel =
				(StagedGroupedModel)classedModel;

			addModelAttribute(
				"group-id", String.valueOf(stagedGroupedModel.getGroupId()));
		}
	}

	private ClassedModel _model;
	private Map<String, String> _modelAttributes =
		new HashMap<String, String>();
	private String _modelClassName;

	private List<ManifestEntryReference> _referencedEntries =
		new ArrayList<ManifestEntryReference>();

}
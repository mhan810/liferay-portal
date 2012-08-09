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

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Attribute;
import com.liferay.portal.kernel.xml.Element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

/**
 * @author Daniel Kocsis
 */
public class LarDigestModuleImpl implements LarDigestModule {

	public LarDigestModuleImpl() {
		_items = new ArrayList<LarDigestItem>();
		_portletPreferences = new ArrayList<String>();
	}

	public LarDigestModuleImpl(Element root) {
		this();

		Attribute nameAttr = root.attribute("name");

		if (nameAttr != null) {
			_name = nameAttr.getText();
		}

		Element metadataRootEl = root.element(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		List<Element> metadatasEl = metadataRootEl.elements(
			LarDigesterConstants.NODE_METADATA_LABEL);

		for (Element metadataEl : metadatasEl) {
			addMetadata(new LarDigestMetadataImpl(metadataEl));
		}

		Element portletPreferencesEl = root.element(
			LarDigesterConstants.NODE_PORTLET_PREFERENCES_LABEL);

		List<Element> portletPreferenceEls = portletPreferencesEl.elements(
			LarDigesterConstants.NODE_PORTLET_PREFERENCE_LABEL);

		for (Element portletPreferenceEl : portletPreferenceEls) {
			_portletPreferences.add(portletPreferenceEl.getText());
		}

		List<Element> itemElements = root.elements(
			LarDigesterConstants.NODE_ITEM_LABEL);

		for (Element itemEl : itemElements) {
			_items.add(new LarDigestItemImpl(itemEl));
		}
	}

	public void addItem(LarDigestItem item) {
		_items.add(item);
	}

	public void addMetadata(LarDigestMetadata metadata) {
		_metadata.add(metadata);
	}

	public void addPortletPreference(String path) {
		_portletPreferences.add(path);
	}

	public List<LarDigestItem> getItems() {
		return _items;
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

	public String getName() {
		return _name;
	}

	public List<String> getPortletPreferences() {
		return _portletPreferences;
	}

	public void serialize(XMLStreamWriter writer) throws Exception {
		writer.writeStartElement(LarDigesterConstants.NODE_MODULE_LABEL);

		if (Validator.isNotNull(_name)) {
			writer.writeAttribute("name", _name);
		}

		// Metadata

		writer.writeStartElement(LarDigesterConstants.NODE_METADATA_SET_LABEL);

		for (LarDigestMetadata metadata : _metadata) {
			metadata.serialize(writer);
		}

		writer.writeEndElement();

		// Preferences

		writer.writeStartElement(
			LarDigesterConstants.NODE_PORTLET_PREFERENCES_LABEL);

		for (String portletPreferene : _portletPreferences) {
			writer.writeStartElement(
				LarDigesterConstants.NODE_PORTLET_PREFERENCE_LABEL);
			writer.writeCharacters(portletPreferene);
			writer.writeEndElement();
		}

		writer.writeEndElement();

		// Module Items

		for (LarDigestItem item : _items) {
			item.serialize(writer);
		}

		writer.writeEndElement();
	}

	public void setItems(List<LarDigestItem> items) {
		_items = items;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setPortletPreferences(List<String> portletPreferences) {
		_portletPreferences = portletPreferences;
	}

	private List<LarDigestItem> _items;
	private List<LarDigestMetadata> _metadata =
		new ArrayList<LarDigestMetadata>();
	private String _name;
	private List<String> _portletPreferences;

}
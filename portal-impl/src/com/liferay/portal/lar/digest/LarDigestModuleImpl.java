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

	public void addPortletPreference(String path) {
		_portletPreferences.add(path);
	}

	public List<LarDigestItem> getItems() {
		return _items;
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

		writer.writeStartElement(
			LarDigesterConstants.NODE_PORTLET_PREFERENCES_LABEL);

		for (String portletPreferene : _portletPreferences) {
			writer.writeStartElement(
				LarDigesterConstants.NODE_PORTLET_PREFERENCE_LABEL);
			writer.writeCharacters(portletPreferene);
			writer.writeEndElement();
		}

		writer.writeEndElement();

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
	private String _name;
	private List<String> _portletPreferences;

}
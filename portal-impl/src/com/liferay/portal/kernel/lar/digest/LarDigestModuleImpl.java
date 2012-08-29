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
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Attribute;
import com.liferay.portal.kernel.xml.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamWriter;

/**
 * @author Daniel Kocsis
 */
public class LarDigestModuleImpl implements LarDigestModule {

	public LarDigestModuleImpl(Element root) {
		// Name

		Attribute nameAttr = root.attribute(
			LarDigesterConstants.NODE_NAME_ATTRIBUTE_LABEL);

		if (nameAttr != null) {
			_name = nameAttr.getText();
		}

		// Metadata

		Element metadataRootEl = root.element(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		List<Element> metadatasEl = metadataRootEl.elements(
			LarDigesterConstants.NODE_METADATA_LABEL);

		for (Element metadataEl : metadatasEl) {
			addMetadata(new LarDigestMetadataImpl(metadataEl));
		}

		// Portlet preferences

		Element portletPreferencesEl = root.element(
			LarDigesterConstants.NODE_PORTLET_PREFERENCES_LABEL);

		List<Element> portletPreferenceEls = portletPreferencesEl.elements(
			LarDigesterConstants.NODE_PORTLET_PREFERENCE_LABEL);

		for (Element portletPreferenceEl : portletPreferenceEls) {
			_portletPreferences.add(
				new LarDigestPortletPreferenceImpl(portletPreferenceEl));
		}

		// Entries

		List<Element> entryElements = root.elements(
			LarDigesterConstants.NODE_ENTRY_LABEL);

		for (Element itemEl : entryElements) {
			LarDigestEntry digestEntry = new LarDigestEntryImpl(itemEl);

			addEntry(digestEntry);
		}
	}

	public LarDigestModuleImpl(String moduleName) {
		_name = moduleName;
	}

	public void addEntry(LarDigestEntry entry) {
		if (entry == null) {
			return;
		}

		Set<LarDigestEntry> batchEntries = _entries.get(entry.getClassName());

		if (batchEntries == null) {
			batchEntries = new HashSet<LarDigestEntry>();

			batchEntries.add(entry);

			String key = entry.getClassName();

			if (Validator.isNull(key)) {
				key = StringPool.BLANK;
			}

			_entries.put(key, batchEntries);

			return;
		}

		batchEntries.add(entry);
	}

	public void addMetadata(LarDigestMetadata metadata) {
		if (_metadata.contains(metadata)) {
			return;
		}

		_metadata.add(metadata);
	}

	public void addPortletPreference(LarDigestPortletPreference preference) {
		_portletPreferences.add(preference);
	}

	public Map<String, Set<LarDigestEntry>> getEntries() {
		return _entries;
	}

	public Set<LarDigestEntry> getEntriesByClassName(String moduleName) {
		if (!_entries.containsKey(moduleName)) {
			return Collections.emptySet();
		}

		return _entries.get(moduleName);
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

	public String getName() {
		return _name;
	}

	public List<LarDigestPortletPreference> getPortletPreferences() {
		return _portletPreferences;
	}

	public void serialize(XMLStreamWriter writer) throws Exception {
		writer.writeStartElement(LarDigesterConstants.NODE_MODULE_LABEL);
		writer.writeAttribute(
			LarDigesterConstants.NODE_NAME_ATTRIBUTE_LABEL, _name);

		// Metadata

		writer.writeStartElement(LarDigesterConstants.NODE_METADATA_SET_LABEL);

		for (LarDigestMetadata metadata : _metadata) {
			metadata.serialize(writer);
		}

		writer.writeEndElement();

		// Preferences

		writer.writeStartElement(
			LarDigesterConstants.NODE_PORTLET_PREFERENCES_LABEL);

		for (LarDigestPortletPreference portletPreferene :
				_portletPreferences) {

			portletPreferene.serialize(writer);
		}

		writer.writeEndElement();

		// Module Items

		for (String key : _entries.keySet()) {
			for (LarDigestEntry moduleEntry : _entries.get(key)) {
				moduleEntry.serialize(writer);
			}
		}

		writer.writeEndElement();
	}

	public void setEntries(Map<String, Set<LarDigestEntry>> entries) {
		_entries = entries;
	}

	public void setModuleEntries(Set<LarDigestEntry> entries) {
		for (LarDigestEntry entry : entries) {
			Set<LarDigestEntry> batchEntries = null;

			if (_entries.containsKey(entry.getClassName())) {
				batchEntries = _entries.get(entry.getClassName());

				batchEntries.add(entry);
			}
			else {
				batchEntries = new HashSet<LarDigestEntry>();

				batchEntries.add(entry);

				_entries.put(entry.getClassName(), batchEntries);
			}
		}
	}

	public void setName(String name) {
		_name = name;
	}

	public void setPortletPreferences(
		List<LarDigestPortletPreference> portletPreferences) {

		_portletPreferences = portletPreferences;
	}

	private Map<String, Set<LarDigestEntry>> _entries =
		new HashMap<String, Set<LarDigestEntry>>();
	private List<LarDigestMetadata> _metadata =
		new ArrayList<LarDigestMetadata>();
	private String _name;
	private List<LarDigestPortletPreference> _portletPreferences =
		new ArrayList<LarDigestPortletPreference>();

}
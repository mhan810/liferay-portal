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

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

/**
 * @author Daniel Kocsis
 */
public class LarDigestPortletPreferenceImpl
	implements LarDigestPortletPreference {

	public LarDigestPortletPreferenceImpl() {
		_metadata = new ArrayList<LarDigestMetadata>();
	}

	public LarDigestPortletPreferenceImpl(String path) {
		this();

		_path = path;
	}

	public LarDigestPortletPreferenceImpl(Element root) {
		this();

		// Metadata

		Element metadataRootEl = root.element(
			LarDigesterConstants.NODE_METADATA_SET_LABEL);

		List<Element> metadatasEl = metadataRootEl.elements(
			LarDigesterConstants.NODE_METADATA_LABEL);

		for (Element metadataEl : metadatasEl) {
			addMetadata(new LarDigestMetadataImpl(metadataEl));
		}

		// Path

		Element element = root.element(LarDigesterConstants.NODE_PATH_LABEL);
		_path = element.getText();
	}

	public void addMetadata(LarDigestMetadata metadata) {
		if (_metadata.contains(metadata)) {
			return;
		}

		_metadata.add(metadata);
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

	public void serialize(XMLStreamWriter writer) throws Exception {
		writer.writeStartElement(
			LarDigesterConstants.NODE_PORTLET_PREFERENCE_LABEL);

		// Metadata

		writer.writeStartElement(LarDigesterConstants.NODE_METADATA_SET_LABEL);

		for (LarDigestMetadata metadata : _metadata) {
			metadata.serialize(writer);
		}

		writer.writeEndElement();

		// Path

		writer.writeStartElement(LarDigesterConstants.NODE_PATH_LABEL);
		if (Validator.isNotNull(_path)) {
			writer.writeCharacters(_path);
		}

		writer.writeEndElement();

		writer.writeEndElement();
	}

	public void setMetadata(List<LarDigestMetadata> metadata) {
		_metadata = metadata;
	}

	public void setPath(String path) {
		_path = path;
	}

	private List<LarDigestMetadata> _metadata;
	private String _path;

}
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

import com.liferay.portal.kernel.xml.Attribute;
import com.liferay.portal.kernel.xml.Element;

import javax.xml.stream.XMLStreamWriter;

/**
 * @author Daniel Kocsis
 */
public class LarDigestMetadataImpl implements LarDigestMetadata {

	public LarDigestMetadataImpl(Element root) {
		Attribute metadataAt = root.attribute(0);

		_name = metadataAt.getName();
		_value = metadataAt.getValue();
	}

	public LarDigestMetadataImpl(String name, String value) {
		_name = name;

		_value = value;
	}

	public String getName() {
		return _name;
	}

	public String getValue() {
		return _value;
	}

	public void serialize(XMLStreamWriter writer) throws Exception {
		writer.writeStartElement(LarDigesterConstants.NODE_METADATA_LABEL);
		writer.writeAttribute(_name, _value);
		writer.writeEndElement();
	}

	public void setName(String name) {
		_name = name;
	}

	public void setValue(String value) {
		_value = value;
	}

	private String _name;

	private String _value;

}
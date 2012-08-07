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

import com.liferay.portal.kernel.xml.Element;

import javax.xml.stream.XMLStreamWriter;

/**
 * @author Daniel Kocsis
 */
public class LarDigestDependencyImpl implements LarDigestDependency {

	public LarDigestDependencyImpl(Element root) {
		Element classPkEl = root.element("className");
		Element uuidEl = root.element("uuid");

		_classPK = classPkEl.getText();
		_uuid = uuidEl.getText();
	}

	public LarDigestDependencyImpl(String className, String uuid) {
		_classPK = className;
		_uuid = uuid;
	}

	public String getClassPK() {
		return _classPK;
	}

	public String getType() {
		return _type;
	}

	public String getUuid() {
		return _uuid;
	}

	public void serialize(XMLStreamWriter writer) throws Exception {
		writer.writeStartElement(LarDigesterConstants.NODE_DEPENDENCY_LABEL);

		writer.writeStartElement("classPK");
		writer.writeCharacters(_classPK);
		writer.writeEndElement();

		writer.writeStartElement("uuid");
		writer.writeCharacters(_uuid);
		writer.writeEndElement();

		writer.writeStartElement("type");
		writer.writeCharacters(_type);
		writer.writeEndElement();

		writer.writeEndElement();
	}

	public void setClassPK(String classPK) {
		_classPK = classPK;
	}

	public void setType(String type) {
		_type = type;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	private String _classPK;
	private String _type;
	private String _uuid;

}
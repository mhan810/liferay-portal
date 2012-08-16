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

import javax.xml.stream.XMLStreamWriter;

/**
 * @author Daniel Kocsis
 */
public class LarDigestEntryDependencyImpl implements LarDigestEntryDependency {

	public LarDigestEntryDependencyImpl(Element root) {
		Element classNameEl = root.element("className");
		Element uuidEl = root.element("uuid");

		if (classNameEl != null) {
			_className = classNameEl.getText();
		}

		if (uuidEl != null) {
			_uuid = uuidEl.getText();
		}
	}

	public LarDigestEntryDependencyImpl(String className, String uuid) {
		_className = className;
		_uuid = uuid;
	}

	public String getClassName() {
		return _className;
	}

	public String getUuid() {
		return _uuid;
	}

	public void serialize(XMLStreamWriter writer) throws Exception {
		if (!isValidObject()) {
			return;
		}

		writer.writeStartElement(LarDigesterConstants.NODE_DEPENDENCY_LABEL);

		writer.writeStartElement(LarDigesterConstants.NODE_CLASS_NAME_LABEL);

		if (Validator.isNotNull(_className)) {
			writer.writeCharacters(_className);
		}

		writer.writeEndElement();

		writer.writeStartElement("uuid");
		if (Validator.isNotNull(_uuid)) {
			writer.writeCharacters(_uuid);
		}

		writer.writeEndElement();

		writer.writeEndElement();
	}

	public void setClassName(String className) {
		_className = className;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	protected boolean isValidObject() throws Exception {
		if (Validator.isNull(_className) && Validator.isNull(_uuid)) {
			return false;
		}

		return true;
	}

	private String _className;
	private String _uuid;

}
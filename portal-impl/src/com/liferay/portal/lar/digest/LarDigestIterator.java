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

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.xml.StAXReaderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import static com.liferay.portal.lar.digest.LarDigesterConstants.*;

/**
 * @author Mate Thurzo
 */
public class LarDigestIterator implements Iterator<LarDigestItem> {

	public LarDigestIterator(XMLEventReader digestXMLEventReader) {
		_xmlEventReader = digestXMLEventReader;
	}

	public LarDigestIterator(XMLStreamReader digestXMLStreamReader) {
		_xmlStreamReader = digestXMLStreamReader;
	}

	/*public boolean hasNext() {
		try {
			while (_xmlEventReader.hasNext()) {
				XMLEvent nextEvent = _xmlEventReader.peek();

				String nextStartElementName = _getElementName(nextEvent);

				if (nextEvent.isStartElement()) {
					if (nextStartElementName.equals(NODE_DIGEST_ITEM_LABEL)) {
						return true;
					}
				}

				nextEvent = _xmlEventReader.nextEvent();
			}

			return false;
		}
		catch (Exception e) {
			return false;
		}
	}  */

	public boolean hasNext() {
		try {
			do {
				if (_xmlStreamReader.isStartElement()) {
					String nextStartElementName =
						_xmlStreamReader.getLocalName();

					if (nextStartElementName.equals(NODE_DIGEST_ITEM_LABEL)) {
						return true;
					}
				}

				_xmlStreamReader.next();
			}
			while (_xmlStreamReader.hasNext());

			return false;
		}
		catch (Exception e) {
			return false;
		}
	}

	public LarDigestItem next() {
		if(!hasNext()) {
			return null;
		}

		try {
			LarDigestItem item = new LarDigestItemImpl();

			Map metadata = new HashMap<String, String>();
			Map<String, List<String>> permissions =
				new HashMap<String, List<String>>();

			while (_xmlStreamReader.hasNext()) {
				String elementName = _xmlStreamReader.getLocalName();

				if (_xmlStreamReader.isStartElement()) {
					if (elementName.equals(NODE_PATH_LABEL)) {
						String elementText = _xmlStreamReader.getElementText();
						item.setPath(elementText);
					}
					else if (elementName.equals(NODE_ACTION_LABEL)) {
						String elementText = _xmlStreamReader.getElementText();
						int actionCode = Integer.parseInt(elementText);

						item.setAction(actionCode);
					}
					else if (elementName.equals(NODE_TYPE_LABEL)) {
						String elementText = _xmlStreamReader.getElementText();
						item.setType(elementText);
					}
					else if (elementName.equals(NODE_CLASS_PK_LABEL)) {
						String elementText = _xmlStreamReader.getElementText();
						item.setClassPK(elementText);
					}
					else if (elementName.equals(NODE_METADATA_LABEL)) {
						metadata.put(_xmlStreamReader.getAttributeName(0),
							_xmlStreamReader.getAttributeValue(0));
					}
					else if (elementName.equals(NODE_PERMISSION_LABEL)) {
						String roleName = _xmlStreamReader.getAttributeValue(
							null, ATTRIBUTE_NAME_ROLE);

						if (Validator.isNull(roleName)) {
							continue;
						}

						List actionNames = permissions.get(roleName);

						if (actionNames == null) {
							actionNames = new ArrayList<String>();
							permissions.put(roleName, actionNames);
						}

						while(_xmlStreamReader.hasNext()) {
							elementName = _xmlStreamReader.getLocalName();

							if (_xmlStreamReader.isEndElement() &&
								elementName.equals(NODE_PERMISSION_LABEL)) {

								break;
							}

							if (_xmlStreamReader.isStartElement() &&
								elementName.equals(NODE_ACTION_KEY_LABEL)) {

								actionNames.add(
									_xmlStreamReader.getElementText());
							}

							_xmlStreamReader.nextTag();
						}
					}
				}
				else if (_xmlStreamReader.isEndElement()) {
					if (elementName.equals(NODE_DIGEST_ITEM_LABEL)) {
						item.setMetadata(metadata);
						item.setPermissions(permissions);
						return item;
					}
				}

				_xmlStreamReader.nextTag();
			}

			return null;
		}
		catch (Exception e) {
			return null;
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	private String _getElementName(XMLEvent event) {
		if (event.isStartElement()) {
			StartElement startElement = event.asStartElement();

			return startElement.getName().getLocalPart();
		}
		else if (event.isEndElement()) {
			EndElement endElement = event.asEndElement();

			return endElement.getName().getLocalPart();
		}

		return StringPool.BLANK;
	}

	private XMLEventReader _xmlEventReader;
	private XMLStreamReader _xmlStreamReader;

}
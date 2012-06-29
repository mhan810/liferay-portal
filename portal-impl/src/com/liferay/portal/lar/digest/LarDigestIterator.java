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
import com.liferay.portal.xml.StAXReaderUtil;

import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
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

	public boolean hasNext() {
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
	}

	public LarDigestItem next() {
		try {
			LarDigestItem item = new LarDigestItemImpl();

			while (_xmlEventReader.hasNext()) {
				XMLEvent event = _xmlEventReader.nextEvent();

				String elementName = _getElementName(event);

				if (event.isStartElement()) {
					if (elementName.equals(NODE_PATH_LABEL)) {

						String path = StAXReaderUtil.read(_xmlEventReader);

						item.setPath(path);
					}
					else if (elementName.equals(NODE_ACTION_LABEL)) {

						String action = StAXReaderUtil.read(_xmlEventReader);
						int actionCode = Integer.parseInt(action);

						item.setAction(actionCode);
					}
					else if (elementName.equals(NODE_TYPE_LABEL)) {

						String type = StAXReaderUtil.read(_xmlEventReader);

						item.setType(type);
					}
					else if (elementName.equals(NODE_CLASS_PK_LABEL)) {

						String classPK = StAXReaderUtil.read(_xmlEventReader);

						item.setClassPK(classPK);
					}
				}
				else if (event.isEndElement()) {
					if (elementName.equals(NODE_DIGEST_ITEM_LABEL)) {

						return item;
					}
				}
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

	private LarDigestItem _nextItem;
	private XMLEventReader _xmlEventReader;

}
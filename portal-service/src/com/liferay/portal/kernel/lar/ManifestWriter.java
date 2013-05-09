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

package com.liferay.portal.kernel.lar;

import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

/**
 * @author Mate Thurzo
 */
public interface ManifestWriter {

	public static String ELEMENT_NAME_ENTRY = "manifest-entry";

	public static String ELEMENT_NAME_REFERENCE = "reference";

	public static String ELEMENT_NAME_REFERENCES = "references";

	public static String ELEMENT_NAME_ROOT = "data-manifest";

	public static String ELEMENT_NAME_SECTION = "section";

	public void addManifestEntry(ManifestEntry manifestEntry);

	public void close() throws PortletDataException;

	public void closeSection() throws XMLStreamException;

	public InputStream getManifestAsStream();

	public InputStream getManifestIndexAsStream();

	public void openSection(String portletId) throws XMLStreamException;

}
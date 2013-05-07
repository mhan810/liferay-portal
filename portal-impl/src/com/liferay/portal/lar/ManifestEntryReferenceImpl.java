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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.lar.ManifestEntryReference;
import com.liferay.portal.kernel.lar.ManifestWriter;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

/**
 * @author Daniel Kocsis
 */
public class ManifestEntryReferenceImpl implements ManifestEntryReference {

	public ManifestEntryReferenceImpl() {
	}

	public ManifestEntryReferenceImpl(Element entryReferenceElement) {
		_path = entryReferenceElement.attributeValue("path");

		_uuid = entryReferenceElement.attributeValue("uuid");

		_groupId = GetterUtil.getLong(
			entryReferenceElement.attributeValue("group-id"));

		_type = GetterUtil.getInteger(
			entryReferenceElement.attributeValue("type"));
	}

	public Element asXmlElement() {
		Element entryReferenceElement = SAXReaderUtil.createElement(
			ManifestWriter.ELEMENT_NAME_REFERENCE);

		if (Validator.isNotNull(_path)) {
			entryReferenceElement.addAttribute("path", _path);
		}

		if (Validator.isNotNull(_uuid)) {
			entryReferenceElement.addAttribute("uuid", _uuid);
		}

		if (_groupId > 0) {
			entryReferenceElement.addAttribute(
				"group-id", String.valueOf(_groupId));
		}

		entryReferenceElement.addAttribute("type", String.valueOf(_type));

		return entryReferenceElement;
	}

	public long getGroupId() {
		return _groupId;
	}

	public String getPath() {
		return _path;
	}

	public int getType() {
		return _type;
	}

	public String getUuid() {
		return _uuid;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public void setPath(String path) {
		_path = path;
	}

	public void setType(int type) {
		_type = type;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	private long _groupId;
	private String _path;
	private int _type;
	private String _uuid;

}
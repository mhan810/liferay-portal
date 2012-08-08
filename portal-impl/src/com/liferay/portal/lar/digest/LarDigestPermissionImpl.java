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
public class LarDigestPermissionImpl implements LarDigestPermission {

	public LarDigestPermissionImpl() {
	}

	public LarDigestPermissionImpl(Element root) {
		Attribute role = root.attribute(
				LarDigesterConstants.ATTRIBUTE_NAME_ROLE);

		_roleName = role.getText();

		List<Element> actionNameElements = root.elements(
			LarDigesterConstants.NODE_ACTION_KEY_LABEL);

		for (Element actionId : actionNameElements) {
			addActionId(actionId.getText());
		}
	}

	public void addActionId(String actionId) {
		_actionIds.add(actionId);
	}

	public List<String> getActionIds() {
		return _actionIds;
	}

	public String getRoleName() {
		return _roleName;
	}

	public void serialize(XMLStreamWriter writer) throws Exception {
		writer.writeStartElement(LarDigesterConstants.NODE_PERMISSION_LABEL);
		writer.writeAttribute(
			LarDigesterConstants.ATTRIBUTE_NAME_ROLE, _roleName);

		for (String action : _actionIds) {
			if (Validator.isNull(action)) {
				continue;
			}

			writer.writeStartElement(
				LarDigesterConstants.NODE_ACTION_KEY_LABEL);
			writer.writeCharacters(action);
			writer.writeEndElement();
		}

		writer.writeEndElement();
	}

	public void setActionIds(List<String> actionIds) {
		_actionIds = actionIds;
	}

	public void setRoleName(String roleName) {
		_roleName = roleName;
	}

	private List<String> _actionIds = new ArrayList<String>();
	private String _roleName;

}
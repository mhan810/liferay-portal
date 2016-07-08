/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.workflow.kaleo.definition;

/**
 * @author Michael C. Han
 */
public class Form implements Comparable<Form> {

	public Form(String name, String formUUID, int priority) {
		_name = name;
		_formUUID = formUUID;
		_priority = priority;
	}

	@Override
	public int compareTo(Form form) {
		if (getPriority() > form.getPriority()) {
			return 1;
		}
		else if (getPriority() < form.getPriority()) {
			return -1;
		}

		return 0;
	}

	public String getDescription() {
		return _description;
	}

	public String getFormUUID() {
		return _formUUID;
	}

	public String getMetadata() {
		return _metadata;
	}

	public String getName() {
		return _name;
	}

	public int getPriority() {
		return _priority;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setMetadata(String metadata) {
		_metadata = metadata;
	}

	private int _priority;
	private String _name;
	private String _description;
	private String _formUUID;
	private String _metadata;

}

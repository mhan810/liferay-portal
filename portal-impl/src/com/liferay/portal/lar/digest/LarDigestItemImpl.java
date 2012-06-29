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

/**
 * @author Mate Thurzo
 */
public class LarDigestItemImpl implements LarDigestItem {

	public int getAction() {
		return _action;
	}

	public String getClassPK() {
		return _classPK;
	}

	public String getPath() {
		return _path;
	}

	public String getType() {
		return _type;
	}

	public void setAction(int action) {
		_action = action;
	}

	public void setClassPK(String classPK) {
		_classPK = classPK;
	}

	public void setPath(String path) {
		_path = path;
	}

	public void setType(String type) {
		_type = type;
	}

	private int _action;
	private String _classPK;
	private String _path;
	private String _type;

}
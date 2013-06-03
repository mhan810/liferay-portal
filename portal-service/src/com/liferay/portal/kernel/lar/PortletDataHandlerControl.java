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

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Locale;

/**
 * @author Raymond Augé
 */
public class PortletDataHandlerControl {

	public static String getNamespacedControlName(
		String namespace, String controlName) {

		StringBundler sb = new StringBundler(4);

		sb.append(StringPool.UNDERLINE);
		sb.append(namespace);
		sb.append(StringPool.UNDERLINE);
		sb.append(controlName);

		return sb.toString();
	}

	public PortletDataHandlerControl(String namespace, String controlName) {
		this(namespace, controlName, false);
	}

	public PortletDataHandlerControl(
		String namespace, String controlName, boolean disabled) {

		this(namespace, controlName, disabled, false, null, null);
	}

	public PortletDataHandlerControl(
		String namespace, String controlName, boolean disabled, boolean hidden,
		String className, PortletDataHandlerControl[] children) {

		_children = children;
		_className = className;
		_controlName = controlName;
		_disabled = disabled;
		_hidden = hidden;
		_namespace = namespace;
	}

	public PortletDataHandlerControl(
		String namespace, String controlName, String className) {

		this(namespace, controlName, false, false, className, null);
	}

	public PortletDataHandlerControl[] getChildren() {
		return _children;
	}

	public String getClassName() {
		return _className;
	}

	public String getControlName() {
		return _controlName;
	}

	public String getHelpMessage(Locale locale, String action) {
		return LanguageUtil.get(
			locale, action + "-" + _controlName + "-help", StringPool.BLANK);
	}

	public String getNamespace() {
		return _namespace;
	}

	public String getNamespacedControlName() {
		return getNamespacedControlName(_namespace, getControlName());
	}

	public boolean isDisabled() {
		return _disabled;
	}

	public boolean isHidden() {
		return _hidden;
	}

	public void setNamespace(String namespace) {
		_namespace = namespace;
	}

	private PortletDataHandlerControl[] _children;
	private String _className;
	private String _controlName;
	private boolean _disabled;
	private boolean _hidden;
	private String _namespace;

}
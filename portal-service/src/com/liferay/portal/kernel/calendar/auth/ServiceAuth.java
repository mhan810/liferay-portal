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

package com.liferay.portal.kernel.calendar.auth;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Locale;

/**
 * @author Josef Sustacek
 */
public interface ServiceAuth {

	/**
	 * User's ID as specified in portal. Used as human-readable
	 * information when making booking using this <code>ServiceAuth</code>
	 * instance.
	 * @return
	 */
	public String getPortalUserId();

	/**
	 * User's full as specified in portal. Used as human-readable
	 * information when making booking using this <code>ServiceAuth</code>
	 * instance.
	 *
	 * @return
	 */
	public String getPortalFullName();

	/**
	 * User's email address as specified in portal. Used as human-readable
	 * information when making booking using this <code>ServiceAuth</code>
	 * instance.
	 *
	 * @return
	 */
	public String getPortalEmailAddress();

	public AuthType getType();

	/**
	 * Specifies a type of authentication of given <code>ServiceAuth</code>.
	 * Used in <code>CalendarServicesProvider</code> to declare which auth
	 * schemes are supported by given provider.
	 */
	public enum AuthType {

		NONE, LOGIN_AND_PASSWORD;

		private AuthType() {
			_labelLanguageKey = _LANGUAGE_KEY_PREFIX.concat(name());
		}

		public String getLabel() {
			return getLabel(LocaleUtil.getMostRelevantLocale());
		}

		public String getLabel(Locale locale) {
			return LanguageUtil.get(locale, _labelLanguageKey);
		}

		private String _labelLanguageKey;


		private static final String _LANGUAGE_KEY_PREFIX =
			"calendar.provider.service.auth.type.";

	}

}

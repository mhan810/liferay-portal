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

package com.liferay.translation.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.translation.service.TranslationEntryServiceUtil;

/**
 * Provides the HTTP utility for the
 * <code>TranslationEntryServiceUtil</code> service
 * utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * <code>HttpPrincipal</code> parameter.
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <b>tunnel.servlet.hosts.allowed</b> in portal.properties to
 * configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TranslationEntryServiceSoap
 * @generated
 */
public class TranslationEntryServiceHttp {

	public static com.liferay.translation.model.TranslationEntry
			addOrUpdateTranslationEntry(
				HttpPrincipal httpPrincipal, long groupId,
				com.liferay.info.item.InfoItemReference infoItemReference,
				String content, String contentType,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				TranslationEntryServiceUtil.class,
				"addOrUpdateTranslationEntry",
				_addOrUpdateTranslationEntryParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, groupId, infoItemReference, content, contentType,
				serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.translation.model.TranslationEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.translation.model.TranslationEntry
			addOrUpdateTranslationEntry(
				HttpPrincipal httpPrincipal, long groupId, String languageId,
				com.liferay.info.item.InfoItemReference infoItemReference,
				com.liferay.info.item.InfoItemFieldValues infoItemFieldValues,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				TranslationEntryServiceUtil.class,
				"addOrUpdateTranslationEntry",
				_addOrUpdateTranslationEntryParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, groupId, languageId, infoItemReference,
				infoItemFieldValues, serviceContext);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.translation.model.TranslationEntry)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		TranslationEntryServiceHttp.class);

	private static final Class<?>[]
		_addOrUpdateTranslationEntryParameterTypes0 = new Class[] {
			long.class, com.liferay.info.item.InfoItemReference.class,
			String.class, String.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};
	private static final Class<?>[]
		_addOrUpdateTranslationEntryParameterTypes1 = new Class[] {
			long.class, String.class,
			com.liferay.info.item.InfoItemReference.class,
			com.liferay.info.item.InfoItemFieldValues.class,
			com.liferay.portal.kernel.service.ServiceContext.class
		};

}
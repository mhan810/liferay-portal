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

package com.liferay.portlet.portaldata.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.security.auth.HttpPrincipal;
import com.liferay.portal.service.http.TunnelUtil;

import com.liferay.portlet.portaldata.service.PortalDataHandlerServiceUtil;

/**
 * Provides the HTTP utility for the
 * {@link com.liferay.portlet.portaldata.service.PortalDataHandlerServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * {@link com.liferay.portal.security.auth.HttpPrincipal} parameter.
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
 * @author    Brian Wing Shun Chan
 * @see       PortalDataHandlerServiceSoap
 * @see       com.liferay.portal.security.auth.HttpPrincipal
 * @see       com.liferay.portlet.portaldata.service.PortalDataHandlerServiceUtil
 * @generated
 */
public class PortalDataHandlerServiceHttp {
	public static java.io.File exportPortalDataAsFile(
		HttpPrincipal httpPrincipal, java.lang.Class type,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.util.Date startDate, java.util.Date endDate)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		try {
			MethodKey methodKey = new MethodKey(PortalDataHandlerServiceUtil.class,
					"exportPortalDataAsFile",
					_exportPortalDataAsFileParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(methodKey, type,
					parameters, startDate, endDate);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				if (e instanceof com.liferay.portal.kernel.exception.SystemException) {
					throw (com.liferay.portal.kernel.exception.SystemException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.io.File)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static java.io.File exportPortalDataAsFile(
		HttpPrincipal httpPrincipal, java.lang.String portletId,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.util.Date startDate, java.util.Date endDate)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		try {
			MethodKey methodKey = new MethodKey(PortalDataHandlerServiceUtil.class,
					"exportPortalDataAsFile",
					_exportPortalDataAsFileParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					portletId, parameters, startDate, endDate);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				if (e instanceof com.liferay.portal.kernel.exception.SystemException) {
					throw (com.liferay.portal.kernel.exception.SystemException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}

			return (java.io.File)returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void importPortalData(HttpPrincipal httpPrincipal,
		java.lang.String portletId,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.io.File file)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		try {
			MethodKey methodKey = new MethodKey(PortalDataHandlerServiceUtil.class,
					"importPortalData", _importPortalDataParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					portletId, parameters, file);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				if (e instanceof com.liferay.portal.kernel.exception.SystemException) {
					throw (com.liferay.portal.kernel.exception.SystemException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	public static void importPortalData(HttpPrincipal httpPrincipal,
		java.lang.String portletId,
		java.util.Map<java.lang.String, java.lang.String[]> parameters,
		java.io.InputStream data)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		try {
			MethodKey methodKey = new MethodKey(PortalDataHandlerServiceUtil.class,
					"importPortalData", _importPortalDataParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(methodKey,
					portletId, parameters, data);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception e) {
				if (e instanceof com.liferay.portal.kernel.exception.PortalException) {
					throw (com.liferay.portal.kernel.exception.PortalException)e;
				}

				if (e instanceof com.liferay.portal.kernel.exception.SystemException) {
					throw (com.liferay.portal.kernel.exception.SystemException)e;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(e);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException se) {
			_log.error(se, se);

			throw se;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(PortalDataHandlerServiceHttp.class);
	private static final Class<?>[] _exportPortalDataAsFileParameterTypes0 = new Class[] {
			java.lang.Class.class, java.util.Map.class, java.util.Date.class,
			java.util.Date.class
		};
	private static final Class<?>[] _exportPortalDataAsFileParameterTypes1 = new Class[] {
			java.lang.String.class, java.util.Map.class, java.util.Date.class,
			java.util.Date.class
		};
	private static final Class<?>[] _importPortalDataParameterTypes2 = new Class[] {
			java.lang.String.class, java.util.Map.class, java.io.File.class
		};
	private static final Class<?>[] _importPortalDataParameterTypes3 = new Class[] {
			java.lang.String.class, java.util.Map.class,
			java.io.InputStream.class
		};
}
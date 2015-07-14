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

package com.liferay.portal.monitoring.internal.servlet.filters;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.monitoring.DataSample;
import com.liferay.portal.kernel.monitoring.DataSampleFactory;
import com.liferay.portal.kernel.monitoring.DataSampleThreadLocal;
import com.liferay.portal.kernel.monitoring.PortalMonitoringControl;
import com.liferay.portal.kernel.monitoring.PortletMonitoringControl;
import com.liferay.portal.kernel.monitoring.RequestStatus;
import com.liferay.portal.kernel.monitoring.ServiceMonitoringControl;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.LayoutLocalService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalInstances;
import com.liferay.portal.util.PortalUtil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Rajesh Thiagarajan
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = {
		"dispatcher=FORWARD", "dispatcher=REQUEST", "servlet-context-name=",
		"servlet-filter-name=Monitoring Filter", "url-pattern=/c/*",
		"url-pattern=/group/*", "url-pattern=/user/*", "url-pattern=/web/*"
	},
	service = {Filter.class, PortalMonitoringControl.class}
)
public class MonitoringFilter extends BaseFilter
	implements PortalMonitoringControl {

	@Override
	public boolean isFilterEnabled() {
		if (!super.isFilterEnabled()) {
			return false;
		}

		if (!_monitorPortalRequest &&
			!_portletMonitoringControl.isMonitorPortletActionRequest() &&
			!_portletMonitoringControl.isMonitorPortletEventRequest() &&
			!_portletMonitoringControl.isMonitorPortletRenderRequest() &&
			!_portletMonitoringControl.isMonitorPortletResourceRequest() &&
			!_serviceMonitoringControl.isMonitorServiceRequest()) {

			return false;
		}

		return true;
	}

	@Override
	public boolean isMonitorPortalRequest() {
		return _monitorPortalRequest;
	}

	@Override
	public void setMonitorPortalRequest(boolean monitorPortalRequest) {
		_monitorPortalRequest = monitorPortalRequest;
	}

	protected Object[] getFriendlyURLPathInfo(String requestURI) {
		if (requestURI.startsWith(PortalUtil.getPathFriendlyURLPrivateUser())) {
			return new Object[] {
				PortalUtil.getPathFriendlyURLPrivateUser(), true
			};
		}
		else if (requestURI.startsWith(
					PortalUtil.getPathFriendlyURLPrivateGroup())) {

			return new Object[] {
				PortalUtil.getPathFriendlyURLPrivateGroup(), false
			};
		}

		return new Object[] {PortalUtil.getPathFriendlyURLPublic(), false};
	}

	protected long getGroupId(HttpServletRequest request) {
		long groupId = ParamUtil.getLong(request, "groupId");

		if (groupId > 0) {
			return groupId;
		}

		long plid = ParamUtil.getLong(request, "p_l_id");

		if ((plid > 0) && (_layoutLocalService != null)) {
			try {
				Layout layout = _layoutLocalService.getLayout(plid);

				groupId = layout.getGroupId();
			}
			catch (PortalException pe) {
				if (_log.isDebugEnabled()) {
					_log.debug("Unable to retrieve layout " + plid, pe);
				}
			}
		}

		if (groupId > 0) {
			return groupId;
		}
		else {
			try {
				groupId = getGroupIdFromPath(request);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Unable to retrieve groupId from request URI", e);
				}
			}
		}

		return groupId;
	}

	protected long getGroupIdFromPath(HttpServletRequest request) {
		Object[] pathInfo = getPathInfo(request);
		String path = (String)pathInfo[0];
		boolean isUser = (boolean)pathInfo[1];

		String friendlyURL = null;

		int pos = path.indexOf(CharPool.SLASH, 1);

		if (pos != -1) {
			friendlyURL = path.substring(0, pos);
		}
		else if (path.length() > 1) {
			friendlyURL = path;
		}

		if (Validator.isNull(friendlyURL)) {
			return 0;
		}

		long companyId = PortalInstances.getCompanyId(request);

		Group group = _groupLocalService.fetchFriendlyURLGroup(
			companyId, friendlyURL);

		if (group == null) {
			String screenName = friendlyURL.substring(1);

			if (isUser || !Validator.isNumber(screenName)) {
				User user = _userLocalService.fetchUserByScreenName(
					companyId, screenName);

				if (user != null) {
					group = user.getGroup();
				}
				else if (_log.isWarnEnabled()) {
					_log.warn("No user exists with friendly URL " + screenName);
				}
			}
			else {
				long groupId = GetterUtil.getLong(screenName);

				group = _groupLocalService.fetchGroup(groupId);

				if (group == null) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							"No group exists with friendly URL " + groupId +
								". Try fetching by screen name instead.");
					}

					User user = _userLocalService.fetchUserByScreenName(
						companyId, screenName);

					if (user != null) {
						group = user.getGroup();
					}
					else if (_log.isWarnEnabled()) {
						_log.warn(
							"No user or group exists with friendly URL " +
								groupId);
					}
				}
			}
		}

		if (group == null) {
			return 0;
		}

		return group.getGroupId();
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	protected Object[] getPathInfo(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		Object[] friendlyURLPathInfo = getFriendlyURLPathInfo(requestURI);
		String friendlyURLPathPrefix = (String)friendlyURLPathInfo[0];
		Boolean isUser = (Boolean)friendlyURLPathInfo[1];

		int pos = requestURI.indexOf(Portal.JSESSIONID);

		if (pos != -1) {
			requestURI = requestURI.substring(0, pos);
		}

		String pathProxy = PortalUtil.getPathProxy();

		pos = friendlyURLPathPrefix.length() - pathProxy.length();

		return new Object[] {requestURI.substring(pos), isUser};
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws IOException, ServletException {

		long companyId = PortalUtil.getCompanyId(request);
		long groupId = getGroupId(request);

		DataSample dataSample = null;

		if (_monitorPortalRequest) {
			dataSample = _dataSampleFactory.createPortalRequestDataSample(
				companyId, groupId, request.getRemoteUser(),
				request.getRequestURI(),
				GetterUtil.getString(request.getRequestURL()));

			DataSampleThreadLocal.initialize();
		}

		try {
			if (dataSample != null) {
				dataSample.prepare();
			}

			processFilter(
				MonitoringFilter.class, request, response, filterChain);

			if (dataSample != null) {
				dataSample.capture(RequestStatus.SUCCESS);
			}
		}
		catch (Exception e) {
			if (dataSample != null) {
				dataSample.capture(RequestStatus.ERROR);
			}

			if (e instanceof IOException) {
				throw (IOException)e;
			}
			else if (e instanceof ServletException) {
				throw (ServletException)e;
			}
			else {
				throw new ServletException("Unable to execute request", e);
			}
		}
		finally {
			if (dataSample != null) {
				DataSampleThreadLocal.addDataSample(dataSample);
			}

			MessageBusUtil.sendMessage(
				DestinationNames.MONITORING,
				DataSampleThreadLocal.getDataSamples());
		}
	}

	@Reference(unbind = "-")
	protected void setDataSampleFactory(DataSampleFactory dataSampleFactory) {
		_dataSampleFactory = dataSampleFactory;
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void setLayoutLocalService(
		LayoutLocalService layoutLocalService) {

		_layoutLocalService = layoutLocalService;
	}

	@Reference(unbind = "-")
	protected final void setPortletMonitoringControl(
		PortletMonitoringControl portletMonitoringControl) {

		_portletMonitoringControl = portletMonitoringControl;
	}

	@Reference(unbind = "-")
	protected void setServiceMonitoringControl(
		ServiceMonitoringControl serviceMonitoringControl) {

		_serviceMonitoringControl = serviceMonitoringControl;
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	protected void unsetGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = null;
	}

	protected void unsetLayoutLocalService(
		LayoutLocalService layoutLocalService) {

		_layoutLocalService = null;
	}

	protected void unsetUserLocalService(UserLocalService userLocalService) {
		_userLocalService = null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MonitoringFilter.class);

	private DataSampleFactory _dataSampleFactory;
	private volatile GroupLocalService _groupLocalService;
	private volatile LayoutLocalService _layoutLocalService;
	private boolean _monitorPortalRequest;
	private PortletMonitoringControl _portletMonitoringControl;
	private ServiceMonitoringControl _serviceMonitoringControl;
	private volatile UserLocalService _userLocalService;

}
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

package com.liferay.portal.systemevents;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.systemevents.SystemEvent;
import com.liferay.portal.kernel.systemevents.SystemEventHierarchyEntry;
import com.liferay.portal.kernel.systemevents.SystemEventHierarchyEntryThreadLocal;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.GroupedModel;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.model.SystemEventConstants;
import com.liferay.portal.service.SystemEventLocalServiceUtil;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;

import java.io.Serializable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Zsolt Berentey
 */
public class SystemEventAdvice
	extends AnnotationChainableMethodAdvice<SystemEvent> {

	@Override
	public void afterReturning(MethodInvocation methodInvocation, Object result)
		throws Throwable {

		SystemEvent systemEvent = findAnnotation(methodInvocation);

		if ((systemEvent == _nullSystemEvent) || !systemEvent.sendEvent()) {
			return;
		}

		Method method = methodInvocation.getMethod();

		Class<?> parameter = method.getParameterTypes()[0];

		if (!GroupedModel.class.isAssignableFrom(parameter)) {
			return;
		}

		GroupedModel groupedModel =
			(GroupedModel)methodInvocation.getArguments()[0];

		Serializable primaryKeyObj = groupedModel.getPrimaryKeyObj();

		if (!(primaryKeyObj instanceof Long)) {
			return;
		}

		SystemEventHierarchyEntry systemEventHierarchyEntry =
			SystemEventHierarchyEntryThreadLocal.peek();

		if (systemEventHierarchyEntry != null) {
			SystemEventLocalServiceUtil.addSystemEvent(
				groupedModel.getGroupId(),
				systemEventHierarchyEntry.getClassName(), (Long)primaryKeyObj,
				systemEventHierarchyEntry.getUuid(), systemEvent.type(),
				systemEventHierarchyEntry.getExtraData());
		}
		else {
			SystemEventLocalServiceUtil.addSystemEvent(
				groupedModel.getGroupId(), groupedModel.getModelClassName(),
				(Long)primaryKeyObj, getUuid(groupedModel), systemEvent.type());
		}
	}

	@Override
	public Object before(MethodInvocation methodInvocation) throws Throwable {
		SystemEvent systemEvent = findAnnotation(methodInvocation);

		if (systemEvent == _nullSystemEvent) {
			return null;
		}

		Method method = methodInvocation.getMethod();

		Class<?> parameter = method.getParameterTypes()[0];

		if (!GroupedModel.class.isAssignableFrom(parameter)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"The first parameter of " + methodInvocation +
						" is not a grouped model");
			}

			return null;
		}

		if (systemEvent.action() != SystemEventConstants.ACTION_NONE) {
			GroupedModel groupedModel =
				(GroupedModel)methodInvocation.getArguments()[0];

			Serializable primaryKeyObj = groupedModel.getPrimaryKeyObj();

			if (!(primaryKeyObj instanceof Long)) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"The first parameter of " + methodInvocation +
							" has a non-long primary key");
				}

				return null;
			}

			SystemEventHierarchyEntry systemEventHierarchyEntry =
				SystemEventHierarchyEntryThreadLocal.push(
					groupedModel.getModelClass(),
					(Long)groupedModel.getPrimaryKeyObj(),
					systemEvent.action());

			systemEventHierarchyEntry.setUuid(getUuid(groupedModel));
		}

		return null;
	}

	@Override
	public void duringFinally(MethodInvocation methodInvocation) {
		SystemEvent systemEvent = findAnnotation(methodInvocation);

		if (systemEvent == _nullSystemEvent) {
			return;
		}

		Method method = methodInvocation.getMethod();

		Class<?> parameter = method.getParameterTypes()[0];

		if (!GroupedModel.class.isAssignableFrom(parameter)) {
			return;
		}

		if (systemEvent.action() != SystemEventConstants.ACTION_NONE) {
			GroupedModel groupedModel =
				(GroupedModel)methodInvocation.getArguments()[0];

			Serializable primaryKeyObj = groupedModel.getPrimaryKeyObj();

			if (!(primaryKeyObj instanceof Long)) {
				return;
			}

			SystemEventHierarchyEntry hierarchyEntry =
				SystemEventHierarchyEntryThreadLocal.peek();

			if (hierarchyEntry != null) {
				Class<?> modelClass = groupedModel.getModelClass();
				long primaryKey = (Long)groupedModel.getPrimaryKeyObj();

				if (hierarchyEntry.isAsset(modelClass.getName(), primaryKey)) {
					SystemEventHierarchyEntryThreadLocal.pop();
				}
			}
		}
	}

	@Override
	public SystemEvent getNullAnnotation() {
		return _nullSystemEvent;
	}

	protected String getUuid(GroupedModel groupedModel) throws Exception {
		String uuid;

		if (groupedModel instanceof StagedModel) {
			uuid = ((StagedModel)groupedModel).getUuid();
		}
		else {
			Class<?> modelClass = groupedModel.getClass();

			Method uuidMethod = modelClass.getMethod("getUuid", new Class[0]);

			if (uuidMethod != null) {
				uuid = (String)uuidMethod.invoke(groupedModel, new Object[0]);
			}
			else {
				uuid = StringPool.BLANK;
			}
		}

		return uuid;
	}

	private static Log _log = LogFactoryUtil.getLog(SystemEventAdvice.class);

	private static SystemEvent _nullSystemEvent =
		new SystemEvent() {

			@Override
			public int action() {
				return 0;
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return SystemEvent.class;
			}

			@Override
			public boolean sendEvent() {
				return false;
			}

			@Override
			public int type() {
				return 0;
			}

		};

}
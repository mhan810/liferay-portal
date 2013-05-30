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

package com.liferay.portlet.social.util;

import com.liferay.portal.kernel.util.AutoResetThreadLocal;
import com.liferay.portal.util.PortalUtil;

import java.util.Stack;

/**
 * @author Zsolt Berentey
 */
public class SocialActivityHierarchyThreadLocal {

	public static void clear() {
		Stack<SocialActivityHierarchy> activityHierarchies =
			_activityHierarchies.get();

		activityHierarchies.clear();
	}

	public static SocialActivityHierarchy peek() {
		Stack<SocialActivityHierarchy> activityHierarchies =
			_activityHierarchies.get();

		if (activityHierarchies.isEmpty()) {
			return null;
		}

		return activityHierarchies.peek();
	}

	public static SocialActivityHierarchy pop() {
		Stack<SocialActivityHierarchy> activityHierarchies =
			_activityHierarchies.get();

		if (activityHierarchies.isEmpty()) {
			return null;
		}

		return activityHierarchies.pop();
	}

	public static void push(Class<?> clazz, long classPK) {
		long classNameId = PortalUtil.getClassNameId(clazz);

		push(classNameId, classPK);
	}

	public static void push(long classNameId, long classPK) {
		Stack<SocialActivityHierarchy> activityHierarchies =
			_activityHierarchies.get();

		activityHierarchies.push(
			new SocialActivityHierarchy(classNameId, classPK));
	}

	public static void push(String className, long classPK) {
		long classNameId = PortalUtil.getClassNameId(className);

		push(classNameId, classPK);
	}

	private static ThreadLocal<Stack<SocialActivityHierarchy>>
		_activityHierarchies =
			new AutoResetThreadLocal<Stack<SocialActivityHierarchy>>(
				SocialActivityHierarchyThreadLocal.class +
					"._activityHierarchy",
				new Stack<SocialActivityHierarchy>());

}
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

package com.liferay.portal.tools.releasecheck;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.spring.aop.Skip;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.ReleaseConstants;
import com.liferay.portal.spring.aop.ServiceBeanAopCacheManager;
import com.liferay.portal.spring.aop.ServiceBeanAopCacheManagerUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
public class ReleaseChecker {

	protected void checkReleaseState(int state) throws Exception {
		if (state == ReleaseConstants.STATE_GOOD) {
			return;
		}

		StringBundler sb = new StringBundler(6);

		sb.append("The database contains changes from a previous ");
		sb.append("upgrade attempt that failed. Please restore the old ");
		sb.append("database and file system and retry the upgrade. A ");
		sb.append("patch may be required if the upgrade failed due to a");
		sb.append(" bug or an unforeseen data permutation that resulted ");
		sb.append("from a corrupt database.");

		throw new IllegalStateException(sb.toString());
	}

	protected void disableTransactions() throws Exception {
		if (_log.isDebugEnabled()) {
			_log.debug("Disable transactions");
		}

		PropsValues.SPRING_HIBERNATE_SESSION_DELEGATED = false;

		Field field = ReflectionUtil.getDeclaredField(
			ServiceBeanAopCacheManager.class, "_annotations");

		field.set(
			null,
			new HashMap<MethodInvocation, Annotation[]>() {

				@Override
				public Annotation[] get(Object key) {
					return _annotations;
				}

				private Annotation[] _annotations = new Annotation[] {
					new Skip() {

						@Override
						public Class<? extends Annotation> annotationType() {
							return Skip.class;
						}

					}
				};

			}
		);
	}

	protected void enableTransactions() throws Exception {
		if (_log.isDebugEnabled()) {
			_log.debug("Enable transactions");
		}

		PropsValues.SPRING_HIBERNATE_SESSION_DELEGATED = GetterUtil.getBoolean(
			PropsUtil.get(PropsKeys.SPRING_HIBERNATE_SESSION_DELEGATED));

		Field field = ReflectionUtil.getDeclaredField(
			ServiceBeanAopCacheManager.class, "_annotations");

		field.set(
                null, new ConcurrentHashMap<MethodInvocation, Annotation[]>());

		ServiceBeanAopCacheManagerUtil.reset();
	}

	protected void updateReleaseState(int state) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataAccess.getConnection();

			ps = con.prepareStatement(
				"update Release_ set modifiedDate = ?, state_ = ? where " +
					"releaseId = ?");

			ps.setDate(1, new Date(System.currentTimeMillis()));
			ps.setInt(2, state);
			ps.setLong(3, ReleaseConstants.DEFAULT_ID);

			ps.executeUpdate();
		}
		finally {
			DataAccess.cleanUp(con, ps);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(ReleaseChecker.class);

}
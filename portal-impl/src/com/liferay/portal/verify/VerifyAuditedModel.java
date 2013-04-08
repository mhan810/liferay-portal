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

package com.liferay.portal.verify;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.security.auth.FullNameGenerator;
import com.liferay.portal.security.auth.FullNameGeneratorFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * @author Michael C. Han
 */
public class VerifyAuditedModel extends VerifyProcess {

	@Override
	protected void doVerify() throws Exception {
		for (String[] model : _MODELS) {
			verifyModel(model[0], model[1]);
		}
	}

	protected UserHolder getDefaultUser(Connection con, long companyId)
		throws Exception {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement(
				"select firstName, middleName, lastName, userId from User_ " +
					"where edfaultUser = 1 and companyId = ?");

			ps.setLong(1, companyId);

			rs = ps.executeQuery();

			if (rs.next()) {
				String firstName = rs.getString("firstName");
				String middleName = rs.getString("middleName");
				String lastName = rs.getString("lastName");
				long userId = rs.getLong("userId");

				FullNameGenerator fullNameGenerator =
					FullNameGeneratorFactory.getInstance();

				String fullName = fullNameGenerator.getFullName(
					firstName, middleName, lastName);

				return new UserHolder(userId, fullName);
			}

			throw new IllegalStateException(
				"No default user found for company: " + companyId);
		}
		finally {
			DataAccess.cleanUp(null, ps, rs);
		}
	}

	protected void verifyModel(String modelName, String pkColumnName)
		throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"select " + pkColumnName + ", companyId from " +
					modelName + " where userName is null order by companyId");

			rs = ps.executeQuery();

			Timestamp createDate = new Timestamp(System.currentTimeMillis());

			UserHolder userHolder = null;

			long previousCompanyId = 0;

			while (rs.next()) {
				long companyId = rs.getLong("companyId");

				long tablePrimaryKey = rs.getLong(pkColumnName);

				if (previousCompanyId != companyId) {
					userHolder = getDefaultUser(con, companyId);
				}

				ps = con.prepareStatement(
					"update " + modelName + " set userId = ?, userName = ?, " +
						"createDate = ?, modifiedDate = ? where " +
						pkColumnName + " = ?");

				ps.setLong(1, userHolder.getUserId());
				ps.setString(2, userHolder.getUserName());
				ps.setTimestamp(3, createDate);
				ps.setTimestamp(4, createDate);
				ps.setLong(5, tablePrimaryKey);

				ps.executeUpdate();

				previousCompanyId = companyId;
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	private static final String[][] _MODELS = new String[][] {
		new String[] {
			"Organization_", "organizationId"
		},
		new String[] {
			"Role_", "roleId"
		},
		new String[] {
			"UserGroup", "userGroupId"
		},
	};
	private static class UserHolder {

		public UserHolder(long userId, String userName) {
			_userId = userId;
			_userName = userName;
		}

		public long getUserId() {
			return _userId;
		}

		public String getUserName() {
			return _userName;
		}

		private long _userId;
		private String _userName;

	}


}
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

package com.liferay.portal.upgrade.v7_0_0.util;

import java.sql.Types;

/**
 * @author	  Brian Wing Shun Chan
 * @generated
 */
public class OrganizationTable {

	public static final String TABLE_NAME = "Organization_";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT},
		{"uuid_", Types.VARCHAR},
		{"organizationId", Types.BIGINT},
		{"companyId", Types.BIGINT},
		{"userId", Types.BIGINT},
		{"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP},
		{"parentOrganizationId", Types.BIGINT},
		{"treePath", Types.VARCHAR},
		{"name", Types.VARCHAR},
		{"type_", Types.VARCHAR},
		{"recursable", Types.BOOLEAN},
		{"regionId", Types.BIGINT},
		{"countryId", Types.BIGINT},
		{"statusId", Types.BIGINT},
		{"comments", Types.VARCHAR},
		{"logoId", Types.BIGINT}
	};

	public static final String TABLE_SQL_CREATE = "create table Organization_ (mvccVersion LONG default 0,uuid_ VARCHAR(75) null,organizationId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,parentOrganizationId LONG,treePath STRING null,name VARCHAR(100) null,type_ VARCHAR(75) null,recursable BOOLEAN,regionId LONG,countryId LONG,statusId LONG,comments STRING null,logoId LONG)";

	public static final String TABLE_SQL_DROP = "drop table Organization_";

	public static final String[] TABLE_SQL_ADD_INDEXES = {
		"create unique index IX_E301BDF5 on Organization_ (companyId, name)",
		"create index IX_418E4522 on Organization_ (companyId, parentOrganizationId)",
		"create index IX_A9D85BA6 on Organization_ (uuid_, companyId)"
	};

}
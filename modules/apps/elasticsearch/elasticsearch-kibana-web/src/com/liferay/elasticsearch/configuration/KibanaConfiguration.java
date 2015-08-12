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

package com.liferay.elasticsearch.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
@Meta.OCD(id = "com.liferay.elasticsearch.configuration.KibanaConfiguration")
public interface KibanaConfiguration {

	@Meta.AD(deflt = "discover", required = false)
	public String defaultAppId();

	@Meta.AD(deflt = "true", required = false)
	public boolean elasticsearchPreserveHost();

	@Meta.AD(deflt = "http://localhost:9200", required = false)
	public String elasticsearchUrl();

	@Meta.AD(deflt = "localhost", required = false)
	public String host();

	@Meta.AD(deflt = "", required = false)
	public String kibanaHomeFolder();

	@Meta.AD(deflt = ".kibana", required = false)
	public String kibanaIndex();

	@Meta.AD(deflt = "5601", required = false)
	public int port();

	@Meta.AD(deflt = "300000", required = false)
	public long requestTimeout();

	@Meta.AD(deflt = "0", required = false)
	public long shardTimeout();

	@Meta.AD(deflt = "true", required = false)
	public boolean verifySsl();

}
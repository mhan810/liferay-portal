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

package com.liferay.portal.scripting.ruby.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * @author Michael C. Han
 */
@Meta.OCD(
	id = "com.liferay.portal.scripting.ruby.configuration.JRubyScriptingConfiguration"
)
public interface JRubyScriptingConfiguration {

	@Meta.AD(
		deflt = "jit", optionValues = {"force", "jit", "none"},
		required = false
	)
	public String compileMode();

	@Meta.AD(deflt = "5", required = false)
	public int compileThreshold();

	@Meta.AD(
		deflt =
			"classpath:/META-INF/jruby.home/lib/ruby/site_ruby/1.8," +
			"classpath:/META-INF/jruby.home/lib/ruby/site_ruby/shared," +
			"classpath:/META-INF/jruby.home/lib/ruby/1.8," +
			"classpath:/gems/chunky_png-1.3.4/lib," +
			"classpath:/gems/compass-1.0.1/lib," +
			"classpath:/gems/compass-core-1.0.3/lib," +
			"classpath:/gems/compass-import-once-1.0.5/lib," +
			"classpath:/gems/ffi-1.9.6-java/lib," +
			"classpath:/gems/multi_json-1.10.1/lib," +
			"classpath:/gems/rb-fsevent-0.9.4/lib," +
			"classpath:/gems/rb-inotify-0.9.5/lib," +
			"classpath:/gems/sass-3.4.13/lib," +
			"${java.io.tmpdir}/liferay/ruby/gems/chunky_png-1.3.4/lib," +
			"${java.io.tmpdir}/liferay/ruby/gems/compass-1.0.1/lib," +
			"${java.io.tmpdir}/liferay/ruby/gems/compass-core-1.0.3/lib," +
			"${java.io.tmpdir}/liferay/ruby/gems/compass-import-once-1.0.5/lib," +
			"${java.io.tmpdir}/liferay/ruby/gems/ffi-1.9.6-java/lib," +
			"${java.io.tmpdir}/liferay/ruby/gems/multi_json-1.10.1/lib," +
			"${java.io.tmpdir}/liferay/ruby/gems/rb-fsevent-0.9.4/lib," +
			"${java.io.tmpdir}/liferay/ruby/gems/rb-inotify-0.9.5/lib," +
			"${java.io.tmpdir}/liferay/ruby/gems/sass-3.4.13/lib",
		required = false
	)
	public String loadPaths();

}
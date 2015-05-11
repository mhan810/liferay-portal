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
package com.liferay.portal.scripting.ruby.internal;

import aQute.bnd.annotation.metatype.Configurable;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.scripting.BaseScriptingExecutor;
import com.liferay.portal.kernel.scripting.ExecutionException;
import com.liferay.portal.kernel.scripting.ScriptingException;
import com.liferay.portal.kernel.scripting.ScriptingExecutor;
import com.liferay.portal.kernel.util.AggregateClassLoader;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.NamedThreadFactory;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.scripting.ruby.configuration.JRubyScriptingConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;

import javax.servlet.ServletContext;

import jodd.io.ZipUtil;

import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.RubyInstanceConfig.CompileMode;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;
import org.jruby.embed.internal.LocalContextProvider;
import org.jruby.exceptions.RaiseException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alberto Montero
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = {"scripting.language=" + RubyExecutor.LANGUAGE},
	service = ScriptingExecutor.class
)
public class RubyExecutor extends BaseScriptingExecutor {

	public static final String LANGUAGE = "ruby";

	public static void initRubyGems(ServletContext servletContext) {
		File rubyGemsJarFile = new File(
			servletContext.getRealPath("/WEB-INF/lib/ruby-gems.jar"));

		if (!rubyGemsJarFile.exists()) {
			if (_log.isWarnEnabled()) {
				_log.warn(rubyGemsJarFile + " does not exist");
			}

			return;
		}

		String tmpDir = SystemProperties.get(SystemProperties.TMP_DIR);

		File rubyDir = new File(tmpDir + "/liferay/ruby");

		if (!rubyDir.exists() ||
			(rubyDir.lastModified() < rubyGemsJarFile.lastModified())) {

			FileUtil.deltree(rubyDir);

			try {
				FileUtil.mkdirs(rubyDir);

				ZipUtil.unzip(rubyGemsJarFile, rubyDir);

				rubyDir.setLastModified(rubyGemsJarFile.lastModified());
			}
			catch (IOException ioe) {
				_log.error(
					"Unable to unzip " + rubyGemsJarFile + " to " + rubyDir,
					ioe);
			}
		}
	}

	@Deactivate
	public void deactivate() {
		_scriptingContainer.terminate();

		_jRubyScriptingConfiguration = null;
	}

	@Override
	public Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, File scriptFile,
			ClassLoader... classLoaders)
		throws ScriptingException {

		return eval(
			allowedClasses, inputObjects, outputNames, scriptFile, null,
			classLoaders);
	}

	@Override
	public Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, String script, ClassLoader... classLoaders)
		throws ScriptingException {

		return eval(
			allowedClasses, inputObjects, outputNames, null, script,
			classLoaders);
	}

	@Override
	public String getLanguage() {
		return LANGUAGE;
	}

	public ScriptingContainer getScriptingContainer() {
		return _scriptingContainer;
	}

	public void setExecuteInSeparateThread(boolean executeInSeparateThread) {
		_executeInSeparateThread = executeInSeparateThread;
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_jRubyScriptingConfiguration = Configurable.createConfigurable(
			JRubyScriptingConfiguration.class, properties);

		_scriptingContainer = new ScriptingContainer(
			LocalContextScope.THREADSAFE);

		LocalContextProvider localContextProvider =
			_scriptingContainer.getProvider();

		RubyInstanceConfig rubyInstanceConfig =
			localContextProvider.getRubyInstanceConfig();

		if (_jRubyScriptingConfiguration.compileMode().equals(
				_COMPILE_MODE_FORCE)) {

			rubyInstanceConfig.setCompileMode(CompileMode.FORCE);
		}
		else if (_jRubyScriptingConfiguration.compileMode().equals(
					_COMPILE_MODE_JIT)) {

			rubyInstanceConfig.setCompileMode(CompileMode.JIT);
		}

		rubyInstanceConfig.setJitThreshold(
			_jRubyScriptingConfiguration.compileThreshold());
		rubyInstanceConfig.setLoader(PortalClassLoaderUtil.getClassLoader());

		String[] loadPaths = StringUtil.split(
			_jRubyScriptingConfiguration.loadPaths(), StringPool.COMMA);

		_loadPaths = new ArrayList<>(Arrays.asList(loadPaths));

		rubyInstanceConfig.setLoadPaths(_loadPaths);

		_scriptingContainer.setCurrentDirectory(_basePath);
	}

	protected Map<String, Object> doEval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, File scriptFile, String script,
			ClassLoader... classLoaders)
		throws ScriptingException {

		if (allowedClasses != null) {
			throw new ExecutionException(
				"Constrained execution not supported for Ruby");
		}

		try {
			LocalContextProvider localContextProvider =
				_scriptingContainer.getProvider();

			RubyInstanceConfig rubyInstanceConfig =
				localContextProvider.getRubyInstanceConfig();

			rubyInstanceConfig.setCurrentDirectory(_basePath);

			if (ArrayUtil.isNotEmpty(classLoaders)) {
				ClassLoader aggregateClassLoader =
					AggregateClassLoader.getAggregateClassLoader(
						PortalClassLoaderUtil.getClassLoader(), classLoaders);

				rubyInstanceConfig.setLoader(aggregateClassLoader);
			}

			rubyInstanceConfig.setLoadPaths(_loadPaths);

			for (Map.Entry<String, Object> entry : inputObjects.entrySet()) {
				String inputName = entry.getKey();
				Object inputObject = entry.getValue();

				if (!inputName.startsWith(StringPool.DOLLAR)) {
					inputName = StringPool.DOLLAR + inputName;
				}

				_scriptingContainer.put(inputName, inputObject);
			}

			if (scriptFile != null) {
				_scriptingContainer.runScriptlet(
					new FileInputStream(scriptFile), scriptFile.toString());
			}
			else {
				_scriptingContainer.runScriptlet(script);
			}

			if (outputNames == null) {
				return null;
			}

			Map<String, Object> outputObjects = new HashMap<>();

			for (String outputName : outputNames) {
				outputObjects.put(
					outputName, _scriptingContainer.get(outputName));
			}

			return outputObjects;
		}
		catch (RaiseException re) {
			throw new ScriptingException(
				re.getException().message.asJavaString() + "\n\n", re);
		}
		catch (FileNotFoundException fnfe) {
			throw new ScriptingException(fnfe);
		}
		finally {
			try {
				_globalRuntimeField.set(null, null);
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}
	}

	protected Map<String, Object> eval(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, File scriptFile, String script,
			ClassLoader... classLoaders)
		throws ScriptingException {

		if (!_executeInSeparateThread) {
			return doEval(
				allowedClasses, inputObjects, outputNames, scriptFile, script,
				classLoaders);
		}

		EvalCallable evalCallable = new EvalCallable(
			allowedClasses, inputObjects, outputNames, scriptFile, script,
			classLoaders);

		FutureTask<Map<String, Object>> futureTask = new FutureTask<>(
			evalCallable);

		Thread oneTimeExecutorThread = _threadFactory.newThread(futureTask);

		oneTimeExecutorThread.start();

		try {
			oneTimeExecutorThread.join();

			return futureTask.get();
		}
		catch (Exception e) {
			futureTask.cancel(true);
			oneTimeExecutorThread.interrupt();

			throw new ScriptingException(e);
		}
	}

	@Reference(unbind = "-")
	protected void setProps(Props props) {
		_basePath = props.get(PropsKeys.LIFERAY_LIB_PORTAL_DIR);
	}

	private static final String _COMPILE_MODE_FORCE = "force";

	private static final String _COMPILE_MODE_JIT = "jit";

	private static final Log _log = LogFactoryUtil.getLog(RubyExecutor.class);

	private static final Field _globalRuntimeField;
	private static final ThreadFactory _threadFactory = new NamedThreadFactory(
		RubyExecutor.class.getName(), Thread.NORM_PRIORITY,
		RubyExecutor.class.getClassLoader());

	static {
		try {
			_globalRuntimeField = ReflectionUtil.getDeclaredField(
				Ruby.class, "globalRuntime");
		}
		catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private String _basePath;
	private boolean _executeInSeparateThread = true;
	private volatile JRubyScriptingConfiguration _jRubyScriptingConfiguration;
	private List<String> _loadPaths;
	private ScriptingContainer _scriptingContainer;

	private class EvalCallable implements Callable<Map<String, Object>> {

		public EvalCallable(
			Set<String> allowedClasses, Map<String, Object> inputObjects,
			Set<String> outputNames, File scriptFile, String script,
			ClassLoader[] classLoaders) {

			_allowedClasses = allowedClasses;
			_inputObjects = inputObjects;
			_outputNames = outputNames;
			_scriptFile = scriptFile;
			_script = script;
			_classLoaders = classLoaders;
		}

		@Override
		public Map<String, Object> call() throws Exception {
			return doEval(
				_allowedClasses, _inputObjects, _outputNames, _scriptFile,
				_script, _classLoaders);
		}

		private final Set<String> _allowedClasses;
		private final ClassLoader[] _classLoaders;
		private final Map<String, Object> _inputObjects;
		private final Set<String> _outputNames;
		private final String _script;
		private final File _scriptFile;

	}

}
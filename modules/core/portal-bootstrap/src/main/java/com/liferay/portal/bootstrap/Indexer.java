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

package com.liferay.portal.bootstrap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.namespace.BundleNamespace;
import org.osgi.framework.namespace.HostNamespace;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.framework.namespace.PackageNamespace;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.resource.Capability;
import org.osgi.service.repository.ContentNamespace;

import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.FastDateFormatFactoryImpl;
import com.liferay.portal.util.FileImpl;
import com.liferay.portal.util.PropsImpl;
import com.liferay.portal.util.PropsValues;

import aQute.bnd.header.Attrs;
import aQute.bnd.header.Parameters;
import aQute.bnd.osgi.Constants;
import aQute.bnd.osgi.Jar;
import aQute.bnd.osgi.Verifier;
import aQute.bnd.osgi.resource.CapabilityBuilder;
import aQute.bnd.version.Version;
import aQute.lib.converter.Converter;
import aQute.lib.converter.TypeReference;

/**
 * @author Raymond Augé
 */
public class Indexer {

	public static void main(String[] args) throws Exception {

		// All ordering below is significant!

		FastDateFormatFactoryUtil fastDateFormatFactoryUtil =
			new FastDateFormatFactoryUtil();

		fastDateFormatFactoryUtil.setFastDateFormatFactory(
			new FastDateFormatFactoryImpl());

		FileUtil fileUtil = new FileUtil();

		fileUtil.setFile(new FileImpl());

		File tempFolder = FileUtil.createTempFolder();

		com.liferay.portal.util.PropsUtil.set(
			PropsKeys.MODULE_FRAMEWORK_STATE_DIR,
			tempFolder.getCanonicalPath());

		PropsUtil.setProps(new PropsImpl());

		String[] moduleFrameworkAutoDeployDirs = PropsUtil.getArray(
				PropsKeys.MODULE_FRAMEWORK_INITIAL_BUNDLES);

		for (int i = 0; i < moduleFrameworkAutoDeployDirs.length; i++) {
			if (moduleFrameworkAutoDeployDirs[i].endsWith("@start")) {
				moduleFrameworkAutoDeployDirs[i] =
					moduleFrameworkAutoDeployDirs[i].substring(
						0, moduleFrameworkAutoDeployDirs[i].length() - 6);
			}
		}

		com.liferay.portal.util.PropsUtil.set(
			PropsKeys.MODULE_FRAMEWORK_INITIAL_BUNDLES,
			StringUtil.merge(moduleFrameworkAutoDeployDirs));

		ModuleFrameworkImpl impl = new ModuleFrameworkImpl();

		impl.initFramework();
		impl.startFramework();

		Framework framework = impl.getFramework();

		BundleContext bundleContext = framework.getBundleContext();

		Bundle systemBundle = bundleContext.getBundle(0);

		String bsn = "com.liferay.portal";
		String version = ReleaseInfo.getVersion();

		File output = new File(
			PropsValues.LIFERAY_HOME, bsn + "-" + version + ".jar");

		File parentFile = output.getParentFile();

		if ((parentFile == null) || !parentFile.isDirectory()) {
			System.err.printf(
				"Cannot write to %s because parent not a directory\n", output);
		}

		if (output.isFile() && !output.canWrite()) {
			System.err.printf("Cannot write to %s\n", output);
		}

		System.out.printf("Starting distro %s;%s\n", bsn, version);

		Parameters packages = new Parameters();
		List<Parameters> provided = new ArrayList<>();

		BundleRevision bundleRevision = systemBundle.adapt(
			BundleRevision.class);

		for (Capability capability : bundleRevision.getCapabilities(null)) {
			String namespace = capability.getNamespace();

			CapabilityBuilder cb = new CapabilityBuilder(namespace);

			Map<String, Object> attributes = capability.getAttributes();

			for (Entry<String,Object> entry : attributes.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (key.equals(Constants.VERSION_ATTRIBUTE)) {
					if (value instanceof Collection ||
						value.getClass().isArray()) {

						value = Converter.cnv(versionTypeReference, value);
					}
					else if (value instanceof org.osgi.framework.Version) {
						value = ((org.osgi.framework.Version)value).toString();
					}
				}

				cb.addAttribute(key, value);
			}

			cb.addDirectives(capability.getDirectives());

			Attrs attrs = cb.toAttrs();

			if (cb.isPackage()) {
				attrs.remove(Constants.BUNDLE_SYMBOLIC_NAME_ATTRIBUTE);
				attrs.remove(Constants.BUNDLE_VERSION_ATTRIBUTE);

				String packageName = attrs.remove(
					PackageNamespace.PACKAGE_NAMESPACE);

				if (packageName == null) {
					System.err.printf(
						"Invalid package capability found %s\n", capability);
				}
				else {
					packages.put(packageName, attrs);
				}

				System.out.printf("P: %s;%s\n", packageName, attrs);
			}
			else if (!ignoredNamespaces.contains(namespace)) {
				System.out.printf("C %s;%s\n", namespace, attrs);

				Parameters parameters = new Parameters();

				parameters.put(namespace, attrs);

				provided.add(parameters);
			}
		}

		Manifest manifest = new Manifest();
		Attributes mainAttributes = manifest.getMainAttributes();

		mainAttributes.putValue(Constants.BUNDLE_MANIFESTVERSION, "2");
		mainAttributes.putValue(Constants.BUNDLE_SYMBOLICNAME, bsn);
		mainAttributes.putValue(Constants.BUNDLE_VERSION, version);
		mainAttributes.putValue(Constants.EXPORT_PACKAGE, packages.toString().replaceAll("version:Version", "version"));

		StringBuilder sb = new StringBuilder();

		for (Parameters parameter : provided) {
			sb.append(parameter.toString());
			sb.append(",");
		}

		String capabilities = sb.toString().substring(0, sb.length() - 1);

		mainAttributes.putValue(Constants.PROVIDE_CAPABILITY, capabilities);
		mainAttributes.putValue(
			Constants.BUNDLE_DESCRIPTION, ReleaseInfo.getReleaseInfo());
		mainAttributes.putValue(
			Constants.BUNDLE_LICENSE,
			"http://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt");
		mainAttributes.putValue(
			Constants.BUNDLE_COPYRIGHT,
			"Copyright (c) 2000-present All rights reserved.");
		mainAttributes.putValue(
			Constants.BUNDLE_VENDOR, ReleaseInfo.getVendor());

		Jar jar = new Jar("distro");

		jar.setManifest(manifest);

		try (Verifier v = new Verifier(jar)) {
			v.setProperty(
				Constants.FIXUPMESSAGES,
				"osgi.* namespace must not be specified with generic " +
				"capabilities");

			v.verify();
			v.getErrors();

			if (v.isOk()) {
				jar.write(output);
			}
			else {
				for (String error : v.getErrors()) {
					System.err.printf("[ERROR] %s\n", error);
				}
			}
		}
		finally {
			impl.stopFramework(0);
		}
	}

	private static final TypeReference<List<String>> versionTypeReference =
		new TypeReference<List<String>>() {};

	private static final Set<String> ignoredNamespaces = new HashSet<>();

	static {
		ignoredNamespaces.add(PackageNamespace.PACKAGE_NAMESPACE);
		ignoredNamespaces.add(HostNamespace.HOST_NAMESPACE);
		ignoredNamespaces.add(BundleNamespace.BUNDLE_NAMESPACE);
		ignoredNamespaces.add(IdentityNamespace.IDENTITY_NAMESPACE);
		ignoredNamespaces.add(ContentNamespace.CONTENT_NAMESPACE);
	}

}
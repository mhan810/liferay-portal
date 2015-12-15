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

package com.liferay.portal.tools.deploy;

import static org.junit.Assert.assertTrue;

import aQute.bnd.osgi.Constants;

import com.liferay.portal.deploy.auto.ModuleAutoDeployListener;
import com.liferay.portal.kernel.deploy.auto.AutoDeployException;
import com.liferay.portal.kernel.deploy.auto.context.AutoDeploymentContext;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.util.PropsUtil;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.text.MessageFormat;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.junit.Test;

/**
 * @author Gregory Amerson
 */
public class ModuleAutoDeployerListenerTest extends BaseDeployerTestCase {

	@Override
	public BaseDeployer getDeployer() {
		return null;
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();

		PropsUtil.removeProperties(PropsUtil.getProperties());

		String moduleFrameworkAutoDeployDirs = MessageFormat.format(
			"{0},{1}", getConfigsDir(), getModulesDir());

		PropsUtil.set(
			PropsKeys.MODULE_FRAMEWORK_AUTO_DEPLOY_DIRS,
			moduleFrameworkAutoDeployDirs);
	}

	@Test
	public void testDeployModuleFile() throws Exception {
		File tempDir = Files.createTempDirectory(null).toFile();

		File manifestFile =
			new File(tempDir, "META-INF/MANIFEST.MF");

		manifestFile.getParentFile().mkdirs();

		FileWriter fileWriter = new FileWriter(manifestFile);

		fileWriter.write("Manifest-Version: 1\n");
		fileWriter.write("Bundle-ManifestVersion: 2\n");
		fileWriter.write(Constants.BUNDLE_SYMBOLICNAME + ": test\n");

		fileWriter.flush();

		fileWriter.close();

		File moduleFile =
			new File(Files.createTempDirectory(null).toFile(), "module.jar");

		zip(tempDir, moduleFile);

		deploy(moduleFile);

		assertTrue(new File(getModulesDir(), moduleFile.getName()).exists());
	}

	@Test
	public void testDeployWarFile() throws Exception {
		File tempDir = Files.createTempDirectory(null).toFile();

		tempDir.mkdirs();

		File liferayPluginPackageProperties =
			new File(tempDir, "WEB-INF/liferay-plugin-package.properties");

		liferayPluginPackageProperties.getParentFile().mkdirs();

		liferayPluginPackageProperties.createNewFile();

		File warFile =
			new File(Files.createTempDirectory(null).toFile(), "war.war");

		zip(tempDir, warFile);

		deploy(warFile);

		assertTrue(new File(getModulesDir(), warFile.getName()).exists());
	}

	@Test
	public void testDontDeployNonModuleFiles() throws Exception {
		File otherFile = File.createTempFile("other", ".txt");

		deploy(otherFile);

		assertTrue(!new File(getModulesDir(), otherFile.getName()).exists());
	}

	@Test
	public void testModuleAutoDeployListenerWabFalse() throws Exception {
		File tempDir = Files.createTempDirectory(null).toFile();

		tempDir.mkdirs();

		File liferayPluginPackageProperties =
			new File(tempDir, "WEB-INF/liferay-plugin-package.properties");

		liferayPluginPackageProperties.getParentFile().mkdirs();

		FileWriter fileWriter = new FileWriter(liferayPluginPackageProperties);

		fileWriter.write("wab=false");

		fileWriter.flush();

		fileWriter.close();

		File warFile =
			new File(Files.createTempDirectory(null).toFile(), "notawab.war");

		zip(tempDir, warFile);

		deploy(warFile);

		assertTrue(!(new File(getModulesDir(), warFile.getName()).exists()));
	}

	protected File getConfigsDir() {
		return new File(getRootDir(), "configs");
	}

	protected File getModulesDir() {
		return new File(getRootDir(), "modules");
	}

	private void deploy(File file) throws AutoDeployException {
		AutoDeploymentContext autoDeploymentContext =
			new AutoDeploymentContext();

		autoDeploymentContext.setFile(file);

		new ModuleAutoDeployListener().deploy(autoDeploymentContext);
	}

	private void zip(File baseDir, File destFile) {
		Zip zip = new Zip();
		zip.setBasedir(baseDir);
		zip.setDestFile(destFile);
		zip.setProject(new Project());

		zip.execute();
	}

}
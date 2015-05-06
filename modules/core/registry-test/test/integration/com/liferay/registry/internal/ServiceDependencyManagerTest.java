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

package com.liferay.registry.internal;

import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.dependency.ServiceDependencyListener;
import com.liferay.registry.dependency.ServiceDependencyManager;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Michael C. Han
 */
@RunWith(Arquillian.class)
public class ServiceDependencyManagerTest {
	@Before
	public void setUp() throws BundleException {
		_bundle.start();

		_registry = RegistryUtil.getRegistry();
	}

	@After
	public void tearDown() throws BundleException {
		_bundle.stop();
	}

	@Test
	public void testRegisterClassDependencies(){
		ServiceDependencyManager serviceDependencyManager =
			new ServiceDependencyManager();

		final AtomicBoolean dependenciesSatisfied = new AtomicBoolean(false);

		serviceDependencyManager.addServiceDependencyListener(
			new ServiceDependencyListener() {

				@Override
				public void dependenciesFulfilled() {
					dependenciesSatisfied.set(true);
				}

				@Override
				public void destroy() {
					throw new UnsupportedOperationException();

				}
			});

		serviceDependencyManager.registerDependencies(
			TrackedOne.class, TrackedTwo.class);

		_bundleContext.registerService(
			TrackedOne.class, new TrackedOne(),
			new HashMapDictionary<String, Object>());

		_bundleContext.registerService(
			TrackedTwo.class, new TrackedTwo(new TrackedOne()),
			new HashMapDictionary<String, Object>());

		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
			Assert.fail(e.toString());
		}

		Assert.assertTrue(dependenciesSatisfied.get());
	}

	@Test
	public void testRegisterFilterDependencies(){
		ServiceDependencyManager serviceDependencyManager =
			new ServiceDependencyManager();

		final AtomicBoolean dependenciesSatisfied = new AtomicBoolean(false);

		serviceDependencyManager.addServiceDependencyListener(
			new ServiceDependencyListener() {

				@Override
				public void dependenciesFulfilled() {
					dependenciesSatisfied.set(true);
				}

				@Override
				public void destroy() {
					throw new UnsupportedOperationException();

				}
			});

		Filter filter1 = _registry.getFilter(
			"(objectClass=" + TrackedOne.class.getName() + ")");
		Filter filter2 = _registry.getFilter(
			"(objectClass=" + TrackedTwo.class.getName() + ")");

		serviceDependencyManager.registerDependencies(
			filter1, filter2);

		_bundleContext.registerService(
			TrackedOne.class, new TrackedOne(),
			new HashMapDictionary<String, Object>());

		_bundleContext.registerService(
			TrackedTwo.class, new TrackedTwo(new TrackedOne()),
			new HashMapDictionary<String, Object>());

		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
			Assert.fail(e.toString());
		}

		Assert.assertTrue(dependenciesSatisfied.get());
	}

	@Test
	public void testRegisterFilterAndClassDependencies(){
		ServiceDependencyManager serviceDependencyManager =
			new ServiceDependencyManager();

		final AtomicBoolean dependenciesSatisfied = new AtomicBoolean(false);

		serviceDependencyManager.addServiceDependencyListener(
			new ServiceDependencyListener() {

				@Override
				public void dependenciesFulfilled() {
					dependenciesSatisfied.set(true);
				}

				@Override
				public void destroy() {
					throw new UnsupportedOperationException();

				}
			});

		Filter filter1 = _registry.getFilter(
			"(objectClass=" + TrackedOne.class.getName() + ")");

		serviceDependencyManager.registerDependencies(
			new Class[] { TrackedTwo.class }, new Filter[] { filter1 });

		_bundleContext.registerService(
			TrackedOne.class, new TrackedOne(),
			new HashMapDictionary<String, Object>());

		_bundleContext.registerService(
			TrackedTwo.class, new TrackedTwo(new TrackedOne()),
			new HashMapDictionary<String, Object>());

		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
			Assert.fail(e.toString());
		}

		Assert.assertTrue(dependenciesSatisfied.get());
	}

	@Test
	public void testWaitForDependencies(){
		final ServiceDependencyManager serviceDependencyManager =
			new ServiceDependencyManager();

		final AtomicBoolean dependenciesSatisfied = new AtomicBoolean(false);

		serviceDependencyManager.addServiceDependencyListener(
			new ServiceDependencyListener() {

				@Override
				public void dependenciesFulfilled() {
					dependenciesSatisfied.set(true);
				}

				@Override
				public void destroy() {
					throw new UnsupportedOperationException();

				}
			});

		Filter filter1 = _registry.getFilter(
			"(objectClass=" + TrackedOne.class.getName() + ")");
		Filter filter2 = _registry.getFilter(
			"(objectClass=" + TrackedTwo.class.getName() + ")");

		serviceDependencyManager.registerDependencies(
			filter1, filter2);

		_bundleContext.registerService(
			TrackedOne.class, new TrackedOne(),
			new HashMapDictionary<String, Object>());

		_bundleContext.registerService(
			TrackedTwo.class, new TrackedTwo(new TrackedOne()),
			new HashMapDictionary<String, Object>());

		Thread dependencyWaiter = new Thread(new Runnable() {
			@Override
			public void run() {
				serviceDependencyManager.waitForDependencies(100);
			}
		});

		dependencyWaiter.setDaemon(true);

		dependencyWaiter.start();

		try {
			Thread.sleep(250);

			if (dependencyWaiter.isAlive()) {
				Assert.fail("Dependencies should have been fulfilled");
			}

			Assert.assertTrue(dependenciesSatisfied.get());
		}
		catch (InterruptedException e) {
		}
	}

	@Test
	public void testWaitForDependenciesUnfilled(){
		final ServiceDependencyManager serviceDependencyManager =
			new ServiceDependencyManager();

		final AtomicBoolean dependenciesSatisfied = new AtomicBoolean(false);

		serviceDependencyManager.addServiceDependencyListener(
			new ServiceDependencyListener() {

				@Override
				public void dependenciesFulfilled() {
					dependenciesSatisfied.set(true);
				}

				@Override
				public void destroy() {
					throw new UnsupportedOperationException();

				}
			});

		Filter filter1 = _registry.getFilter(
			"(objectClass=" + TrackedOne.class.getName() + ")");
		Filter filter2 = _registry.getFilter(
			"(objectClass=" + TrackedTwo.class.getName() + ")");

		serviceDependencyManager.registerDependencies(
			filter1, filter2);

		_bundleContext.registerService(
			TrackedOne.class, new TrackedOne(),
			new HashMapDictionary<String, Object>());

		Thread dependencyWaiter = new Thread(new Runnable() {
			@Override
			public void run() {
				serviceDependencyManager.waitForDependencies(100);
			}
		});

		dependencyWaiter.setDaemon(true);

		dependencyWaiter.start();

		try {
			Thread.sleep(250);

			if (dependencyWaiter.isAlive()) {
				Assert.fail("Dependencies should have timed out");
			}

			Assert.assertFalse(dependenciesSatisfied.get());
		}
		catch (InterruptedException e) {
		}
	}

	@ArquillianResource
	private Bundle _bundle;

	@ArquillianResource
	private BundleContext _bundleContext;

	private Registry _registry;

}

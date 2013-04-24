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

package com.liferay.portal.kernel.nio.intraband.rpc;

import com.liferay.portal.kernel.io.Deserializer;
import com.liferay.portal.kernel.io.Serializer;
import com.liferay.portal.kernel.nio.intraband.CompletionHandler;
import com.liferay.portal.kernel.nio.intraband.Datagram;
import com.liferay.portal.kernel.nio.intraband.DatagramHelper;
import com.liferay.portal.kernel.nio.intraband.MockIntraband;
import com.liferay.portal.kernel.nio.intraband.MockRegistrationReference;
import com.liferay.portal.kernel.nio.intraband.RegistrationReference;
import com.liferay.portal.kernel.process.ProcessCallable;
import com.liferay.portal.kernel.test.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

import java.io.Serializable;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class IntrabandRPCUtilTest {

	@ClassRule
	public static CodeCoverageAssertor codeCoverageAssertor =
		new CodeCoverageAssertor();

	@Test
	public void testConstructor() {
		new IntrabandRPCUtil();
	}

	@Test
	public void testExecuteFail() {
		PortalClassLoaderUtil.setClassLoader(getClass().getClassLoader());

		MockIntraband mockIntraband = new MockIntraband() {

			@Override
			protected void doSendDatagram(
				RegistrationReference registrationReference,
				Datagram datagram) {

				throw new RuntimeException();
			}

		};

		try {
			IntrabandRPCUtil.execute(
				new MockRegistrationReference(mockIntraband),
				new TestProcessCallable());

			Assert.fail();
		}
		catch (IntrabandRPCException ibrpce) {
			Throwable throwable = ibrpce.getCause();

			Assert.assertSame(RuntimeException.class, throwable.getClass());
		}

		try {
			IntrabandRPCUtil.execute(
				new MockRegistrationReference(mockIntraband),
				new TestProcessCallable(), 1, TimeUnit.MILLISECONDS);

			Assert.fail();
		}
		catch (IntrabandRPCException ibrpce) {
			Throwable throwable = ibrpce.getCause();

			Assert.assertSame(RuntimeException.class, throwable.getClass());
		}
	}

	@Test
	public void testExecuteSuccess() throws IntrabandRPCException {
		PortalClassLoaderUtil.setClassLoader(getClass().getClassLoader());

		MockIntraband mockIntraband = new MockIntraband() {

			@Override
			protected void doSendDatagram(
				RegistrationReference registrationReference,
				Datagram datagram) {

				Deserializer deserializer = new Deserializer(
					datagram.getDataByteBuffer());

				try {
					ProcessCallable<Serializable> processCallable =
						deserializer.readObject();

					Serializable result = processCallable.call();

					Serializer serializer = new Serializer();

					serializer.writeObject(result);

					CompletionHandler<Object> completionHandler =
						DatagramHelper.getCompletionHandler(datagram);

					completionHandler.replied(
						null,
						Datagram.createResponseDatagram(
							datagram, serializer.toByteBuffer()));
				}
				catch (Exception e) {
					Assert.fail(e.getMessage());
				}
			}

		};

		MockRegistrationReference mockRegistrationReference =
			new MockRegistrationReference(mockIntraband);

		String result = IntrabandRPCUtil.execute(
			mockRegistrationReference, new TestProcessCallable());

		Assert.assertEquals(TestProcessCallable.class.getName(), result);

		result = IntrabandRPCUtil.execute(
			mockRegistrationReference, new TestProcessCallable(), 1,
			TimeUnit.MILLISECONDS);

		Assert.assertEquals(TestProcessCallable.class.getName(), result);
	}

	private static class TestProcessCallable
		implements ProcessCallable<String> {

		@Override
		public String call() {
			return TestProcessCallable.class.getName();
		}

		private static final long serialVersionUID = 1L;
	}

}
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

package com.liferay.dynamic.data.mapping.form.evaluator.functions;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class IsDecimalTest {

	@Test
	public void testEvaluateFalse() throws Exception {
		IsDecimal isDecimal = new IsDecimal();

		Assert.assertFalse((Boolean)isDecimal.evaluate("simple text"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid() throws Exception {
		IsDecimal isDecimal = new IsDecimal();

		isDecimal.evaluate("test", "test2");
	}

	@Test
	public void testEvaluateTrue() throws Exception {
		IsDecimal isDecimal = new IsDecimal();

		Assert.assertTrue((Boolean)isDecimal.evaluate("3"));
		Assert.assertTrue((Boolean)isDecimal.evaluate("4.76"));
		Assert.assertTrue((Boolean)isDecimal.evaluate("-50.67"));
	}

}
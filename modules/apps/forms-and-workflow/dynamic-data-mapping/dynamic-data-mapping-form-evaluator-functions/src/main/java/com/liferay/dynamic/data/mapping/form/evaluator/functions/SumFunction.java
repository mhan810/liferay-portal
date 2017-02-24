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

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "ddm.form.evaluator.function.name=sum",
	service = DDMExpressionFunction.class
)
public class SumFunction implements DDMExpressionFunction {

	@Override
	public Object evaluate(Object... parameters) {
		Object[] values = null;

		if ((parameters.length == 1) && parameters[0].getClass().isArray()) {
			values = (Object[])parameters[0];
		}
		else {
			values = parameters;
		}

		if (values.length < 2) {
			throw new IllegalArgumentException(
				"Two or more parameters are expected");
		}

		double sum = 0;

		boolean integerSum = true;

		for (Object value : values) {
			if (!Integer.class.isInstance(value)) {
				integerSum = false;
			}

			if (!Number.class.isInstance(value)) {
				throw new IllegalArgumentException(
					"The parameters should be numbers");
			}

			Number number = (Number)value;

			sum += number.doubleValue();
		}

		if (integerSum) {
			return (int)sum;
		}

		return sum;
	}

}
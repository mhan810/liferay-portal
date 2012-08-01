/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.service.persistence.lar;

import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Mate Thurzo
 */
public class ExportImportMessageAdvice implements MethodInterceptor {

	public static final String MESSAGE_COMMAND_DIGEST_START = "digest_start";
	public static final String MESSAGE_COMMAND_DIGEST_STOP = "digest_stop";

	public static final String MESSAGE_COMMAND_SERIALIZE_START =
		"serialize_start";
	public static final String MESSAGE_COMMAND_SERIALIZE_STOP =
		"serialize_stop";

	public static final String MESSAGE_COMMAND_IMPORT_START = "import_start";
	public static final String MESSAGE_COMMAND_IMPORT_STOP = "import_stop";

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		String methodName = methodInvocation.getMethod().getName();

		if (METHOD_NAME_DIGEST.equals(methodName)) {
			sendUpdateMessage(MESSAGE_COMMAND_DIGEST_START, null);
		}
		else if (METHOD_NAME_IMPORTDATA.equals(methodName)) {
			sendUpdateMessage(MESSAGE_COMMAND_IMPORT_START, null);
		}
		else if (METHOD_NAME_SERIALIZE.equals(methodName)) {
			sendUpdateMessage(MESSAGE_COMMAND_SERIALIZE_START, null);
		}

		Object returnValue = methodInvocation.proceed();

		if (METHOD_NAME_DIGEST.equals(methodName)) {
			sendUpdateMessage(MESSAGE_COMMAND_DIGEST_STOP, returnValue);
		}
		else if (METHOD_NAME_IMPORTDATA.equals(methodName)) {
			sendUpdateMessage(MESSAGE_COMMAND_IMPORT_STOP, returnValue);
		}
		else if (METHOD_NAME_SERIALIZE.equals(methodName)) {
			sendUpdateMessage(MESSAGE_COMMAND_SERIALIZE_STOP, returnValue);
		}

		return returnValue;
	}

	protected void sendUpdateMessage(String messageCommand, Object payload) {
		Message message = new Message();

		message.put("command", messageCommand);

		if (payload != null) {
			message.put("payload", payload);
		}

		MessageBusUtil.sendMessage(DestinationNames.LAR_EXPORT_IMPORT, message);
	}

	private static final String METHOD_NAME_DIGEST = "digest";
	private static final String METHOD_NAME_IMPORTDATA = "importData";
	private static final String METHOD_NAME_SERIALIZE = "serialize";

}

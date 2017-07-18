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

package com.liferay.poshi.runner.elements;

import com.liferay.poshi.runner.util.RegexUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Element;

/**
 * @author Kenji Heigel
 */
public class ExecuteElement extends PoshiElement {

	public ExecuteElement(Element element) {
		super("execute", element);
	}

	public ExecuteElement(String readableSyntax) {
		super("execute", readableSyntax);
	}

	public ExecuteElement(String name, Element element) {
		super(name, element);
	}

	public ExecuteElement(String name, String readableSyntax) {
		super(name, readableSyntax);
	}

	@Override
	public void parseReadableSyntax(String readableSyntax) {
		String executeType = "macro";

		String content = getParentheticalContent(readableSyntax);

		if (content.contains("locator1") || content.contains("locator2") ||
			content.contains("value1") || content.contains("value2")) {

			executeType = "function";
		}

		String executeCommandName = RegexUtil.getGroup(
			readableSyntax, "([^\\s]*)\\(", 1);

		executeCommandName = executeCommandName.replace(".", "#");

		addAttribute(executeType, executeCommandName);

		if (content.length() == 0) {
			return;
		}

		List<String> assignments = new ArrayList<>();

		Matcher matcher = _assignmentPattern.matcher(content);

		while (matcher.find()) {
			assignments.add(matcher.group());
		}

		for (String assignment : assignments) {
			assignment = assignment.trim();

			if (executeType.equals("macro")) {
				assignment = "var " + assignment;

				addElementFromReadableSyntax(assignment);

				continue;
			}

			String name = getNameFromAssignment(assignment);
			String value = getQuotedContent(assignment);

			addAttribute(name, value);
		}
	}

	@Override
	public String toReadableSyntax() {
		if (attributeValue("function") != null) {
			StringBuilder sb = new StringBuilder();

			for (PoshiElementAttribute poshiElementAttribute :
					toPoshiElementAttributes(attributeList())) {

				String name = poshiElementAttribute.getName();

				if (name.equals("function")) {
					continue;
				}

				sb.append(poshiElementAttribute.toReadableSyntax());
				sb.append(", ");
			}

			if (sb.length() > 2) {
				sb.setLength(sb.length() - 2);
			}

			return createReadableBlock(sb.toString());
		}

		String readableSyntax = super.toReadableSyntax();

		return createReadableBlock(readableSyntax);
	}

	@Override
	protected String createReadableBlock(String content) {
		StringBuilder sb = new StringBuilder();

		String blockName = getBlockName();
		String pad = getPad();

		sb.append("\n\n");
		sb.append(pad);
		sb.append(blockName.replace("#", "."));
		sb.append("(");

		String trimmedContent = content.trim();

		if (!trimmedContent.equals("")) {
			if (content.contains("\n")) {
				content = content.replaceAll("\n", ",\n" + pad);
				content = content.replaceFirst(",", "");
				content = content + "\n" + pad;
			}

			sb.append(content);
		}

		sb.append(");");

		return sb.toString();
	}

	@Override
	protected String getBlockName() {
		if (attributeValue("function") != null) {
			return attributeValue("function");
		}

		return attributeValue("macro");
	}

	private static final Pattern _assignmentPattern = Pattern.compile(
		"([^,]*? = \".*?\")");

}
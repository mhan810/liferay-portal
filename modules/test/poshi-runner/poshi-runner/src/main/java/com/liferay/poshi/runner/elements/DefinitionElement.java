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

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

/**
 * @author Kenji Heigel
 */
public class DefinitionElement extends PoshiElement {

	public DefinitionElement(Element element) {
		super("definition", element);
	}

	public DefinitionElement(String readableSyntax) {
		super("definition", readableSyntax);
	}

	@Override
	public void parseReadableSyntax(String readableSyntax) {
		for (String readableBlock : getReadableBlocks(readableSyntax)) {
			if (readableBlock.startsWith("@") &&
				!readableBlock.startsWith("@description") &&
				!readableBlock.startsWith("@priority")) {

				String name = getNameFromAssignment(readableBlock);
				String value = getQuotedContent(readableBlock);

				addAttribute(name, value);

				continue;
			}

			addElementFromReadableSyntax(readableBlock);
		}
	}

	@Override
	public String toReadableSyntax() {
		StringBuilder sb = new StringBuilder();

		for (PoshiElementAttribute poshiElementAttribute :
				toPoshiElementAttributes(attributeList())) {

			sb.append("\n@");

			sb.append(poshiElementAttribute.toReadableSyntax());
		}

		StringBuilder content = new StringBuilder();

		for (PoshiElement poshiElement :
				toPoshiElements(elements("property"))) {

			content.append(poshiElement.toReadableSyntax());
		}

		content.append("\n");

		for (PoshiElement poshiElement : toPoshiElements(elements("set-up"))) {
			content.append(poshiElement.toReadableSyntax());
		}

		content.append("\n");

		for (PoshiElement poshiElement :
				toPoshiElements(elements("tear-down"))) {

			content.append(poshiElement.toReadableSyntax());
		}

		for (PoshiElement poshiElement : toPoshiElements(elements("command"))) {
			content.append("\n");
			content.append(poshiElement.toReadableSyntax());
		}

		sb.append(createReadableBlock(content.toString()));

		String string = sb.toString();

		return string.trim();
	}

	@Override
	protected String getBlockName() {
		return "definition";
	}

	@Override
	protected String getPad() {
		return "";
	}

	protected List<String> getReadableBlocks(String readableSyntax) {
		StringBuilder sb = new StringBuilder();

		List<String> readableBlocks = new ArrayList<>();

		for (String line : readableSyntax.split("\n")) {
			line = line.trim();

			if (line.length() == 0) {
				continue;
			}

			if (line.startsWith("@") && !line.startsWith("@description") &&
				!line.startsWith("@priority")) {

				readableBlocks.add(line);

				continue;
			}

			if (line.startsWith("definition {")) {
				continue;
			}

			String readableBlock = sb.toString();

			readableBlock = readableBlock.trim();

			if (isValidReadableBlock(readableBlock)) {
				readableBlocks.add(readableBlock);

				sb.setLength(0);
			}

			sb.append(line);
			sb.append("\n");
		}

		return readableBlocks;
	}

	@Override
	protected boolean isBalanceValidationRequired(String readableSyntax) {
		readableSyntax = readableSyntax.trim();

		if ((readableSyntax.startsWith("@") && readableSyntax.contains("{")) ||
			readableSyntax.startsWith("setUp") ||
			readableSyntax.startsWith("tearDown") ||
			readableSyntax.startsWith("test")) {

			return true;
		}

		return false;
	}

}
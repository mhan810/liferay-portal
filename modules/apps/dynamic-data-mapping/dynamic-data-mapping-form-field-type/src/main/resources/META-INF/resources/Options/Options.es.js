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

import ClayIcon from '@clayui/icon';
import classNames from 'classnames';
import {RulesSupport} from 'dynamic-data-mapping-form-builder';
import {usePage} from 'dynamic-data-mapping-form-renderer';
import {openModal} from 'frontend-js-web';
import React, {useEffect, useRef, useState} from 'react';
import {DndProvider} from 'react-dnd';
import {HTML5Backend} from 'react-dnd-html5-backend';

import {FieldBase} from '../FieldBase/ReactFieldBase.es';
import KeyValue from '../KeyValue/KeyValue.es';
import DnD from './DnD.es';
import DragPreview from './DragPreview.es';
import {
	compose,
	dedupValue,
	getDefaultOptionValue,
	isOptionValueGenerated,
	normalizeFields,
	random,
} from './util.es';

const Option = React.forwardRef(
	({children, className, disabled, onClick, showCloseButton, style}, ref) => (
		<div
			className={classNames('ddm-field-options', className)}
			style={style}
		>
			<span
				className={classNames('ddm-options-drag', {
					disabled,
				})}
				ref={disabled ? null : ref}
			>
				<ClayIcon symbol="drag" />
			</span>

			<div className="ddm-option-entry">
				{children}

				{showCloseButton && (
					<button
						className="close close-modal"
						onClick={onClick}
						type="button"
					>
						<ClayIcon symbol="times" />
					</button>
				)}
			</div>
		</div>
	)
);

const getInitialOption = (generateOptionValueUsingOptionLabel) => {
	return generateOptionValueUsingOptionLabel
		? {
				id: random(),
				label: '',
				value: '',
		  }
		: {
				id: random(),
				label: '',
				value: getDefaultOptionValue(
					generateOptionValueUsingOptionLabel,
					''
				),
		  };
};

const refreshFields = (
	defaultLanguageId,
	editingLanguageId,
	generateOptionValueUsingOptionLabel,
	initialOption,
	options
) => {
	const refreshedFields = [
		...options.map((option) => ({
			generateKeyword: generateOptionValueUsingOptionLabel
				? isOptionValueGenerated(
						defaultLanguageId,
						editingLanguageId,
						options,
						option
				  )
				: false,
			...option,
			value: option.value
				? option.value
				: getDefaultOptionValue(
						generateOptionValueUsingOptionLabel,
						option.label
				  ),
		})),
		{
			generateKeyword: generateOptionValueUsingOptionLabel,
			...initialOption,
		},
	].filter((field) => field && Object.keys(field).length > 0);

	return normalizeFields(
		refreshedFields,
		generateOptionValueUsingOptionLabel
	);
};

const Options = ({
	children,
	defaultLanguageId,
	disabled,
	editingLanguageId,
	generateOptionValueUsingOptionLabel,
	onChange,
	value = {},
}) => {
	const {builderRules} = usePage();

	const initialOptionRef = useRef(
		getInitialOption(generateOptionValueUsingOptionLabel)
	);

	const [normalizedValue, setNormalizedValue] = useState(() => {
		const formattedValue = {...value};

		Object.keys(value).forEach((languageId) => {
			if (defaultLanguageId !== languageId) {
				formattedValue[languageId] = formattedValue[languageId].filter(
					({value}) => !!value
				);
			}

			formattedValue[languageId] = formattedValue[languageId].map(
				(option) => {
					return {
						id: random(),
						...option,
						value:
							!option.value &&
							option.label.toLowerCase() ===
								Liferay.Language.get('option').toLowerCase()
								? getDefaultOptionValue(
										generateOptionValueUsingOptionLabel,
										option.label
								  )
								: option.value,
					};
				}
			);
		});

		return formattedValue;
	});

	const [fields, setFields] = useState(() => {
		const options =
			normalizedValue[editingLanguageId] ||
			normalizedValue[defaultLanguageId] ||
			[];

		return refreshFields(
			defaultLanguageId,
			editingLanguageId,
			generateOptionValueUsingOptionLabel,
			initialOptionRef.current,
			options
		);
	});

	useEffect(() => {
		const options =
			normalizedValue[editingLanguageId] ||
			normalizedValue[defaultLanguageId] ||
			[];

		setFields(
			refreshFields(
				defaultLanguageId,
				editingLanguageId,
				generateOptionValueUsingOptionLabel,
				initialOptionRef.current,
				options
			)
		);
	}, [
		defaultLanguageId,
		editingLanguageId,
		generateOptionValueUsingOptionLabel,
		normalizedValue,
	]);

	const defaultOptionRef = useRef(
		fields.length === 2 &&
			fields[0].label.toLowerCase() ===
				Liferay.Language.get('option').toLowerCase()
	);

	const getSynchronizedValue = (fields) => {
		const _fields = [...fields];

		_fields.pop();

		const availableLanguageIds = Object.getOwnPropertyNames(
			normalizedValue
		);

		return availableLanguageIds.reduce(
			(value, languageId) => ({
				...value,
				[languageId]: synchronizeValue(_fields, languageId),
			}),
			{[editingLanguageId]: [..._fields]}
		);
	};

	const synchronizeValue = (fields, languageId) => {
		if (editingLanguageId === languageId) {
			return [...fields];
		}

		return [...fields].map((field) => {
			const existingValue = normalizedValue[languageId].find(
				({value}) => value === field.value
			);

			if (existingValue) {
				const {copyFrom} = existingValue;

				if (copyFrom && copyFrom === editingLanguageId) {
					return {
						...existingValue,
						label: field.label,
					};
				}

				return existingValue;
			}

			return {
				...field,
				copyFrom: editingLanguageId,
				label: field.label,
			};
		});
	};

	const clone = (...args) => {
		return [[...fields], ...args];
	};

	const dedup = (fields, index, property, value) => {
		const {generateKeyword, id} = fields[index];

		if (property === 'value' && generateKeyword) {
			value = dedupValue(
				fields,
				value ? value : Liferay.Language.get('option'),
				id,
				generateOptionValueUsingOptionLabel
			);
		}

		return [fields, index, property, value];
	};

	const set = (fields) => {
		setFields(fields);

		const synchronizedNormalizedValue = getSynchronizedValue(fields);

		setNormalizedValue(synchronizedNormalizedValue);
		onChange(synchronizedNormalizedValue);
	};

	const add = (fields, index, property, value) => {
		fields[index][property] = value;

		const initialOption = getInitialOption(
			generateOptionValueUsingOptionLabel
		);

		fields.push({
			generateKeyword: generateOptionValueUsingOptionLabel,
			...initialOption,
		});

		initialOptionRef.current = initialOption;

		return [fields, index, property, value];
	};

	const change = (fields, index, property, value) => {
		const {edited, label} = fields[index];

		fields[index][property] = value;
		fields[index]['edited'] =
			edited || (value && value !== label && property === 'value');

		if (property === 'label') {
			fields[index]['copyFrom'] = undefined;
		}

		return [fields, index, property, value];
	};

	const normalize = (fields) => {
		return [normalizeFields(fields, generateOptionValueUsingOptionLabel)];
	};

	const handleConfirmDelete = (index, option) => {
		if (RulesSupport.findRuleByFieldName(option, builderRules)) {
			openModal({
				bodyHTML: Liferay.Language.get(
					'a-rule-is-applied-to-this-field'
				),
				buttons: [
					{
						displayType: 'secondary',
						label: Liferay.Language.get('cancel'),
						type: 'cancel',
					},
					{
						displayType: 'danger',
						label: Liferay.Language.get('confirm'),
						onClick: () => {
							composedDelete(index);
						},
						type: 'cancel',
					},
				],
				size: 'md',
				title: Liferay.Language.get('delete-field-with-rule-applied'),
			});
		}
		else {
			composedDelete(index);
		}
	};

	const handleDelete = (fields, index) => {
		fields.splice(index, 1);

		return [fields];
	};

	const move = (fields, data) => {
		const {itemPosition, targetPosition} = data;

		if (itemPosition === fields.length - 1) {
			return [fields];
		}

		const item = {...fields[itemPosition]};
		const newTargetPosition =
			targetPosition > itemPosition ? targetPosition - 1 : targetPosition;

		fields.splice(itemPosition, 1);
		fields.splice(newTargetPosition, 0, item);

		return [fields];
	};

	const composedAdd = compose(clone, dedup, add, set);
	const composedBlur = compose(clone, normalize, set);
	const composedChange = compose(clone, dedup, change, set);
	const composedDelete = compose(clone, handleDelete, set);
	const composedMove = compose(clone, move, set);

	return (
		<div className="ddm-field-options-container">
			<DragPreview component={Option}>{children}</DragPreview>
			{fields.map((option, index) => (
				<DnD
					index={index}
					key={option.id}
					onDragEnd={composedMove}
					option={option}
				>
					<Option
						disabled={disabled}
						onClick={() => handleConfirmDelete(index, option.value)}
						showCloseButton={
							!(fields.length - 1 === index) && !disabled
						}
					>
						{children({
							defaultOptionRef,
							handleBlur: composedBlur,
							handleField: !(fields.length - 1 === index)
								? composedChange.bind(this, index)
								: composedAdd.bind(this, index),
							index,
							option,
						})}
					</Option>
				</DnD>
			))}
		</div>
	);
};

const Main = ({
	defaultLanguageId = themeDisplay.getLanguageId(),
	editingLanguageId = themeDisplay.getLanguageId(),
	generateOptionValueUsingOptionLabel = false,
	onChange,
	keywordReadOnly,
	placeholder = Liferay.Language.get('enter-an-option'),
	readOnly,
	required,
	value = {},
	visible,
	...otherProps
}) => (
	<DndProvider backend={HTML5Backend} context={window}>
		<FieldBase {...otherProps} readOnly={readOnly} visible={visible}>
			<Options
				defaultLanguageId={defaultLanguageId}
				disabled={readOnly}
				editingLanguageId={editingLanguageId}
				generateOptionValueUsingOptionLabel={
					generateOptionValueUsingOptionLabel
				}
				onChange={(value) => onChange({}, value)}
				value={value}
			>
				{({defaultOptionRef, handleBlur, handleField, index, option}) =>
					option && (
						<KeyValue
							generateKeyword={option.generateKeyword}
							keyword={option.value}
							keywordReadOnly={keywordReadOnly}
							name={`option${index}`}
							onBlur={handleBlur}
							onChange={(event) =>
								handleField('label', event.target.value)
							}
							onFocus={() => {
								if (defaultOptionRef.current) {
									handleField('label', '');
									defaultOptionRef.current = false;
								}
							}}
							onKeywordBlur={handleBlur}
							onKeywordChange={(event, value, generate) => {
								handleField('generateKeyword', generate);
								handleField('value', value);
							}}
							placeholder={placeholder}
							readOnly={option.disabled}
							required={required}
							showLabel={false}
							value={option.label}
							visible={visible}
						/>
					)
				}
			</Options>
		</FieldBase>
	</DndProvider>
);

Main.displayName = 'Options';

export default Main;

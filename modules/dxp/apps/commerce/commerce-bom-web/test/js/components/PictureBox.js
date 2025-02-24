/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import {mount, shallow} from 'enzyme';
import React from 'react';

import appActions from '../../../src/main/resources/META-INF/resources/js/actions/app.es';
import areaActions from '../../../src/main/resources/META-INF/resources/js/actions/area.es';
import PictureBox, {
	PartDetail,
} from '../../../src/main/resources/META-INF/resources/js/components/areas/PictureBox.es';

const mockedContext = {
	actions: {...areaActions, ...appActions},
	state: {
		app: {
			spritemap: '/spritemap.test.svg',
		},
		area: {
			highlightedDetail: {
				number: 1,
			},
			imageUrl: '/testImg.jpg',
			name: 'test name',
			products: [
				{
					id: 'PR01',
					name: 'Product 1',
					price: '$ 12.99',
					sku: 'sku01',
					thumbnailUrl: '/test-product',
					url: '/test-product',
				},
			],
			spotFormData: null,
			spots: [
				{
					id: 'SP01',
					number: 1,
					position: {
						x: 75,
						y: 75,
					},
					productId: 'PR01',
				},
				{
					id: 'SP02',
					number: 1,
					position: {
						x: 25,
						y: 25,
					},
					productId: 'PR01',
				},
			],
		},
	},
};

describe('PictureBox', () => {
	jest.spyOn(React, 'useContext').mockImplementation(() => mockedContext);

	it('renders without crashing', () => {
		mount(<PictureBox />);
	});

	it('display the picture', () => {
		const pictureBox = shallow(<PictureBox />);
		expect(pictureBox.find('.picture-box__image').prop('src')).toEqual(
			mockedContext.state.area.imageUrl
		);
	});

	describe('Spots', () => {
		const pictureBox = mount(<PictureBox />);
		const partDetails = pictureBox.find(PartDetail);

		it('display the parts details', () => {
			expect(partDetails.length).toEqual(2);
		});

		it('receive the correct props', () => {
			const firstPartDetailProps = partDetails.first().props();

			expect(firstPartDetailProps.id).toEqual('SP01');
			expect(firstPartDetailProps.number).toEqual(1);
			expect(firstPartDetailProps.position).toEqual({x: 75, y: 75});
			expect(firstPartDetailProps.productId).toEqual('PR01');
		});
	});
});

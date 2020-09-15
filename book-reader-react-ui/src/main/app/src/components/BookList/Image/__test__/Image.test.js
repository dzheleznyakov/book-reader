import React from 'react';
import { mount } from 'enzyme';

import Image from '../Image';
import { expect } from 'chai';

describe("<Image />", () => {
    const props = { image: [0, 1, 2, 3] };
    let wrapper;
    let realCreateObjectURL;
    let mockCreateObjectURL

    beforeAll(() => {
        realCreateObjectURL = global.URL.createObjectURL;
    });

    beforeEach(() => {
        mockCreateObjectURL = sinon.mock();
        global.URL.createObjectURL = mockCreateObjectURL;        
    });

    afterAll(() => {
        global.URL.createObjectURL = realCreateObjectURL;
    });

    const getImageSrc = () => wrapper.find('img').prop('src');

    test("render without errors", () => {
        const mockUrl = 'https://mock-url.net';
        mockCreateObjectURL
            .returns(mockUrl);
        
        wrapper = mount(<Image {...props} />);

        expect(getImageSrc()).to.be.equal(mockUrl);
    });
});
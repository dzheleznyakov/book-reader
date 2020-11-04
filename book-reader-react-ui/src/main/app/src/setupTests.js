// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom

import { expect } from 'chai';
import '@testing-library/jest-dom/extend-expect';
import sinon from 'sinon';

import Adapter from 'enzyme-adapter-react-16';
import Enzyme from 'enzyme';

import { Builder } from './__test__/renderComponent';

Enzyme.configure({ adapter: new Adapter() })

global.expect = expect;
global.sinon = sinon;
global.renderComponentBuilder = (component, props = {}) => new Builder(component, props);

global.mockPackage = (path, override = {}) => jest.mock(path, () => ({
    ...jest.requireActual(path),
    ...override,
}));

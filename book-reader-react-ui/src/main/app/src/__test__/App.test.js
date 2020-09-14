import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { shallow, mount } from 'enzyme';

import App from '../App';

describe('<App />', () => {
  let container;

  beforeEach(() => {
    container = mount(<BrowserRouter><App /></BrowserRouter>);
  });

  test('renders successfully', () => {
  });
});

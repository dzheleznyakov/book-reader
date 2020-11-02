import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { mount } from 'enzyme';

import store from '../store';
import App from '../App';

describe('<App />', () => {
  let container;

  beforeEach(() => {
    container = mount(
      <Provider store={store}>
        <BrowserRouter>
          <App />
        </BrowserRouter>
      </Provider>
    );
  });

  test('renders successfully', () => {
  });
});

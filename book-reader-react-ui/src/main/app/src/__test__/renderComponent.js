import React from 'react';
import { Router } from 'react-router-dom';
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import { shallow, mount } from 'enzyme';
import { createMemoryHistory } from 'history';

import reducer from '../store/reducers';
import { act } from 'react-dom/test-utils';

export class Builder {
    constructor(component, props) {
        this.component = component;
        this.props = props;
        this.isShallow = false;
        this.history = null;
        this.state = null;
    }

    isShallow(value) {
        this.isShallow = value;
        return this;
    };

    withHistory(history) {
        this.history = history || createMemoryHistory();
        return this;
    };

    withStore(state) {
        this.state = state;
        return this;
    };

    get() {
        const renderer = this.isShallow ? shallow : mount;
        const P = this.state ? Provider : React.Fragment;
        const R = this.history ? Router : React.Fragment;
        const C = this.component;

        const reduxStore = this.state ? createStore(reducer, this.state) : null;
        let wrapper;

        const pProps = this.state ? { store: reduxStore } : {};
        const rProps = this.history ? { history: this.history } : {};

        const renderComponent = async () => {
            await act(
                async () => {
                    wrapper = renderer(
                        <P {...pProps}>
                            <R {...rProps}>
                                <C {...this.props} />
                            </R>
                        </P>
                    );
                }
            );
        };
        const getWrapper = () => wrapper;
        const getStore = () => reduxStore;

        return { renderComponent, getWrapper, getStore };
    };
}

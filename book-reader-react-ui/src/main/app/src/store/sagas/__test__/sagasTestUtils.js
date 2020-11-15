import { put, call } from 'redux-saga/effects';

import axios from '../../../axios-api';

const effectTypes = {
    PUT: 'PUT',
    CALL: 'CALL',
};

const sagaRuntime = Symbol('sagaRuntime');
const getNext = Symbol('next');

const testPut = Symbol('testPut');
const testCall = Symbol('testCall');

class SagaTestRunner {
    constructor(saga, action) {
        this.saga = saga;
        this.action = action;
    }

    effects = [];

    dispatchAction(action) {
        this.effects.push({
            type: effectTypes.PUT,
            action,
        });
        return this;
    }

    callFunction(func, args = [], res, error) {
        this.effects.push({
            type: effectTypes.CALL,
            func,
            args,
            res,
            error,
        });
        return this;
    }

    callApi(method, args, res, error) {
        return this.callFunction(axios[method], args, res, error);
    }

    test() {
        const { saga, action, effects } = this;
        this[sagaRuntime] = saga(action);
        let value;
        for (const effect of effects)
            switch (effect.type) {
                case effectTypes.CALL: value = this[testCall](effect, value); break;
                case effectTypes.PUT: value = this[testPut](effect, value); break;
                default: throw new TypeError(`Effect type [${effect.type}] is not supported`);
            }
        const next = this[getNext](value);
        expect(next).to.be.deep.equal({ value: undefined, done: true });
    }

    [testCall](effect, value) {
        const { func, args, res, error } = effect;
        const next = this[getNext](value);
        expect(next.value).to.be.deep.equal(call(func, ...args));
        return res || error;
    }

    [testPut](effect, value) {
        const { action } = effect;
        const next = this[getNext](value);
        expect(next.value).to.be.deep.equal(put(action));
    }

    [getNext](value) {
        return value instanceof Error
            ? this[sagaRuntime].throw(value)
            : this[sagaRuntime].next(value);
    }
}

export const createSagaTestRunner = (saga, action) => new SagaTestRunner(saga, action);

import { createStore, applyMiddleware, compose, } from 'redux';
import createSagaMiddleware from 'redux-saga';

import * as sagas from './sagas/';
import rootReducer from './reducers';

const composeEnhancers = 
  (process.env.NODE_ENV === 'development' ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ : null) 
  || compose;

const sagaMiddleware = createSagaMiddleware();

const store = createStore(rootReducer, composeEnhancers(applyMiddleware(sagaMiddleware)));

Object.keys(sagas)
  .forEach(watcher => sagaMiddleware.run(sagas[watcher]));

export default store;

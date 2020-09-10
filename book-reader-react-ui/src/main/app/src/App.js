import React from 'react';
import { Switch, Redirect, Route, withRouter } from 'react-router-dom';

import Layout from './hoc/Layout/Layout';
import asyncComponent from './hoc/asyncComponent/asyncComponent';

const asyncBookList = asyncComponent(() => {
  return import('./components/BookList/BookList');
});

const asyncBookMain = asyncComponent(() => {
  return import('./components/BookMain/BookMain');
})

const App = () => {
  const routes = (
    <Switch>
      <Route exact path="/books" component={asyncBookList} />
      <Route exact path="/books/:id" component={asyncBookMain} />
      <Redirect to="/books" />
    </Switch>
  );
  
  return (
    <Layout>
      {routes}
    </Layout>
  );
};

export default withRouter(App);

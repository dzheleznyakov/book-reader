import React from 'react';

import Layout from './hoc/Layout/Layout';

import classes from './App.module.scss';

const App = () => {
  return (
    <Layout>
      <div className={classes.Content}>Content</div>
    </Layout>
  );
};

export default App;

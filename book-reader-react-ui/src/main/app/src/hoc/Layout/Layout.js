import React from 'react';
import PropTypes from 'prop-types';

import classes from './Layout.module.scss';

const Layout = props => {
  const { children } = props;

  return (
    <div className={classes.Layout}>
      <div className={classes.Toolbar}>This is Toolbar...</div>
      {children}
    </div>
  );
};

Layout.propTypes = {
  children: PropTypes.node,
};

export default Layout;
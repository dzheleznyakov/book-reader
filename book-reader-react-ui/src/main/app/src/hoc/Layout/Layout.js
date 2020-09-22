import React from 'react';
import PropTypes from 'prop-types';

import NavigationBar from '../../components/UI/NavigationBar/NavigationBar';

import classes from './Layout.module.scss';

const Layout = props => {
  const { children } = props;

  return (
    <div className={classes.Layout}>
      <div className={classes.Toolbar}><NavigationBar /></div>
      <div className={classes.Content}>
        {children}
      </div>
      <div className={classes.Footer}></div>
    </div>
  );
};

Layout.propTypes = {
  children: PropTypes.node,
};

export default Layout;
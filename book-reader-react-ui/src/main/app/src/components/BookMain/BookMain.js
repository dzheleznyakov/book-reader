import React from 'react';
import { useLocation } from 'react-router-dom';

import classes from './BookMain.module.scss';

const BookMain = props => {
    const location = useLocation();
    console.log(location);
    return (
        <div style={{ marginTop: "100px"}}>
            <div>{location.pathname}</div>
            <div></div>
        </div>
    );
};

export default BookMain;

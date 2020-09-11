import React from 'react';
import { useLocation, useParams } from 'react-router-dom';

import classes from './BookMain.module.scss';

const BookMain = props => {
    const location = useLocation();
    const params = useParams();
    console.log(params);
    
    return (
        <div style={{ marginTop: "100px"}}>
            <div>{location.pathname}</div>
            <div></div>
        </div>
    );
};

export default BookMain;

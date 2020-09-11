import React from 'react';
import PropTypes from 'prop-types';

import classes from './Image.module.scss';

const Image = props => {
    const { image } = props;

    const bytes = new Uint8Array(image);
    const blob = new Blob([bytes], { type: "image/jpeg" });
    const urlCreator = window.URL || window.webkitURL;
    const imageUrl = urlCreator.createObjectURL(blob);
    return (
        <div className={classes.ImageWrapper}>
            <img className={classes.Image} src={imageUrl} alt="Book Cover" />
        </div>
    );
};

Image.propTypes = {
    image: PropTypes.arrayOf(PropTypes.number),
};

Image.defaultProps = {
    image: [],
};

export default Image;

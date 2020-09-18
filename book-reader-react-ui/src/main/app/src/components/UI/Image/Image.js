import React from 'react';
import PropTypes from 'prop-types';

import classes from './Image.module.scss';

const Image = props => {
    const { image, imageWrapperClass, imageClass } = props;

    const bytes = new Uint8Array(image);
    const blob = new Blob([bytes], { type: "image/jpeg" });
    const urlCreator = window.URL || window.webkitURL;
    const imageUrl = urlCreator.createObjectURL(blob);

    const wrapperCssClasses = [classes.ImageWrapper];
    if (imageWrapperClass)
        wrapperCssClasses.push(imageWrapperClass);

    const imageCssClasses = [classes.Image];
    if (imageClass)
        imageCssClasses.push(imageClass);

    return (
        <div className={wrapperCssClasses.join(' ')}>
            <img className={imageCssClasses.join(' ')} src={imageUrl} alt="Book Cover" />
        </div>
    );
};

Image.propTypes = {
    image: PropTypes.arrayOf(PropTypes.number),
    imageWrapperClass: PropTypes.string,
    imageClass: PropTypes.string,
};

Image.defaultProps = {
    image: [],
};

export default Image;

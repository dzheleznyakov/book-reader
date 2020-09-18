import PropTypes from 'prop-types';

const docShape = {
    id: PropTypes.string,
    documentType: PropTypes.string,
    formatting: PropTypes.arrayOf(PropTypes.string),
};

const contentTypes = [
    PropTypes.arrayOf(PropTypes.shape(docShape)),
    PropTypes.string,
];

docShape.content = PropTypes.oneOfType(contentTypes);

export default docShape;

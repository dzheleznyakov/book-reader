import PropTypes from 'prop-types';

const docShape = {
    id: PropTypes.string,
    documentType: PropTypes.string.isRequired,
    formatting: PropTypes.arrayOf(PropTypes.string),
};

const contentTypes = [
    PropTypes.arrayOf(PropTypes.shape(docShape)),
    PropTypes.arrayOf(PropTypes.number),
    PropTypes.string,
];

docShape.content = PropTypes.oneOfType(contentTypes);

export default docShape;

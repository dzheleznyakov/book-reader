import React from 'react';
import { Link } from 'react-router-dom';

import types from './types';
import styles from './styles';

if (Array.prototype.hasOwnProperty('has'))
    console.warn('[Array.prototype.has] already exists');
// eslint-disable-next-line no-extend-native
Array.prototype.has = function (item) {
    return this.indexOf(item) >= 0;
};

if (Array.prototype.hasOwnProperty('hasOnly'))
    console.warn('[Array.prototype.hasOnly] already exists');
// eslint-disable-next-line no-extend-native
Array.prototype.hasOnly = function (item) {
    return this.length === 1 && this[0] === item;
};

const getAnchorView = () => props => {
    const { href } = props;
    return !href || href.indexOf("http") === 0 || href.indexOf("mailto:") === 0
        ? <a {...props} /> // eslint-disable-line jsx-a11y/anchor-has-content
        : <Link to={href} {...props} />;
};

const getView = Tag => (
    Tag === 'a' ? getAnchorView() : props => <Tag {...props} />
);

const mapByDefault = (type, formatting, tag = 'div') => {
    const ft = formatting.length ? `.${formatting.join('.')}` : '';
    console.debug('View [%s] is not recognised', `${type}${ft}`);
    return getView(tag);
};

const mapSection = formatting => getView('section');

const mapBlock = formatting => {
    if (!formatting.length)
        return getView('div');
    if (formatting.hasOnly(styles.FIGURE))
        return getView('div');
    if (formatting.hasOnly(styles.FOOTNOTE))
        return getView('div');

    if (formatting.hasOnly(styles.TIP))
        return getView('div');
    if (formatting.hasOnly(styles.NOTE))
        return getView('div');
    if (formatting.hasOnly(styles.CAUTION))
        return getView('div');
    if (formatting.hasOnly(styles.WARNING))
        return getView('div');

    if (formatting.hasOnly(styles.UNORDERED_LIST))
        return getView('ul');
    if (formatting.hasOnly(styles.ORDERED_LIST))
        return getView('ol');
    if (formatting.hasOnly(styles.LIST_ITEM))
        return getView('li');

    if (formatting.has(styles.UNORDERED_LIST) && formatting.has(styles.DEFINITION))
        return getView('dl');
    if (formatting.hasOnly(styles.DEFINITION_TERM))
        return getView('dt');
    if (formatting.hasOnly(styles.DEFINITION_DESCR))
        return getView('dd');

    if (formatting.has(styles.FRAMED) && formatting.has(styles.NOTE))
        return getView('div');
    if (formatting.has(styles.UNORDERED_LIST) && formatting.has(styles.SIMPLE))
        return getView('ul');
    if (formatting.has(styles.CODE) && formatting.has(styles.LISTING))
        return getView('pre');
    if (formatting.hasOnly(styles.EPIGRAPH))
        return getView('blockquote');
    if (formatting.hasOnly(styles.SIDEBAR))
        return getView('aside');
    
    return mapByDefault(types.BLOCK, formatting);
};

const mapInlined = formatting => {
    if (!formatting.length)
        return getView('span');
    if (formatting.hasOnly(styles.INDEX_TERM))
        return getView('span');
    if (formatting.hasOnly(styles.BOLD))
        return getView('strong');
    if (formatting.hasOnly(styles.EMPH))
        return getView('em');
    if (formatting.hasOnly(styles.TITLE))
        return getView('h1');
    if (formatting.has(styles.CODE))
        return getView('code');
    if (formatting.hasOnly(styles.CAPTION))
        return getView('caption');
    if (formatting.hasOnly(styles.SUP))
        return getView('sup');
    return mapByDefault(types.INLINED, formatting, 'span');
};

const mapParagraph = formatting => {
    if (!formatting.length)
        return getView('p');
    return mapByDefault(types.PARAGRAPH, formatting, 'p');
};

const mapHref = formatting => {
    if (!formatting.length)
        return getView('a');
    if (formatting.hasOnly(styles.INDEX_TERM))
        return getView('a');
    if (formatting.has(styles.FOOTNOTE_REF) >= 0 || formatting.has(styles.FOOTNOTE) >= 0)
        return getView('a');
    return mapByDefault(types.HREF, formatting, 'a');
};

const mapEmail = formatting => {
    if (!formatting.length)
        return getView('a');
    return mapByDefault(types.EMAIL, formatting, 'a');
};

const mapImage = formatting => props => {
    const { width, height, id, key, className, image } = props;

    const bytes = new Uint8Array(image);
    const blob = new Blob([bytes], { type: "image/jpeg" });
    const urlCreator = window.URL || window.webkitURL;
    const imageUrl = urlCreator.createObjectURL(blob);

    return (
        <img 
            id={id} 
            key={key} 
            className={className} 
            src={imageUrl} 
            alt="Illustration"
            width={width} 
            height={height}
        />
    );
};

const mapTable = formatting => {
    if (!formatting.length)
        return getView('table');
    if (formatting.hasOnly(styles.TABLE_BODY))
        return getView('tbody');
    if (formatting.hasOnly(styles.TABLE_HEADER))
        return getView('thead');
    if (formatting.has(styles.TABLE_ROW))
        return getView('tr');
    if (formatting.hasOnly(styles.TABLE_CELL))
        return getView('td');
    if (formatting.hasOnly(styles.TABLE_HEADER_CELL))
        return getView('th');
    return mapByDefault(types.TABLE, formatting, 'span');
};

const mapper = (type, formatting) => {
    switch(type) {
        case types.TEXT: return props => props.children;
        case types.SECTION: return mapSection(formatting);
        case types.BLOCK: return mapBlock(formatting);
        case types.PARAGRAPH: return mapParagraph(formatting);
        case types.INLINED: return mapInlined(formatting);
        case types.HREF: return mapHref(formatting);
        case types.EMAIL: return mapEmail(formatting);
        case types.IMAGE: return mapImage(formatting);
        case types.TABLE: return mapTable(formatting);
        default: return mapByDefault(type, formatting);
    }    
};

export default mapper;

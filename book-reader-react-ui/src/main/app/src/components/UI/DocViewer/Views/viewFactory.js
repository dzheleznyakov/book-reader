import React from 'react';

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

const getView = Tag => 
    props => <Tag {...props} />;

const mapByDefault = (type, formatting, tag = 'div') => {
    const ft = formatting.length ? `.${formatting.join('.')}` : '';
    console.debug('View [%s] is not recognised', `${type}${ft}`);
    return getView(tag);
};

const mapSection = formatting => {
    if (!formatting.length)
        return getView('section');
    return mapByDefault(types.SECTION, formatting, 'section');
};

const mapBlock = formatting => {
    if (!formatting.length)
        return getView('div');
    if (formatting.hasOnly(styles.UNORDERED_LIST))
        return getView('ul');
    if (formatting.hasOnly(styles.LIST_ITEM))
        return getView('li');
    if (formatting.has(styles.UNORDERED_LIST) && formatting.has(styles.DEFINITION))
        return getView('dl');
    if (formatting.hasOnly(styles.DEFINITION_TERM))
        return getView('dt');
    if (formatting.hasOnly(styles.DEFINITION_DESCR))
        return getView('dd');
    if (formatting.hasOnly(styles.TIP))
        return getView('div');
    if (formatting.hasOnly(styles.NOTE))
        return getView('div');
    if (formatting.hasOnly(styles.CAUTION))
        return getView('div');
    if (formatting.has(styles.FRAMED) && formatting.has(styles.NOTE))
        return getView('dl');
    if (formatting.has(styles.UNORDERED_LIST) && formatting.has(styles.SIMPLE))
        return getView('ul');
    return mapByDefault(types.BLOCK, formatting);
};

const mapInlined = formatting => {
    if (!formatting.length)
        return getView('span');
    if (formatting.hasOnly(styles.BOLD))
        return getView('strong');
    if (formatting.hasOnly(styles.EMPH))
        return getView('em');
    if (formatting.hasOnly(styles.TITLE))
        return getView('h1');
    if (formatting.hasOnly(styles.CODE))
        return getView('code');
    if (formatting)
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
    return mapByDefault(types.HREF, formatting, 'a');
};

const mapEmail = formatting => {
    if (!formatting.length)
        return getView('a');
    return mapByDefault(types.EMAIL, formatting, 'a');
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
        default: return mapByDefault(type, formatting);
    }    
};

export default mapper;

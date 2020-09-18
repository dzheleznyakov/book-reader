import React from 'react';

import types from './types';
import styles from './styles';

if (Array.prototype.hasOwnProperty('has'))
    console.warn('[Array.prototype.has] already exists');
// eslint-disable-next-line no-extend-native
Array.prototype.has = function (item) {
    return this.indexOf(item) >= 0;
};

const getView = Tag => 
    props => <Tag key={props.key} className={props.className}>{props.children}</Tag>;

const mapByDefault = (type, formatting) => {
    console.debug('View [%s] is not recognised', `${type}.${formatting.join('.')}`);
    return getView('div');
};

const mapBlock = formatting => {
    if (!formatting.length)
        return getView('div');
    if (formatting.has(styles.UNORDERED_LIST))
        return getView('ul');
    if (formatting.has(styles.LIST_ITEM))
        return getView('li');
    return mapByDefault(types.BLOCK, formatting);
};

const mapInlined = formatting => {
    if (!formatting.length)
        return getView('span');
    if (formatting.has(styles.BOLD))
        return getView('strong');
    return mapByDefault(types.INLINED, formatting);
};

const mapParagraph = formatting => {
    if (!formatting.length)
        return getView('p');
    return mapByDefault(types.PARAGRAPH, formatting);
};

const mapper = (type, formatting) => {
    switch(type) {
        case types.TEXT: return props => props.children;
        case types.BLOCK: return mapBlock(formatting);
        case types.PARAGRAPH: return mapParagraph(formatting);
        case types.INLINED: return mapInlined(formatting);
        default: return mapByDefault(type, formatting);
    }    
};

export default mapper;

import styles from "./styles";
import types from "./types";

import classes from './Views.module.scss';

export const CssClasses = function () {
    this.classes = [];

    CssClasses.prototype.push = function (...items) {
        this.classes.push(...items);
    }

    CssClasses.prototype.toString = function () {
        return this.classes.length ? this.classes.join(' ') : undefined;
    }
};

const classFactory = (type, formatting) => {
    const cssClasses = new CssClasses();
    if (formatting.indexOf(styles.LEVEL_0) >= 0)
        cssClasses.push(classes['Level-0']);
    if (formatting.indexOf(styles.LEVEL_1) >= 0)
        cssClasses.push(classes['Level-1']);
    if (formatting.indexOf(styles.LEVEL_2) >= 0)
        cssClasses.push(classes['Level-2']);
    if (formatting.indexOf(styles.DEFINITION_TERM) >= 0)
        cssClasses.push(classes.Term);
    if (formatting.indexOf(styles.TIP) >= 0)
        cssClasses.push(classes.Tip);
    if (formatting.indexOf(styles.NOTE) >= 0)
        cssClasses.push(classes.Note);
    if (formatting.indexOf(styles.CAUTION) >= 0)
        cssClasses.push(classes.Caution);
    if (type === types.HREF)
        cssClasses.push(classes.Href);
    if (type === types.EMAIL)
        cssClasses.push(classes.Email);
    if (formatting.indexOf(styles.UNORDERED_LIST) >= 0 && formatting.indexOf(styles.SIMPLE) >= 0)
        cssClasses.push(classes['Simple-List']);

    if (!cssClasses.classes.length && formatting.length)
        console.debug('Found no classes for [%s]', formatting.join('.'));
    
    return cssClasses;
};

export default classFactory;

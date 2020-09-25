import styles from "./styles";
import types from "./types";

import classes from './Views.module.scss';

const toDocLiteral = (type, formatting) => [type].concat(formatting).join('.');

const classlessLiterals = new Set([
    `${types.BLOCK}.${styles.UNORDERED_LIST}`, 
    `${types.BLOCK}.${styles.ORDERED_LIST}`, 
    `${types.BLOCK}.${styles.LIST_ITEM}`,
    `${types.BLOCK}.${styles.UNORDERED_LIST}.${styles.DEFINITION}`,
    `${types.BLOCK}.${styles.DEFINITION_DESCR}`,
    `${types.INLINED}.${styles.TITLE}`,
    `${types.INLINED}.${styles.CODE}`,
    `${types.INLINED}.${styles.BOLD}`,
    `${types.INLINED}.${styles.EMPH}`,
    `${types.INLINED}.${styles.SUP}`,
]);

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
    if (formatting.indexOf(styles.LEVEL_IND) >= 0)
        cssClasses.push(classes['Level-Ind']);
    if (formatting.indexOf(styles.LEVEL_0) >= 0)
        cssClasses.push(classes['Level-0']);
    if (formatting.indexOf(styles.LEVEL_1) >= 0)
        cssClasses.push(classes['Level-1']);
    if (formatting.indexOf(styles.LEVEL_2) >= 0)
        cssClasses.push(classes['Level-2']);
    if (formatting.indexOf(styles.LEVEL_3) >= 0)
        cssClasses.push(classes['Level-3']);
    if (formatting.indexOf(styles.LEVEL_4) >= 0)
        cssClasses.push(classes['Level-4']);

    if (formatting.indexOf(styles.DEFINITION_TERM) >= 0)
        cssClasses.push(classes.Term);
    if (formatting.indexOf(styles.INDEX_TERM) >= 0)
        cssClasses.push(classes.IndexTerm);

    if (formatting.indexOf(styles.TIP) >= 0)
        cssClasses.push(classes.Tip);
    if (formatting.indexOf(styles.NOTE) >= 0)
        cssClasses.push(classes.Note);
    if (formatting.indexOf(styles.CAUTION) >= 0)
        cssClasses.push(classes.Caution);
    if (formatting.indexOf(styles.FIGURE) >= 0)
        cssClasses.push(classes.Figure);
    if (formatting.indexOf(styles.WARNING) >= 0)
        cssClasses.push(classes.Warning);

    if (type === types.HREF)
        cssClasses.push(classes.Href);
    if (type === types.EMAIL)
        cssClasses.push(classes.Email);
    if (formatting.indexOf(styles.FOOTNOTE_REF) >= 0)
        cssClasses.push(classes.FootnoteRef);
    if (formatting.indexOf(styles.FOOTNOTE) >= 0)
        cssClasses.push(classes.Footnote);

    if (formatting.indexOf(styles.UNORDERED_LIST) >= 0 && formatting.indexOf(styles.SIMPLE) >= 0)
        cssClasses.push(classes['Simple-List']);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.LISTING) >= 0)
        cssClasses.push(classes.Listing);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.CODE_OPERATOR) >= 0)
        cssClasses.push(classes.Op);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.CODE_STRING) >= 0)
        cssClasses.push(classes.Str);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.CODE_NUMBER) >= 0)
        cssClasses.push(classes.Num);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.CODE_NAME) >= 0)
        cssClasses.push(classes.Name);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.CODE_BOOLEAN) >= 0)
        cssClasses.push(classes.Bool);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.CODE_KEYWORD) >= 0)
        cssClasses.push(classes.Kw);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.CODE_COMMENT) >= 0)
        cssClasses.push(classes.Comm);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.CODE_CLASSNAME) >= 0)
        cssClasses.push(classes.ClName);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.CODE_REGEXP) >= 0)
        cssClasses.push(classes.Regexp);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.ERROR) >= 0)
        cssClasses.push(classes.Err);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.CODE_VARIABLE) >= 0)
        cssClasses.push(classes.Var);
    if (formatting.indexOf(styles.CODE) >= 0 && formatting.indexOf(styles.CODE_ESCAPED) >= 0)
        cssClasses.push(classes.Esc);
        
    if (type === types.TABLE && !formatting.length)
        cssClasses.push(classes.Table);
    if (formatting.indexOf(styles.TABLE_BODY) >= 0)
        cssClasses.push(classes.TableBody);
    if (formatting.indexOf(styles.TABLE_HEADER) >= 0)
        cssClasses.push(classes.TableHeader);
    if (formatting.indexOf(styles.TABLE_HEADER_CELL) >= 0)
        cssClasses.push(classes.TableHeaderCell);
    if (formatting.indexOf(styles.TABLE_CELL) >= 0)
        cssClasses.push(classes.TableCell);
    if (formatting.indexOf(styles.TABLE_ROW) >= 0)
        cssClasses.push(classes.TableRow);
    if (formatting.indexOf(styles.CAPTION) >= 0)
        cssClasses.push(classes.Caption);
        
    if (formatting.indexOf(styles.EPIGRAPH) >= 0)
        cssClasses.push(classes.Epigraph);
    if (formatting.indexOf(styles.SIDEBAR) >= 0)
        cssClasses.push(classes.Sidebar);

    // console.log(toDocLiteral(type, formatting), classlessLiterals.has(toDocLiteral(type, formatting)));
    if (!cssClasses.classes.length && formatting.length && !classlessLiterals.has(toDocLiteral(type, formatting)))
        console.debug('Found no classes for [%s]', toDocLiteral(type, formatting));
    
    return cssClasses;
};

export default classFactory;

import React from 'react';
import { shallow } from 'enzyme';

import viewFactory from '../viewFactory';
import types from '../types';
import styles from '../styles';

describe("docFactory()", () => {
    const assertTypeView = type => 
        (formatting, tag) => assertView(type, formatting, tag);

    const assertView = (type, formatting, tag) => {
        const View = viewFactory(type, formatting);
        const view = shallow(<View>content</View>);
        expect(view.html()).to.be.equal(`<${tag}>content</${tag}>`);
    };

    test("TEXT docs", () => {
        const content = 'some text'

        const View = viewFactory(types.TEXT, []);
        const view = View({ children: content });

        expect(view).to.be.equal(content);
    });

    test("SECTION docs", () => {
        const assertBlockView = assertTypeView(types.SECTION);
        assertBlockView([], 'section');
        assertBlockView(['FAKE_STYLE'], 'section');
    });

    test("BLOCK docs", () => {
        const assertBlockView = assertTypeView(types.BLOCK);
        assertBlockView([], 'div');
        assertBlockView(['FAKE_STYLE'], 'div');
        assertBlockView([styles.UNORDERED_LIST], 'ul');
        assertBlockView([styles.LIST_ITEM], 'li');
        assertBlockView([styles.UNORDERED_LIST, styles.DEFINITION], 'dl');
        assertBlockView([styles.DEFINITION_TERM], 'dt');
        assertBlockView([styles.DEFINITION_DESCR], 'dd');
        assertBlockView([styles.TIP], 'div');
        assertBlockView([styles.NOTE], 'div');
        assertBlockView([styles.CAUTION], 'div');
        assertBlockView([styles.CAUTION], 'div');
        assertBlockView([styles.FRAMED, styles.NOTE], 'div');
        assertBlockView([styles.UNORDERED_LIST, styles.SIMPLE], 'ul');
    });

    test("PARAGRAPH docs", () => {
        const assertParagraphView = assertTypeView(types.PARAGRAPH);
        assertParagraphView([], 'p');
        assertParagraphView(['FAKE_STYLE'], 'p');
    });

    test("INLINED docs", () => {
        const assertInlinedView = assertTypeView(types.INLINED);
        assertInlinedView([], 'span');
        assertInlinedView(['FAKE_STYLE'], 'span');
        assertInlinedView([styles.BOLD], 'strong');
        assertInlinedView([styles.EMPH], 'em');
        assertInlinedView([styles.TITLE], 'h1');
        assertInlinedView([styles.CODE], 'code');
    });

    test("HREF docs", () => {
        const assertHrefView = assertTypeView(types.HREF);
        assertHrefView([], 'a');
        assertHrefView(['FAKE_STYLE'], 'a');
        assertHrefView([styles.INDEX_TERM], 'a')
    });

    test("EMAIL docs", () => {
        const assertEmailView = assertTypeView(types.EMAIL);
        assertEmailView([], 'a');
    });

    test("unknown doc type", () => {
        assertView('FAKE_TYPE', [], 'div');
    });
});

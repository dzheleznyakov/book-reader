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

    test("BLOCK docs", () => {
        const assertBlockView = assertTypeView(types.BLOCK);
        assertBlockView([], 'div');
        assertBlockView(['FAKE_STYLE'], 'div');
        assertBlockView([styles.UNORDERED_LIST], 'ul');
        assertBlockView([styles.LIST_ITEM], 'li');
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
    });

    test("unknown doc type", () => {
        assertView('FAKE_TYPE', [], 'div');
    });
});

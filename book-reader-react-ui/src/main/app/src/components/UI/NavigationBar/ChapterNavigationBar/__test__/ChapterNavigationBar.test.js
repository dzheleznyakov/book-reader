import { NavLink } from 'react-router-dom';

import ChapterNavigationBar from '../ChapterNavigationBar';

describe("<ChapterNavigationBar />", () => {
    let wrapper;
    let navProps;
    let navBar;
    let links;

    beforeEach(() => {
        navProps = { nav:
            {
                prev: '/prev/path',
                next: '/next/path',
                book: '/book/bookId',
            },
        };
    });

    afterEach(() => {
        wrapper = null;
        navBar = null;
        links = null;
    });

    const renderComponent = async (props = navProps) => {
        const { renderComponent, getWrapper } = renderComponentBuilder(ChapterNavigationBar, props)
            .withHistory()
            .get();
        await renderComponent();
        wrapper = getWrapper();
        navBar = wrapper.find(ChapterNavigationBar);
        links = navBar.find(NavLink);
    };

    const assertPrevLink = link => {
        expect(link.text()).to.be.equal('Prev');
        expect(link.prop('to')).to.be.equal(navProps.nav.prev);
    };

    const assertNextLink = link => {
        expect(link.text()).to.be.equal('Next');
        expect(link.prop('to')).to.be.equal(navProps.nav.next);
    };

    const assertBookMainLink = link => {
        expect(link.text()).to.be.equal('');
        expect(link.prop('to')).to.be.equal(navProps.nav.book);
    };

    const assertMainPageLink = link => {
        expect(link.text()).to.be.equal('');
        expect(link.prop('to')).to.be.equal('/');
    };

    test("should render 'prev' and 'next' links", async () => {
        await renderComponent();

        expect(navBar.text()).to.equal('PrevNext');
        expect(links).to.have.length(4);
        assertPrevLink(links.at(0));
        assertMainPageLink(links.at(1));
        assertBookMainLink(links.at(2));
        assertNextLink(links.at(3));
    });

    test("should not render 'prev' when it is missing in props", async () => {
        navProps.nav.prev = null;
        await renderComponent();

        expect(navBar.text()).to.be.equal('Next');
        expect(links).to.have.length(3);
        assertMainPageLink(links.at(0));
        assertBookMainLink(links.at(1));
        assertNextLink(links.at(2));
    });

    test("should not render 'next' link when it is missing in props", async () => {
        navProps.nav.next = null;
        await renderComponent();

        expect(navBar.text()).to.be.equal('Prev');
        expect(links).to.have.length(3);
        assertPrevLink(links.at(0));
        assertMainPageLink(links.at(1));
        assertBookMainLink(links.at(2));
    });
});

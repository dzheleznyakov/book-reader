describe('useSearch hook', () => {    
    let useLocationResult = null;

    beforeEach(() => {
        jest.restoreAllMocks();
    });

    afterEach(() => {
        useLocationResult = null;
    });

    mockPackage('react-router-dom', { useLocation: () => useLocationResult});
    const useSearch = () => require('../useSearch').useSearch();

    test('should return query params as object',  () => {
        useLocationResult = { search: '?page=42&otherParam=23' };
        
        const actual = useSearch();

        expect(actual).to.be.deep.equal({ page: '42', otherParam: '23' });
    });

    test('should return empty object when there are not params', () => {
        useLocationResult = { search: null };
        
        const actual = useSearch();
        
        expect(actual).to.be.deep.equal({});
    });

    test('should return empty object when the search string contains only a question mark', () => {
        useLocationResult = { search: '?' };

        const actual = useSearch();

        expect(actual).to.be.deep.equal({});
    });
});
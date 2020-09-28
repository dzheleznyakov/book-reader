describe("useHash hook", () => {
    let useLocationResult = null;

    beforeEach(() => {
        jest.restoreAllMocks();
    });

    afterEach(() => {
        useLocationResult = null;
    });

    mockPackage('react-router-dom', { useLocation: () => useLocationResult});
    const useHash = () => require('../useHash').useHash();

    test("should return null when there is no hash", () => {
        useLocationResult = { hash: '' };

        const actual = useHash();
        
        expect(actual).to.be.equal(null);
    });

    test("should return hash value when ther is one", () => {
        const hashValue = '#loc0101';
        useLocationResult = { hash: hashValue };

        const actual = useHash();

        expect(actual).to.be.equal(hashValue);
    });
});
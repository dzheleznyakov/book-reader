import { useLocation } from "react-router-dom";

export const useSearch = () => {
    let { search } = useLocation();
    if (!search)
        return {};
    return search.substring(1)
        .split('&')
        .filter(e => e.indexOf('=') > 0)
        .map(e => e.split('='))
        .reduce((acc, [key, value]) => ({ ...acc, [key]: value }), {});
};
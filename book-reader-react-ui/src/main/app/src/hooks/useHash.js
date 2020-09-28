import { useLocation } from 'react-router-dom';

export const useHash = () => {
    const { hash } = useLocation();
    return hash || null;
};

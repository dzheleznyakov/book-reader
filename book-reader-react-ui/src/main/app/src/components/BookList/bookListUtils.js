import axios from '../../axios-api';

export const PAGE_SIZE = 10

export const fetchPage = (page) => {
    const params = {
        offset: (page - 1) * PAGE_SIZE,
        limit: PAGE_SIZE,
    };
    return axios.get('/books', { params })
        .then(res => res.data)
};

export const fetchCount = () => axios.get('/books/count')
    .then(res => res.data)
    .catch(err => console.error(err) || 0);

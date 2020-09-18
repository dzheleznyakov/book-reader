import axios from '../../axios-api';

export const fetchBookMainPage = id => axios.get(`/books/${id}`)
    .then(result => result.data)
    .catch(err => {});
import axios from '../../axios-api';

export const fetchChapter = (id, chapterId) => axios.get(`/books/${id}/chapters/${chapterId}`)
    .then(res => res.data)
    .catch(err => {
        console.error(err);
        return {};
    })
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch } from 'react-redux';

import { fetchChapter } from './chaptersHelper';
import DocViewer from '../UI/DocViewer/DocViewer';
import Spinner from '../UI/Spinner/Spinner';
import * as actions from '../../store/actions';

import classes from './Chapter.module.scss';
import { useHash } from '../../hooks/useHash';

const Chapter = () => {
    const { id, chapterId } = useParams();
    const [content, setContent] = useState(null);
    const dispatch = useDispatch();
    const hash = useHash();

    useEffect(() => {
        setContent(null);
        dispatch(actions.fetchChapterNavigation(id, chapterId));
        fetchChapter(id, chapterId)
            .then(c => setContent(c.content));
    }, [id, chapterId, dispatch]);

    useEffect(() => {
        if (!hash)
            window.scrollTo(0, 0);
    }, [hash, chapterId]);

    const docViewer = content ? <DocViewer docs={[content]} /> : <Spinner />;

    return <div className={classes.Chapter}>{docViewer}</div>;
};

export default Chapter;

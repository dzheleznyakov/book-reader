import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import DocViewer from '../UI/DocViewer/DocViewer';
import Spinner from '../UI/Spinner/Spinner';
import * as actions from '../../store/actions';

import classes from './Chapter.module.scss';
import { useHash } from '../../hooks/useHash';

const Chapter = () => {
    const { id, chapterId } = useParams();
    const { content, index: chapterIndex } = useSelector(state => state.chapters);
    const [loading, setLoading] = useState(true)
    const dispatch = useDispatch();
    const hash = useHash();

    useEffect(() => {
        setLoading(true);
        dispatch(actions.fetchChapterNavigation(id, chapterId));
        dispatch(actions.fetchChapterData(id, chapterId));
    }, [id, chapterId, dispatch]);

    useEffect(() => {
        if (!hash)
            window.scrollTo(0, 0);
    }, [hash, chapterId]);

    useEffect(() => {
        if (chapterIndex)
            dispatch(actions.saveBookReadingHistory(id, chapterIndex));
    }, [chapterIndex, id, dispatch]);

    useEffect(() => {
        if (content)
            setLoading(false);
        else
            setLoading(true);
    }, [content]);

    useEffect(() => {
        return () => dispatch(actions.releaseChapterData());
    }, []);

    const docViewer = loading ? <Spinner /> : <DocViewer docs={[content]} />;

    return <div className={classes.Chapter}>{docViewer}</div>;
};

export default Chapter;

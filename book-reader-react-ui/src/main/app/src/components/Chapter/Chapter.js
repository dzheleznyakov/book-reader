import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch } from 'react-redux';

import { fetchChapter } from './chaptersHelper';
import DocViewer from '../UI/DocViewer/DocViewer';
import Spinner from '../UI/Spinner/Spinner';
import * as actions from '../../store/actions';

import classes from './Chapter.module.scss';

const Chapter = props => {
    const { id, chapterId } = useParams();
    const [content, setContent] = useState(null);
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(actions.fetchChapterNavigation(id, chapterId));
        fetchChapter(id, chapterId)
            .then(c => setContent(c.content));
    }, [id, chapterId, dispatch]);

    const docViewer = content ? <DocViewer docs={[content]} /> : <Spinner />;

    return <div className={classes.Chapter}>{docViewer}</div>;
};

export default Chapter;

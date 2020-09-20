import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import { fetchChapter } from './chaptersHelper';
import DocViewer from '../UI/DocViewer/DocViewer';
import Spinner from '../UI/Spinner/Spinner';

const Chapter = props => {
    const { id, chapterId } = useParams();
    const [content, setContent] = useState(null);

    useEffect(() => {
        fetchChapter(id, chapterId)
            .then(c => setContent(c.content));
    }, [id, chapterId]);

    const docViewer = content ? <DocViewer docs={[content]} /> : <Spinner />;

    return docViewer;
};

export default Chapter;

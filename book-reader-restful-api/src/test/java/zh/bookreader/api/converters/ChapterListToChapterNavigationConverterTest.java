package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.bookreader.api.commands.ChapterNavigationCommand;
import zh.bookreader.model.documents.Chapter;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class ChapterListToChapterNavigationConverterTest {
    private static final List<String> CHAPTERS_IDS = ImmutableList.of("preface", "ch01", "ch02", "app01");
    private static final String CHAPTER_NOT_FROM_LIST_ID = "some-other-ch";
    private static final int FIRST = 0;
    private static final int LAST = 3;
    private static final int MIDDLE = 2;

    private ChapterListToChapterNavigationConverter converter = new ChapterListToChapterNavigationConverter();

    private List<Chapter> chapters;

    @BeforeEach
    void setUpChapters() {
        chapters = CHAPTERS_IDS.stream()
                .map(chId -> {
                    Chapter ch = new Chapter();
                    ch.setId(chId);
                    return ch;
                })
                .collect(ImmutableList.toImmutableList());
    }

    @Test
    @DisplayName("Test the given chapter is not in the list")
    void testChapterIsNotInList() {
        ChapterNavigationCommand command = converter.convert(CHAPTER_NOT_FROM_LIST_ID, chapters);

        assertThat(command, is(notNullValue()));
        assertThat(command.getPrev(), is(nullValue()));
        assertThat(command.getNext(), is(nullValue()));
    }

    @Test
    @DisplayName("Test the given chapter is first in the list")
    void testFirstChapter() {
        int ind = FIRST;
        String chapterId = chapters.get(ind).getId();

        ChapterNavigationCommand command = converter.convert(chapterId, chapters);

        assertThat(command, is(notNullValue()));
        assertThat(command.getPrev(), is(nullValue()));
        assertThat(command.getNext(), is(equalTo(chapters.get(ind + 1).getId())));
    }

    @Test
    @DisplayName("Test the given chapter is first in the list")
    void testLastChapter() {
        int ind = LAST;
        String chapterId = chapters.get(ind).getId();

        ChapterNavigationCommand command = converter.convert(chapterId, chapters);

        assertThat(command, is(notNullValue()));
        String expectedPrev = chapters.get(ind - 1).getId();
        assertThat(command.getPrev(), is(equalTo(expectedPrev)));
        assertThat(command.getNext(), is(nullValue()));
    }

    @Test
    @DisplayName("Test the given chapter is in the middle of the list")
    void testMiddleChapter() {
        int ind = MIDDLE;

        String chapterId = chapters.get(ind).getId();
        ChapterNavigationCommand command = converter.convert(chapterId, chapters);

        assertThat(command, is(notNullValue()));
        String expectedPrev = chapters.get(ind - 1).getId();
        assertThat(command.getPrev(), is(equalTo(expectedPrev)));
        String expectedNext = chapters.get(ind + 1).getId();
        assertThat(command.getNext(), is(equalTo(expectedNext)));
    }
}
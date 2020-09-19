package zh.bookreader.services.htmlservices;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.bookreader.model.Book;
import zh.bookreader.model.Chapter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static zh.bookreader.services.htmlservices.TestHelpersKt.getBook1Description;
import static zh.bookreader.testutils.TestUtils.box;

public class TestBookConstants {
    public static final String BOOK_TEST_LIBRARY_PATH = "target/test-classes/library";
    public static final String EMPTY_LIBRARY_PATH = "emptyLibrary";
    public static final String BOOK_TITLE_1 = "Book Title One";
    public static final String BOOK_TITLE_2 = "Book Title Two";
    public static final List<String> AUTHORS_1 = ImmutableList.of("Author One-One", "Author One-Two", "Author Both");
    public static final List<String> AUTHORS_2 = ImmutableList.of("Author Two-One", "Author Two-Two", "Author Both");
    public static final String BOOK_ID_1 = "book-one";
    public static final String BOOK_ID_2 = "book-two";
    public static final String RELEASE_DATE_1 = "January 1970";
    public static final String RELEASE_DATE_2 = "February 1970";
    public static final List<String> TOPICS_1 = ImmutableList.of("Topic One", "Topic Two");
    public static final List<String> TOPICS_2 = ImmutableList.of("Topic Three", "Topic One");
    public static final Byte[] IMAGE_1;
    private static final Byte[] IMAGE_2 = new Byte[0];
    private static final Map<String, String> RESOURCES_1 = ImmutableMap.of(
            "Mock resource 1", "42",
            "Mock resource 2", "forty two");
    private static final Map<String, String> RESOURCES_2 = ImmutableMap.of();
    private static final List<String> CHAPTERS_IDS_1 = ImmutableList.of(
            "preface", "ch01", "ch02", "app");
    private static final List<String> CHAPTERS_IDS_2 = ImmutableList.of(
            "intro", "p01", "p02");

    static {
        String image1Path = "library/book-one/media/square.jpg";
        var image1Url = HtmlBookService.class.getClassLoader().getResource(image1Path);
        if (image1Url == null)
            throw new IllegalStateException("Cannot find file " + image1Path);
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(image1Url.toURI()));
            IMAGE_1 = box(imageBytes);
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Exception when reading file " + image1Path);
        }
    }

    public static Book getBook1() {
        var chapters1 = CHAPTERS_IDS_1.stream()
                .map(TestBookConstants::getChapter)
                .collect(ImmutableList.toImmutableList());

        Book book = new Book();
        book.setTitle(BOOK_TITLE_1);
        book.setAuthors(AUTHORS_1);
        book.setId(BOOK_ID_1);
        book.setReleaseDate(RELEASE_DATE_1);
        book.setTopics(TOPICS_1);
        book.setResources(RESOURCES_1);
        book.setImage(IMAGE_1);
        book.setChapters(chapters1);
        book.setDescription(getBook1Description());
        return book;
    }

    public static Book getBook2() {
        var chapters2 = CHAPTERS_IDS_2.stream()
                .map(TestBookConstants::getChapter)
                .collect(ImmutableList.toImmutableList());

        Book book = new Book();
        book.setTitle(BOOK_TITLE_2);
        book.setAuthors(AUTHORS_2);
        book.setId(BOOK_ID_2);
        book.setReleaseDate(RELEASE_DATE_2);
        book.setTopics(TOPICS_2);
        book.setResources(RESOURCES_2);
        book.setImage(IMAGE_2);
        book.setChapters(chapters2);
        book.setDescription(ImmutableList.of());
        return book;
    }

    private static Chapter getChapter(String name) {
        Chapter ch = new Chapter();
        ch.setId(name);
        return ch;
    }
}

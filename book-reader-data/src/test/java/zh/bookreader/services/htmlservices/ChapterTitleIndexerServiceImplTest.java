package zh.bookreader.services.htmlservices;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Uninterruptibles;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.bookreader.model.documents.Book;
import zh.bookreader.model.documents.Chapter;
import zh.bookreader.model.documents.Document;
import zh.bookreader.model.documents.DocumentFormatting;
import zh.bookreader.model.documents.DocumentType;
import zh.bookreader.model.documents.EnclosingDocument;
import zh.bookreader.model.documents.TextDocument;
import zh.bookreader.services.BookService;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static zh.bookreader.services.htmlservices.TestBookConstants.BOOK_TEST_LIBRARY_PATH;
import static zh.bookreader.testutils.hamcrest.ZhMatchers.exists;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test ChapterTitleIndexerServiceImpl")
class ChapterTitleIndexerServiceImplTest {
    private static final String BOOK_ID = "book-one";
    private static final String CH_0_ID = "ch-0-id";
    private static final String CH_1_ID = "ch-1-id";
    private static final String CH_0_TITLE = "Chapter 0 Title";
    private static final String CH_1_TITLE = "Chapter 1 Title";

    @Mock
    private BookService bookService;

    @InjectMocks
    private ChapterTitleIndexerServiceImpl service;

    private Disposable subscription;
    private File indexFile;
    private Book book;

    @BeforeEach
    void setUpFiles() {
        indexFile = new File("temp.txt");
        service.unsubscribe();
    }

    @BeforeEach
    void setUpBook() {
        Chapter ch0 = new Chapter();
        ch0.setId(CH_0_ID);
        ch0.setContent(getTitleDoc(CH_0_TITLE));

        Chapter ch1 = new Chapter();
        ch1.setId(CH_1_ID);
        ch1.setContent(getTitleDoc(CH_1_TITLE));

        book = new Book();
        book.setId(BOOK_ID);
        book.setChapters(ImmutableList.of(ch0, ch1));
    }

    private Document<List<Document<?>>> getTitleDoc(String title) {
        return EnclosingDocument.builder(DocumentType.INLINED)
                .withFormatting(ImmutableList.of(DocumentFormatting.TITLE))
                .withContent(TextDocument.builder().withContent(title).build())
                .build();
    }

    @AfterEach
    void unsubscribe() {
    if (subscription != null)
            subscription.dispose();
    }

    @AfterEach
    void removeFiles() {
        if (indexFile != null && indexFile.exists())
            indexFile.delete();
    }

    @Test
    @DisplayName("Test index(f, b, l): the call schedules the event for processing")
    void testIndexSubscriptionActivation() {
        Observable<ChapterTitleIndexerServiceImpl.Payload> indexSubject = service.getIndexSubject();
        TestSubscriber<ChapterTitleIndexerServiceImpl.Payload> subscriber = new TestSubscriber<>();
        subscription = indexSubject.subscribe(subscriber::onNext);

        String bookId = "book-one";
        String libraryPath = "library-path";
        service.index(indexFile, bookId, libraryPath);

        subscriber.assertValueCount(1);
        subscriber.assertValue(v -> indexFile == v.indexFile && bookId.equals(v.bookId) && libraryPath.equals(v.libraryPath));
    }

    @Test
    @DisplayName("Test index(f, b, l): if index file [f] exists, it remains untouched")
    void testNoIndexingIfFileExists() throws IOException {
        indexFile.createNewFile();
        assertThat(indexFile, exists());

        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MILLISECONDS);
        ChapterTitleIndexerServiceImpl.Payload payload = getPayload();
        long before = System.currentTimeMillis();
        service.doIndex(payload);
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MILLISECONDS);

        assertThat(indexFile.lastModified(), lessThan(before));
    }

    @Test
    @DisplayName("Test index(f, b, l): if index file [f] does not exist, then it should be created")
    void testIndexIfFileDoesNotExist() throws IOException {
        when(bookService.findById(BOOK_ID))
                .thenReturn(Optional.of(book));

        assertThat(indexFile, not(exists()));

        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MILLISECONDS);
        long before = System.currentTimeMillis();
        service.doIndex(getPayload());
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MILLISECONDS);

        assertThat(indexFile.lastModified(), greaterThan(before));

        List<String> actualLines = Files.readAllLines(indexFile.toPath());
        List<String> expectedLines = ImmutableList.of("#chapter_titles",
                CH_0_ID + "=>" + CH_0_TITLE,
                CH_1_ID + "=>" + CH_1_TITLE);
        assertThat(actualLines, is(equalTo(expectedLines)));
    }

    @Test
    @DisplayName("If book is not found, then the index file should not exist")
    void testBookIsNotFound() {
        when(bookService.findById(BOOK_ID))
                .thenReturn(Optional.empty());

        service.doIndex(getPayload());

        assertThat(indexFile, not(exists()));
    }

    @Nonnull
    private ChapterTitleIndexerServiceImpl.Payload getPayload() {
        return new ChapterTitleIndexerServiceImpl.Payload(indexFile, BOOK_ID, BOOK_TEST_LIBRARY_PATH);
    }
}
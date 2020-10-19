package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.SearchHitCommand;
import zh.bookreader.model.documents.Book;
import zh.bookreader.model.documents.Chapter;
import zh.bookreader.model.search.SearchHit;
import zh.bookreader.services.BookService;
import zh.bookreader.services.ChapterService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@Component
public class SearchHitToSearchHitCommandConverter implements Converter<SearchHit, SearchHitCommand> {
    private final BookService bookService;
    private final ChapterService chapterService;

    public SearchHitToSearchHitCommandConverter(
            BookService bookService,
            ChapterService chapterService
    ) {
        this.bookService = bookService;
        this.chapterService = chapterService;
    }

    @Override
    @Nullable
    public SearchHitCommand convert(@Nullable SearchHit hit) {
        return hit == null
                ? null
                : buildSearchHitCommand(hit);
    }

    @Nullable
    private SearchHitCommand buildSearchHitCommand(@Nonnull SearchHit hit) {
        Optional<Book> bookOptional = bookService.findById(hit.getBookId());
        if (bookOptional.isEmpty())
            return null;

        Book book = bookOptional.get();

        return SearchHitCommand.builder()
                .bookId(book.getId())
                .authors(book.getAuthors())
                .topics(book.getTopics())
                .image(book.getImage())
                .title(book.getTitle())
                .chapterIds(getChapterIds(book, hit))
                .build();
    }

    private List<String[]> getChapterIds(Book book, SearchHit hit) {
        return hit.getChapterNums().stream()
                .filter(i -> i >= 0)
                .map(book::getChapter)
                .map(chapter -> getChapterMetainfo(chapter, hit))
                .collect(ImmutableList.toImmutableList());
    }

    private String[] getChapterMetainfo(Chapter chapter, SearchHit hit) {
        return new String[]{chapter.getId(), chapterService.getTitle(hit.getBookId(), chapter.getId())};
    }

}

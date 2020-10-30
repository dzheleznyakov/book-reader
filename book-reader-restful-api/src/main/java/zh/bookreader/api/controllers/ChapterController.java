package zh.bookreader.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.bookreader.api.commands.ChapterCommand;
import zh.bookreader.api.commands.ChapterNavigationCommand;
import zh.bookreader.api.converters.ChapterListToChapterNavigationConverter;
import zh.bookreader.api.converters.ChapterToChapterCommand;
import zh.bookreader.api.util.ApiController;
import zh.bookreader.model.documents.Book;
import zh.bookreader.model.documents.Chapter;
import zh.bookreader.services.BookService;

import java.util.Objects;
import java.util.Optional;

import static zh.bookreader.api.controllers.ControllersConstants.CONTENT_TYPE;

@ApiController
@RequestMapping(path = "/api/books/{id}/chapters", produces = CONTENT_TYPE)
public class ChapterController {
    private final ChapterToChapterCommand chapterConverter;
    private final BookService bookService;
    private final ChapterListToChapterNavigationConverter navigationConverter;

    public ChapterController(
            ChapterToChapterCommand chapterConverter,
            BookService bookService,
            ChapterListToChapterNavigationConverter navigationConverter
    ) {
        this.chapterConverter = chapterConverter;
        this.bookService = bookService;
        this.navigationConverter = navigationConverter;
    }

    @GetMapping("/{chapterId}")
    public ChapterCommand getChapter(
            @PathVariable("id") String bookId,
            @PathVariable("chapterId") String chapterId
    ) {
        Optional<Book> bookOptional = bookService.findById(bookId);
        if (bookOptional.isEmpty())
            return null;

        Chapter chapter = bookOptional.get().getChapters()
                .stream()
                .filter(ch -> Objects.equals(ch.getId(), chapterId))
                .findAny()
                .orElse(null);

        return chapterConverter.convert(chapter);
    }

    @GetMapping("/{chapterId}/navigation")
    public ChapterNavigationCommand getChapterNavigation(
            @PathVariable("id") String bookId,
            @PathVariable("chapterId") String chapterId
    ) {
        Book book = bookService
                .findById(bookId)
                .orElse(null);

        return book == null ? null : navigationConverter.convert(chapterId, book.getChapters());
    }
}

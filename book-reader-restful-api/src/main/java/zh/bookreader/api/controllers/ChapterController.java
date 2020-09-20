package zh.bookreader.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.bookreader.api.commands.ChapterCommand;
import zh.bookreader.api.converters.ChapterToChapterCommandConverter;
import zh.bookreader.api.util.ApiController;
import zh.bookreader.model.Book;
import zh.bookreader.model.Chapter;
import zh.bookreader.services.BookService;

import java.util.Objects;
import java.util.Optional;

import static zh.bookreader.api.controllers.ControllersConstants.CONTENT_TYPE;

@ApiController
@RequestMapping(path = "/api/books/{id}/chapters", produces = CONTENT_TYPE)
public class ChapterController {
    private final ChapterToChapterCommandConverter chapterConverter;
    private final BookService bookService;

    public ChapterController(ChapterToChapterCommandConverter chapterConverter, BookService bookService) {
        this.chapterConverter = chapterConverter;
        this.bookService = bookService;
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
}

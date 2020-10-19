package zh.bookreader.api.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zh.bookreader.api.commands.BookMainCommand;
import zh.bookreader.api.commands.BookOverviewCommand;
import zh.bookreader.api.converters.BookToBookMainCommandConverter;
import zh.bookreader.api.converters.BookToBookOverviewCommandConverter;
import zh.bookreader.api.util.ApiController;
import zh.bookreader.model.documents.Book;
import zh.bookreader.services.BookService;

import java.util.List;
import java.util.stream.IntStream;

import static zh.bookreader.api.controllers.ControllersConstants.CONTENT_TYPE;

@ApiController
@RequestMapping(path = "/api/books", produces = CONTENT_TYPE)
public class BookController {
    private final BookService bookService;
    private final BookToBookOverviewCommandConverter bookOverviewConverter;
    private final BookToBookMainCommandConverter bookMainConverter;

    public BookController(
            BookService bookService,
            BookToBookOverviewCommandConverter bookOverviewConverter,
            BookToBookMainCommandConverter bookMainConverter
    ) {
        this.bookService = bookService;
        this.bookOverviewConverter = bookOverviewConverter;
        this.bookMainConverter = bookMainConverter;
    }

    @GetMapping
    public List<BookOverviewCommand> getAllBooksOverview(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        List<Book> books = bookService.findAll();
        return shouldReturnEmptyList(offset, limit, books)
                ? ImmutableList.of()
                : IntStream.range(offset, Math.min(offset + limit, books.size()))
                        .mapToObj(books::get)
                        .map(bookOverviewConverter::convert)
                        .collect(ImmutableList.toImmutableList());
    }

    private boolean shouldReturnEmptyList(int offset, int limit, List<Book> books) {
        return offset > books.size()
                || offset < 0
                || limit < 0;
    }

    @GetMapping("/count")
    public int getBooksCount() {
        return bookService.count();
    }

    @GetMapping("/{id}")
    public BookMainCommand getBookMainPage(@PathVariable("id") String bookId) {
        Book book = bookService.findById(bookId)
                .orElse(null);
        return bookMainConverter.convert(book);
    };
}

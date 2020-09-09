package zh.bookreader.api.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zh.bookreader.api.commands.BookOverviewCommand;
import zh.bookreader.api.converters.BookToBookOverviewCommandConverter;
import zh.bookreader.model.Book;
import zh.bookreader.services.BookService;

import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping(path = "/api", produces = "application/json;charset=UTF-8")
public class BookController {
    private final BookService bookService;
    private final BookToBookOverviewCommandConverter bookOverviewConverter;

    public BookController(BookService bookService, BookToBookOverviewCommandConverter bookOverviewConverter) {
        this.bookService = bookService;
        this.bookOverviewConverter = bookOverviewConverter;
    }

    @GetMapping("/books")
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
}

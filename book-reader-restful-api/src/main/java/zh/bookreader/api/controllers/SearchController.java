package zh.bookreader.api.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zh.bookreader.api.commands.SearchHitCommand;
import zh.bookreader.api.converters.SearchHitToSearchHitCommandConverter;
import zh.bookreader.api.util.ApiController;
import zh.bookreader.services.SearchService;

import java.util.List;
import java.util.Objects;

import static zh.bookreader.api.controllers.ControllersConstants.CONTENT_TYPE;

@ApiController
@RequestMapping(path = "/api/search", produces = CONTENT_TYPE)
public class SearchController {
    private static final int MAX_QUERY_LENGTH = 50;

    private final SearchService searchService;
    private final SearchHitToSearchHitCommandConverter searchHitConverter;

    public SearchController(SearchService searchService, SearchHitToSearchHitCommandConverter searchHitConverter) {
        this.searchService = searchService;
        this.searchHitConverter = searchHitConverter;
    }

    @GetMapping
    public List<SearchHitCommand> search(
            @RequestParam("q") String query,
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        String[] queryTokens = preProcess(query).split(" ");
        return searchService.find(ImmutableList.copyOf(queryTokens))
                .stream()
                .skip(offset)
                .limit(limit)
                .map(searchHitConverter::convert)
                .filter(Objects::nonNull)
                .collect(ImmutableList.toImmutableList());
    }

    private String preProcess(String query) {
        int endIndex = Math.min(MAX_QUERY_LENGTH, query.length());
        return query.substring(0, endIndex);
    }
}

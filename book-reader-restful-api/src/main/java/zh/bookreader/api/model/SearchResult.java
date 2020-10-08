package zh.bookreader.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zh.bookreader.api.commands.SearchHitCommand;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResult {
    private Integer totalCount;
    private List<SearchHitCommand> results;
}

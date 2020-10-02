package zh.bookreader.services;

import zh.bookreader.services.util.SearchHit;

import javax.annotation.Nonnull;
import java.util.List;

public interface SearchService {
    @Nonnull
    List<SearchHit> find(List<String> query);
}

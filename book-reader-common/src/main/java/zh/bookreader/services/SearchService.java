package zh.bookreader.services;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public interface SearchService {
    @Nonnull
    Map<String, List<String>> find(List<String> query);
}

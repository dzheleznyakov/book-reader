package zh.bookreader.services.htmlservices;

import java.io.File;

public interface ChapterTitleIndexerService {
    void index(File indexFile, String bookId, String libraryPath);
}

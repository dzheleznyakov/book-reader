package zh.bookreader.services;

import zh.bookreader.model.history.ReadingHistoryItem;

import javax.annotation.Nonnull;

public interface ReadingHistoryService {
    @Nonnull
    ReadingHistoryItem getLastReadChapter(String bookId);
}

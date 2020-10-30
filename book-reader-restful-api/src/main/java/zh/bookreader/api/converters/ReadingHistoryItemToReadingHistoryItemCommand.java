package zh.bookreader.api.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.ReadingHistoryItemCommand;
import zh.bookreader.model.history.ReadingHistoryItem;

import javax.annotation.Nullable;

@Component
public class ReadingHistoryItemToReadingHistoryItemCommand implements Converter<ReadingHistoryItem, ReadingHistoryItemCommand> {
    @Override
    public ReadingHistoryItemCommand convert(@Nullable ReadingHistoryItem item) {
        return item == null ? null : ReadingHistoryItemCommand.builder()
                .bookId(item.getBookId())
                .lastChapterIndex(item.getLastChapterIndex())
                .build();
    }
}

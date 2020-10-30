package zh.bookreader.model.history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ReadingHistoryItem {
    private String bookId;
    private int lastChapterIndex;

    public static final ReadingHistoryItem NULL = new ReadingHistoryItem() {
        @Override
        public String getBookId() {
            return "";
        }

        @Override
        public void setBookId(String bookId) {
        }

        @Override
        public int getLastChapterIndex() {
            return Integer.MIN_VALUE;
        }

        @Override
        public void setLastChapterIndex(int lastChapterIndex) {
        }
    };
}

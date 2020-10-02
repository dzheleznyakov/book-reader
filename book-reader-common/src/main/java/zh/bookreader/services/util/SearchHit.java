package zh.bookreader.services.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SearchHit {
    private String bookId;
    private List<Integer> chapterNums;
}

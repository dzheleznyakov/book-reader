package zh.bookreader.api.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchHitCommand {
    private String bookId;
    private String title;
    private List<String> authors = new ArrayList<>();
    private List<String> topics = new ArrayList<>();
    private Byte[] image = new Byte[0];
    private List<String[]> chapterIds = new ArrayList<>();
}

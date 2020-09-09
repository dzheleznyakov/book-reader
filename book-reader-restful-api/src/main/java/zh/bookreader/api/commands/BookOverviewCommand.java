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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookOverviewCommand {
    private String id;
    private String title;
    private List<String> authors = new ArrayList<>();
    private List<String> topics = new ArrayList<>();
    private Byte[] image = new Byte[0];
}

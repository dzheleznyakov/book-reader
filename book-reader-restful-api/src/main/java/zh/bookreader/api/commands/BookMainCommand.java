package zh.bookreader.api.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookMainCommand {
    private String id;
    private String title;
    private String releaseDate;
    private List<String> authors = new ArrayList<>();
    private List<String> topics = new ArrayList<>();
    private List<? extends DocumentCommand> description = new ArrayList<>();
    private Map<String, String> resources = new HashMap<>();
    private Byte[] image = new Byte[0];
    private List<String> toc = new ArrayList<>();
}

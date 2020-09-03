package zh.bookreader.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Chapter {
    private String name;
    private Document<List<Document<?>>> content;
}

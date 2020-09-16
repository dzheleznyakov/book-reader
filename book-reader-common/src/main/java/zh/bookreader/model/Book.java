package zh.bookreader.model;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Book {
    @Nonnull private String id;
    @Nonnull private String title;
    @Nonnull private String releaseDate;
    @Nonnull private List<String> authors = new ArrayList<>();
    @Nonnull private List<String> topics = new ArrayList<>();
    @Nonnull private List<Document<?>> description = new ArrayList<>();
    @Nonnull private Map<String, String> resources = new HashMap<>();
    @Nonnull private Byte[] image = new Byte[0];
    @Nonnull private List<Chapter> chapters = new ArrayList<>();
}

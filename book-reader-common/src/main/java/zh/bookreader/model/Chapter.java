package zh.bookreader.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chapter {
    private String id;
    private Document<List<Document<?>>> content;

    public String getFirstTitle() {
        return getContent() instanceof EnclosingDocument
                ? getFirstTitle((EnclosingDocument) getContent())
                : "";
    }

    private String getFirstTitle(EnclosingDocument content) {
        EnclosingDocument title = (EnclosingDocument) content
                .findFirst(d -> d instanceof EnclosingDocument && d.getFormatting().contains(DocumentFormatting.TITLE));
        return title == null
                ? ""
                : title.text();
    }
}

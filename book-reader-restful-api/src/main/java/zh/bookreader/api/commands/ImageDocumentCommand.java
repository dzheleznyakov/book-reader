package zh.bookreader.api.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDocumentCommand implements DocumentCommand {
    private String documentType;
    private Byte[] content;
    private String id;
    private Set<String> formatting;
    private String width;
    private String height;
}

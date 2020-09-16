package zh.bookreader.api.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnclosingDocumentCommand implements DocumentCommand {
    private String documentType;
    private List<? extends DocumentCommand> content;
    private String id;
    private Set<String> formatting;
}

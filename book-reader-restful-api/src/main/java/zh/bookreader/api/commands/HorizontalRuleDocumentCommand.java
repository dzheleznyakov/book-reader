package zh.bookreader.api.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zh.bookreader.model.documents.DocumentType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorizontalRuleDocumentCommand implements DocumentCommand {
    private String documentType = DocumentType.HORIZONTAL.toString();
}

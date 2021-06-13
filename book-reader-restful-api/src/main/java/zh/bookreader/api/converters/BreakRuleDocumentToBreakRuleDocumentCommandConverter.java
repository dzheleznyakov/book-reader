package zh.bookreader.api.converters;

import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.BreakRuleDocumentCommand;
import zh.bookreader.model.documents.BreakRuleDocument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Component
public class BreakRuleDocumentToBreakRuleDocumentCommandConverter
        implements BaseDocumentConverter<BreakRuleDocument, BreakRuleDocumentCommand> {
    @Override
    public BreakRuleDocumentCommand convert(@Nullable BreakRuleDocument source) {
        return source == null ? null : new BreakRuleDocumentCommand();
    }

    @Nonnull
    @Override
    public Class<BreakRuleDocument> getSourceClass() {
        return BreakRuleDocument.class;
    }
}

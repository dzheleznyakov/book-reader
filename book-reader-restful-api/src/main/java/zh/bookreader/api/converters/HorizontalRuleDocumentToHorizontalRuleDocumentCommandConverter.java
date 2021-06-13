package zh.bookreader.api.converters;

import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.HorizontalRuleDocumentCommand;
import zh.bookreader.model.documents.HorizontalRuleDocument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Component
public class HorizontalRuleDocumentToHorizontalRuleDocumentCommandConverter
        implements BaseDocumentConverter<HorizontalRuleDocument, HorizontalRuleDocumentCommand> {
    @Override
    public HorizontalRuleDocumentCommand convert(@Nullable HorizontalRuleDocument source) {
        return source == null ? null : new HorizontalRuleDocumentCommand();
    }

    @Nonnull
    @Override
    public Class<HorizontalRuleDocument> getSourceClass() {
        return HorizontalRuleDocument.class;
    }
}

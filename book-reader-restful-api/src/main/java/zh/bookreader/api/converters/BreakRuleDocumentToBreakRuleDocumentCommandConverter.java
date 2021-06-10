package zh.bookreader.api.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.BreakRuleDocumentCommand;
import zh.bookreader.model.documents.BreakRuleDocument;

import javax.annotation.Nullable;

@Component
public class BreakRuleDocumentToBreakRuleDocumentCommandConverter implements Converter<BreakRuleDocument, BreakRuleDocumentCommand> {
    @Override
    public BreakRuleDocumentCommand convert(@Nullable BreakRuleDocument source) {
        return source == null ? null : new BreakRuleDocumentCommand();
    }
}

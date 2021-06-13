package zh.bookreader.api.converters;

import org.springframework.core.convert.converter.Converter;
import zh.bookreader.model.documents.Document;

import javax.annotation.Nonnull;

public interface BaseDocumentConverter<D extends Document<?>, C> extends Converter<D, C> {
    @Nonnull Class<D> getSourceClass();
}

package zh.bookreader.model;

import com.google.common.io.Files;
import lombok.SneakyThrows;
import zh.bookreader.utils.ClassUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

public class ImageDocument extends BaseDocument<Byte[]> {
    private final Byte[] content;

    private ImageDocument(DocumentBuilder<Byte[]> builder) {
        super(builder);
        content = builder.getContent();
    }

    @Nonnull
    @Override
    public Byte[] getContent() {
        return content;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;

        final ImageDocument that = (ImageDocument) obj;

        if (!Arrays.equals(content, that.content)) return false;
        if (!Objects.equals(formatting, that.formatting)) return false;
        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(metadata, that.metadata);
    }

    public int hashCode() {
        int result;
        result = (content != null ? Arrays.hashCode(content) : 0);
        result = 31 * result + (formatting != null ? formatting.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        return result;
    }

    public static DocumentBuilder<Byte[]> builder(DocumentType documentType) {
        return new Builder(documentType);
    }

    static class Builder extends DocumentBuilder<Byte[]> {
        private Byte[] content = new Byte[0];

        Builder(DocumentType documentType) {
            super(documentType);
        }

        @Override
        public Byte[] getContent() {
            return content;
        }

        @Override
        public DocumentBuilder<Byte[]> withContent(Object uri) {
            File file;
            if (uri instanceof URI && (file = new File((URI) uri)).exists()) {
                content = toBytes(file);
            }
            return this;
        }

        @SneakyThrows
        @Nonnull
        private Byte[] toBytes(File file) {
            return ClassUtils.cast(Files.toByteArray(file));
        }

        @Override
        public ImageDocument build() {
            return new ImageDocument(this);
        }
    }
}

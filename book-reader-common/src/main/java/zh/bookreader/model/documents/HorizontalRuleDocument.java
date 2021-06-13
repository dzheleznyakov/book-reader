package zh.bookreader.model.documents;

import javax.annotation.Nonnull;

public class HorizontalRuleDocument extends BaseDocument<Void> {
    public HorizontalRuleDocument() {
        this(HorizontalRuleDocument.builder());
    }

    private HorizontalRuleDocument(DocumentBuilder<Void> builder) {
        super(builder);
    }

    @Nonnull
    @Override
    public Void getContent() {
        return null;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof HorizontalRuleDocument;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DocumentBuilder<Void> {
        protected Builder() {
            super(DocumentType.HORIZONTAL);
        }

        @Override
        public DocumentBuilder<Void> withContent(Object content) {
            return this;
        }

        @Override
        Void getContent() {
            return null;
        }

        @Override
        public HorizontalRuleDocument build() {
            return new HorizontalRuleDocument(this);
        }
    }
}

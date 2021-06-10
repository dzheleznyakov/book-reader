package zh.bookreader.model.documents;

import javax.annotation.Nonnull;

public class BreakRuleDocument extends BaseDocument<Void> {
    private BreakRuleDocument(DocumentBuilder<Void> builder) {
        super(builder);
    }

    @Nonnull
    @Override
    public Void getContent() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BreakRuleDocument;
    }

    public static DocumentBuilder<Void> builder() {
        return new Builder();
    }

    static class Builder extends DocumentBuilder<Void> {
        protected Builder() {
            super(DocumentType.BREAK);
        }

        @Override
        public DocumentBuilder<Void> withContent(Object content) {
            return this;
        }

        @Override
        public BreakRuleDocument build() {
            return new BreakRuleDocument(this);
        }

        @Override
        Void getContent() {
            return null;
        }
    }
}

package zh.bookreader.api.testtuils;

import com.google.common.collect.ImmutableList;
import zh.bookreader.api.commands.DocumentCommand;
import zh.bookreader.api.commands.EnclosingDocumentCommand;
import zh.bookreader.api.commands.TextDocumentCommand;
import zh.bookreader.model.documents.Document;
import zh.bookreader.model.documents.DocumentFormatting;
import zh.bookreader.model.documents.EnclosingDocument;
import zh.bookreader.model.documents.TextDocument;

import java.util.Objects;

public class ToStringHelper {
    public static String toString(Document<?> doc) {
        if (doc instanceof TextDocument)
            return toString((TextDocument) doc);
        if (doc instanceof EnclosingDocument)
            return toString((EnclosingDocument) doc);
        return null;
    }

    public static String toString(DocumentCommand item) {
        if (item instanceof TextDocumentCommand)
            return toString((TextDocumentCommand) item);
        if (item instanceof EnclosingDocumentCommand)
            return toString((EnclosingDocumentCommand) item);
        return null;
    }

    public static String toString(TextDocument doc) {
        return String.format("type=[%s], content=[%s]", doc.getDocumentType(), doc.getContent());
    }

    public static String toString(TextDocumentCommand item) {
        return String.format("type=[%s], content=[%s]", item.getDocumentType(), item.getContent());
    }

    public static String toString(EnclosingDocument doc) {
        return String.format("type=[%s], id=[%s], formatting=[%s], content=[%s]",
                doc.getDocumentType(), doc.getId(), prepareFormatting(doc), prepareContent(doc));
    }

    private static ImmutableList<String> prepareFormatting(EnclosingDocument doc) {
        return doc.getFormatting()
                .stream()
                .map(DocumentFormatting::name)
                .sorted()
                .collect(ImmutableList.toImmutableList());
    }

    private static ImmutableList<String> prepareContent(EnclosingDocument doc) {
        return doc.getContent()
                .stream()
                .map(ToStringHelper::toString)
                .filter(Objects::nonNull)
                .collect(ImmutableList.toImmutableList());
    }

    public static String toString(EnclosingDocumentCommand item) {
        return String.format("type=[%s], id=[%s], formatting=[%s], content=[%s]",
                item.getDocumentType(), item.getId(), prepareFormatting(item), prepareContent(item));
    }

    private static ImmutableList<String> prepareFormatting(EnclosingDocumentCommand item) {
        return item.getFormatting()
                .stream()
                .sorted()
                .collect(ImmutableList.toImmutableList());
    }

    private static ImmutableList<String> prepareContent(EnclosingDocumentCommand item) {
        return item.getContent()
                .stream()
                .map(ToStringHelper::toString)
                .filter(Objects::nonNull)
                .collect(ImmutableList.toImmutableList());
    }
}

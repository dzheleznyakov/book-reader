package zh.bookreader.services.htmlservices.hamcrest

import org.hamcrest.Matcher
import zh.bookreader.model.documents.Document
import zh.bookreader.model.documents.DocumentFormatting
import zh.bookreader.model.documents.DocumentType

fun isOfType(type: String): Matcher<Document<*>> = DocIsOfType(type)
fun isOfType(type: DocumentType): Matcher<Document<*>> = DocIsOfType(type.toString())
fun hasTextContent(text: String): Matcher<Document<*>> = DocHasTextContent(text)
fun hasFormatting(formatting: String): Matcher<Document<*>> = DocHasFormatting(formatting)
fun hasFormatting(formatting: DocumentFormatting): Matcher<Document<*>> = DocHasFormatting(formatting.toString())
fun hasMetadata(metadata: String): Matcher<Document<*>> = DocHasMetadata(metadata)
fun hasMetadata(metadata: Map<String, String>): Matcher<Document<*>> = DocHasMetadata(metadata)
fun containsMetadata(metadata: Map<String, String>): Matcher<Document<*>> = DocContainsMetadata(metadata)
fun hasContent(content: List<Document<*>>): Matcher<Document<*>> = DocHasContent(content)
fun hasId(id: String): Matcher<Document<*>> = DocHasId(id)
fun hasImage(bytes: Array<Byte>): Matcher<Document<*>> = DocHasImage(bytes)
fun isNull(): Matcher<Document<*>> = DocIsNull()
fun isBreakRule(): Matcher<Document<*>> = DocIsBreakRule()
fun isHorizontalRule(): Matcher<Document<*>> = DocIsHorizontalRule()

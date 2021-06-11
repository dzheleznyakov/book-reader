package zh.bookreader.services.htmlservices

import org.jsoup.Jsoup
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.slf4j.LoggerFactory
import zh.bookreader.model.documents.Document
import zh.bookreader.model.documents.DocumentFormatting
import zh.bookreader.model.documents.DocumentType
import zh.bookreader.model.documents.EnclosingDocument
import zh.bookreader.model.documents.ImageDocument
import zh.bookreader.model.documents.RawDocument
import zh.bookreader.model.documents.TextDocument
import zh.bookreader.services.utils.readLines
import java.io.File
import java.lang.Enum.valueOf
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import java.nio.file.Paths
import java.util.Scanner

class HtmlDocumentParser(private val fileName: String) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val tagsToTypes: Map<String, DocumentType>
    private val tagsToFormattings: Map<String, List<DocumentFormatting>>

    init {
        val termMapAsStream = javaClass.classLoader.getResourceAsStream("term-map/term-map.txt")
                ?: throw IllegalStateException("term-map/term-map.txt is not found")
        tagsToTypes = Scanner(termMapAsStream)
                .readLines()
                .asSequence()
                .map(String::trim)
                .filter(String::isNotEmpty)
                .map { line -> line.split("=>") }
                .map { tokens -> tokens[0].trim() to tokens[1].trim() }
                .filter { (_, value) -> value != "_" }
                .map { (key, value) -> key to DocumentType.NULL.parse(value) }
                .toMap()
    }

    init {
        val formattingMapAsStream = javaClass.classLoader.getResourceAsStream("term-map/formatting-map.txt")
                ?: throw IllegalStateException("term-map/formatting-map.txt is not found")
        tagsToFormattings = Scanner(formattingMapAsStream)
                .readLines()
                .asSequence()
                .map(String::trim)
                .filter(String::isNotEmpty)
                .map { line -> line.split("=>") }
                .map { tokens-> tokens[0].trim() to tokens[1].split(";")
                        .map(String::trim)
                        .filter(String::isNotEmpty)
                        .map { DocumentFormatting.ITALIC.parse(it) }
                        .toList()
                }
                .toMap()
    }

    private fun DocumentType.parse(value: String) = valueOf(javaClass, value)
    private fun DocumentFormatting.parse(value: String) = valueOf(javaClass, value)

    fun parse() = File(fileName).run { parseFileContent(readText(), parent) }

    internal fun parseFileContent(fileText: String, dirPath: String = "."): Document<*> {
        val doc = Jsoup.parse("<div>$fileText</div>")
        val body = doc.body()
        if (body.childNodeSize() == 0)
            return Document.NULL

        return parseElement(body.childNode(0), dirPath)
                .run { when {
                    this is EnclosingDocument && this.content.size == 1 -> this.content[0]
                    this is EnclosingDocument && this.content.isEmpty() -> Document.NULL
                    else -> this
                } }
    }

    private fun parseElement(element: Node, dirPath: String): Document<*> {
        log.debug("Processing a node <{}>", element.nodeName())

        return when (element.getDocumentType()) {
            DocumentType.NULL -> Document.NULL
            DocumentType.RAW -> element.toRawDocument()
            DocumentType.TEXT -> element.toTextDocument()
            DocumentType.IMAGE -> element.toImageDocument(dirPath)
            else -> element.toEnclosingDocument(dirPath)
        }
    }

    private fun Node.toRawDocument(): RawDocument =
        RawDocument.builder()
                .withContent(getContentAsHtml())
                .build()

    private fun Node.toTextDocument(): TextDocument =
            TextDocument.builder(getDocumentType())
                    .withContent(getContentAsText())
                    .build()

    private fun Node.toImageDocument(dirPath: String): ImageDocument =
            ImageDocument.builder(getDocumentType())
                    .withId(getId())
                    .withMetadata(getMetadata())
                    .withFormatting(getContentFormatting())
                    .withContent(getImageSource(dirPath))
                    .build()

    private fun Node.toEnclosingDocument(dirPath: String): EnclosingDocument =
            EnclosingDocument.builder(getDocumentType())
                    .withId(getId())
                    .withMetadata(getMetadata())
                    .withContent(getContentAsDocuments(dirPath))
                    .withFormatting(getContentFormatting())
                    .build()

    private fun Node.getContentAsDocuments(dirPath: String) = childNodes()
            .map { child -> parseElement(child, dirPath) }
            .filter { doc -> doc != Document.NULL }

    private fun Node.getContentAsHtml() = this.outerHtml()

    private fun Node.getContentAsText() = (this as TextNode).wholeText

    private fun Node.getId() = attr("id")

    private fun Node.getImageSource(dirPath: String): URI {
        val src = attr("src")
        return try {
            URL(src).toURI()
        } catch (ex: MalformedURLException) {
            Paths.get(dirPath, src).toUri()
        }
    }

    private fun Node.getMetadata(): Map<String, String> {
        val metadata = mutableMapOf("&tag" to nodeName())
        attributes().forEach {
            if (it.key.startsWith(METADATA_PREFIX))
                metadata += it.key.substring(METADATA_PREFIX.length) to it.value
            else if (it.key != "id")
                metadata += "@${it.key}" to it.value
        }
        return metadata
    }

    private fun Node.getContentFormatting(): List<DocumentFormatting> {
        val tagKey = toTagLiteral().toString()
        return if (tagsToFormattings[tagKey] != null) tagsToFormattings[tagKey] as List<DocumentFormatting>
        else tagsToFormattings[nodeName()] ?: listOf()
    }

    private fun Node.getDocumentType(): DocumentType {
        val tagKey = toTagLiteral().toString()
        return when {
            tagKey == "#text" -> DocumentType.TEXT
            tagKey == "#comment" -> DocumentType.NULL
            tagKey == "script" -> DocumentType.RAW
            tagsToTypes[tagKey] != null -> tagsToTypes[tagKey] as DocumentType
            tagsToTypes[nodeName()] != null -> tagsToTypes[nodeName()] as DocumentType
            else -> {
                log.warn("Tag literal [{}] is not recognised ({})", tagKey, fileName)
                DocumentType.NULL
            }
        }
    }

    private fun Node.toTagLiteral(): TagLiteral {
        val cssClasses = attr("class")
                .split(" ")
                .filter { css -> css != "" }
                .toList()
        return TagLiteral(nodeName(), cssClasses, attr("data-type"))
    }
}
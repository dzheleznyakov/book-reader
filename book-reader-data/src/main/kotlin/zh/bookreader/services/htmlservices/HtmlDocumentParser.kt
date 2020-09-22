package zh.bookreader.services.htmlservices

import org.slf4j.LoggerFactory
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import zh.bookreader.model.Document
import zh.bookreader.model.DocumentFormatting
import zh.bookreader.model.DocumentType
import zh.bookreader.model.EnclosingDocument
import zh.bookreader.model.ImageDocument
import zh.bookreader.model.TextDocument
import java.io.File
import java.io.StringReader
import java.lang.Enum.valueOf
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory

class HtmlDocumentParser(private val fileName: String) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val tagsToTypes: Map<String, DocumentType>
    private val tagsToFormattings: Map<String, List<DocumentFormatting>>

    init {
        val uri = javaClass.classLoader.getResource("term-map/term-map.txt")?.toURI()
        tagsToTypes = if (uri == null) mapOf()
                      else File(uri).readLines().asSequence()
                            .map(String::trim)
                            .filter(String::isNotEmpty)
                            .map { line -> line.split("=>") }
                            .map { tokens -> tokens[0].trim() to tokens[1].trim() }
                            .filter { (_, value) -> value != "_" }
                            .map { (key, value) -> key to DocumentType.NULL.parse(value) }
                            .toMap()
    }

    init {
        val uri = javaClass.classLoader.getResource("term-map/formatting-map.txt")?.toURI()
        tagsToFormattings = if (uri == null) mapOf()
                            else File(uri).readLines().asSequence()
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
        var text = fileText.replace(Regex("<img([^>]*)>")) { mr ->
            val (attrs) = mr.destructured
            if (attrs.endsWith('/')) "<img${attrs}>"
            else "<img${attrs}/>"
        }
        text = text.replace(Regex("<col([^>]*)>")) { mr ->
            val (attrs) = mr.destructured
            if (attrs.endsWith('/')) "<col${attrs}>"
            else "<col${attrs}/>"
        }
        text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html [<!ENTITY nbsp \"&#160;\">\n]><body>${text}</body>"

        val xmlInput = InputSource(StringReader(text))
        val doc = DocumentBuilderFactory
                .newInstance()
                .run {
                    isValidating = false
                    isNamespaceAware = false
                    newDocumentBuilder()
                }
                .parse(xmlInput)

        val rootChildren = doc.childNodes
        var bodyOptional: Node? = null
        val length = rootChildren.length
        for (i in 0 until length) {
            val nodeName = rootChildren.item(i).nodeName
            if (nodeName == "body")
                bodyOptional = rootChildren.item(i)
        }
        if (bodyOptional == null || bodyOptional.childNodes.length == 0)
            return Document.NULL
        val content = bodyOptional.childNodes.item(0)

        return parse(content, dirPath)
    }

    private fun parse(content: Node, dirPath: String) =
            if (content.nodeName == "#text") parseText(content.textContent)
            else parseNode(content, dirPath)

    private fun parseText(text: String): TextDocument =
            TextDocument.builder(DocumentType.TEXT)
                    .withContent(text)
                    .build()

    private fun parseNode(node: Node, dirPath: String): Document<*> {
        log.debug("Processing a node <{}>", node.nodeName)
        return when (node.getDocumentType()) {
            DocumentType.NULL -> Document.NULL
            DocumentType.TEXT -> node.toTextDocument()
            DocumentType.IMAGE -> node.toImageDocument(dirPath)
            else -> node.toEnclosingDocument(dirPath)
        }
    }

    private fun Node.toTextDocument(): TextDocument =
            TextDocument.builder(getDocumentType())
                    .withContent(getContentAsText())
                    .withFormatting(getContentFormatting())
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

    private fun Node.getContentAsDocuments(dirPath: String) = childNodes
            .map { node -> parse(node, dirPath) }
            .filter { doc -> doc != Document.NULL }

    private fun <E> NodeList.map(mapper: (Node) -> E): List<E> {
        val resList = mutableListOf<E>()
        for (i in 0 until length)
            resList += mapper(item(i))
        return resList
    }

    private fun Node.getContentAsText() = textContent

    private fun Node.getId():String {
        return attribute("id") ?: ""
    }

    private fun Node.getImageSource(dirPath: String): URI {
        val src = this.attribute("src")
        try {
            return URL(src).toURI()
        } catch (ex: MalformedURLException) {
            return Paths.get(dirPath, src).toUri()
        }
    }

    private fun Node.getMetadata(): Map<String, String> {
        val metadata = mutableMapOf("&tag" to nodeName)
        attributes.forEach { key, value ->
            if (key.startsWith(METADATA_PREFIX))
                metadata += key.substring(METADATA_PREFIX.length) to value
            else if (key != "id")
                metadata += "@$key" to value
        }
        return metadata
    }

    private fun NamedNodeMap.forEach(consumer: (String, String) -> Unit) {
        for (i in 0 until length)
            consumer(item(i).nodeName, item(i).nodeValue)
    }

    private fun Node.getContentFormatting(): List<DocumentFormatting> {
        val tagKey = toTagLiteral().toString()
        return if (tagsToFormattings[tagKey] != null) tagsToFormattings[tagKey] as List<DocumentFormatting>
               else tagsToFormattings[nodeName] ?: listOf()
    }

    private fun Node.getDocumentType(): DocumentType {
        val tagKey = toTagLiteral().toString()
        return when {
            tagsToTypes[tagKey] != null -> tagsToTypes[tagKey] as DocumentType
            tagsToTypes[nodeName] != null -> tagsToTypes[nodeName] as DocumentType
            else -> {
                log.warn("Tag literal [{}] is not recognised", tagKey)
                DocumentType.NULL
            }
        }
    }

    private fun Node.toTagLiteral(): TagLiteral {
        val cssClasses = attribute("class")
                ?.split(" ")
                ?.toList()
                ?: listOf()
        val dataType = attribute("data-type") ?: ""
        return TagLiteral(nodeName, cssClasses, dataType)
    }

    private fun Node.attribute(name: String) = attributes?.getNamedItem(name)?.nodeValue
}
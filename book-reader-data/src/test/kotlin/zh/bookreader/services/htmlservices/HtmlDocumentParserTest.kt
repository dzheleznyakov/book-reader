package zh.bookreader.services.htmlservices

import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import zh.bookreader.model.documents.Document
import zh.bookreader.model.documents.DocumentType.TABLE
import zh.bookreader.model.documents.EnclosingDocument
import zh.bookreader.services.htmlservices.hamcrest.containsMetadata
import zh.bookreader.services.htmlservices.hamcrest.hasContent
import zh.bookreader.services.htmlservices.hamcrest.hasFormatting
import zh.bookreader.services.htmlservices.hamcrest.hasId
import zh.bookreader.services.htmlservices.hamcrest.hasImage
import zh.bookreader.services.htmlservices.hamcrest.hasMetadata
import zh.bookreader.services.htmlservices.hamcrest.hasTextContent
import zh.bookreader.services.htmlservices.hamcrest.isBreakRule
import zh.bookreader.services.htmlservices.hamcrest.isHorizontalRule
import zh.bookreader.services.htmlservices.hamcrest.isNull
import zh.bookreader.services.htmlservices.hamcrest.isOfType
import java.net.URI
import zh.bookreader.model.documents.DocumentType as DocType

@Suppress("UNUSED_PARAMETER")
internal class HtmlDocumentParserTest {
    private val parser = HtmlDocumentParser("/")

    @Test
    @DisplayName("Test parsing a plain text string")
    internal fun testParsingPlainText() {
        val text = "\"mock text\""

        val doc = parser.parseFileContent(text)

        assertThat(doc, isOfType(DocType.TEXT))
        assertThat(doc, hasTextContent(text))
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test parsing an inlined node")
    @CsvSource(
            "<h1>some title</h1>                            ,   ->   , some title, TITLE",
            "<h2>some title</h2>                            ,   ->   , some title, TITLE",
            "<h3>some title</h3>                            ,   ->   , some title, TITLE",
            "<h4>some title</h4>                            ,   ->   , some title, TITLE",
            "<h5>some title</h5>                            ,   ->   , some title, TITLE",
            "<h6>some title</h6>                            ,   ->   , some title, TITLE",
            "<h2 class=\"title\">some title</h2>            ,   ->   , some title, TITLE",
            "<span>some text</span>                         ,   ->   , some text,  _",
            "<span class=\"keep-together\">some text</span> ,   ->   , some text,  _",
            "<span class=\"label\">some text</span>         ,   ->   , some text,  _",
            "<nobr>some text</nobr>                         ,   ->   , some text,  _",
            "<cite>some text</cite>                         ,   ->   , some text,  CITE",
            "<em>some text</em>                             ,   ->   , some text,  EMPH",
            "<i>some text</i>                               ,   ->   , some text,  EMPH",
            "<strong>some text</strong>                     ,   ->   , some text,  BOLD",
            "<b>some text</b>                               ,   ->   , some text,  BOLD",
            "<s>some text</s>                               ,   ->   , some text,  STRIKE",
            "<sup>some text</sup>                           ,   ->   , some text,  SUP",
            "<sub>some text</sub>                           ,   ->   , some text,  SUB",
            "<span data-type=\"index-term\">some text</span>,   ->   , some text,  INDEX_TERM"
    )
    internal fun testParsingInlinedNode(htmlString: String, _d: String,
                                        eText: String, eFormatting: String) {
        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, isOfType(DocType.INLINED))
        assertThat(doc, hasTextContent(eText))
        assertThat(doc, hasFormatting(eFormatting))
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test parsing a code node")
    @CsvSource(
            "<code>some code</code>                     ,   ->   , some code, CODE",
            "<code class=\"function\">some code</code>  ,   ->   , some code, CODE",
            "<code class=\"option\">some code</code>    ,   ->   , some code, CODE",
            "<code class=\"parameter\">some code</code> ,   ->   , some code, CODE",
            "<code class=\"nx\">some code</code>        ,   ->   , some code, CODE; CODE_NAME",
            "<code class=\"n\">some code</code>         ,   ->   , some code, CODE; CODE_NAME",
            "<code class=\"na\">some code</code>        ,   ->   , some code, CODE; CODE_NAME",
            "<code class=\"nb\">some code</code>        ,   ->   , some code, CODE; CODE_NAME",
            "<code class=\"nf\">some code</code>        ,   ->   , some code, CODE; CODE_NAME",
            "<code class=\"nn\">some code</code>        ,   ->   , some code, CODE; CODE_NAME",
            "<code class=\"nv\">some code</code>        ,   ->   , some code, CODE; CODE_VARIABLE",
            "<code class=\"o\">some code</code>         ,   ->   , some code, CODE; CODE_OPERATOR",
            "<code class=\"p\">some code</code>         ,   ->   , some code, CODE; CODE_OPERATOR",
            "<code class=\"s-Affix\">some code</code>   ,   ->   , some code, CODE; CODE_OPERATOR",
            "<code class=\"err\">some code</code>       ,   ->   , some code, CODE; ERROR",
            "<code class=\"ne\">some code</code>        ,   ->   , some code, CODE; ERROR",
            "<code class=\"classname\">some code</code> ,   ->   , some code, CODE; CODE_CLASSNAME",
            "<code class=\"bp\">some code</code>        ,   ->   , some code, CODE; CODE_BOOLEAN",
            "<code class=\"kc\">some code</code>        ,   ->   , some code, CODE; CODE_BOOLEAN",
            "<code class=\"mf\">some code</code>        ,   ->   , some code, CODE; CODE_NUMBER",
            "<code class=\"mi\">some code</code>        ,   ->   , some code, CODE; CODE_NUMBER",
            "<code class=\"s1\">some code</code>        ,   ->   , some code, CODE; CODE_STRING",
            "<code class=\"s2\">some code</code>        ,   ->   , some code, CODE; CODE_STRING",
            "<code class=\"sr\">some code</code>        ,   ->   , some code, CODE; CODE_REGEXP",
            "<code class=\"se\">some code</code>        ,   ->   , some code, CODE; CODE_ESCAPED",
            "<code class=\"c1\">some code</code>        ,   ->   , some code, CODE; CODE_COMMENT",
            "<code class=\"cm\">some code</code>        ,   ->   , some code, CODE; CODE_COMMENT",
            "<code class=\"k\">some code</code>         ,   ->   , some code, CODE; CODE_KEYWORD",
            "<code class=\"kd\">some code</code>        ,   ->   , some code, CODE; CODE_KEYWORD",
            "<code class=\"kn\">some code</code>        ,   ->   , some code, CODE; CODE_KEYWORD"
    )
    internal fun testParsingCodeNode(htmlString: String, _d: String,
                                     eText: String, eFormatting: String) {
        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, isOfType(DocType.INLINED))
        assertThat(doc, hasTextContent(eText))
        assertThat(doc, hasFormatting(eFormatting))
    }

    @Test
    @DisplayName("Test parsing a script node")
    internal fun testParsingScriptNode() {
        val doc = parser.parseFileContent("<script>something inside</script>")

        assertThat(doc, isOfType(DocType.RAW))
    }

    @Test
    @DisplayName("Test parsing a MathML node")
    internal fun testParsingMathML() {
        val doc = parser.parseFileContent("<math>some math</math>")

        assertThat(doc, isOfType(DocType.RAW))
        assertThat(doc, hasFormatting("_"))
        assertThat(doc, hasTextContent("<math>\n some math\n</math>"))
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test parsing a link node")
    @CsvSource(
            "<a href=\"https://my-site.com\">mock text</a>,                                     ->   , HREF,  mock text, @href=>https://my-site.com; &tag=>a,                           _",
            "<a data-type=\"xref\" href=\"some-chapter.html#some-pos\">mock text</a>,           ->   , HREF,  mock text, @href=>some-chapter.html#some-pos; type=>xref; &tag=>a,        _",
            "<a data-type=\"indexterm\" data-primary=\"term1\" data-secondary=\"term2\"></a>,   ->   , HREF,  _        , type=>indexterm; primary=>term1; secondary=>term2; &tag=>a,    INDEX_TERM",
            "<a data-type=\"index:locator\" href=\"#pos\"></a>,                                 ->   , HREF,  _        , type=>index:locator; @href=>#pos; &tag=>a,                     INDEX_LOCATOR",
            "<a class=\"email\" href=\"mailto:jon.snow@nightwatch.org\">mock text</a>,          ->   , EMAIL, mock text, @class=>email; @href=>mailto:jon.snow@nightwatch.org; &tag=>a, _"
    )
    internal fun testParsingLinkNode(htmlString: String, _d: String,
                                     eType: String, eText: String, eMetadata: String, eFormatting: String) {
        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, isOfType(eType))
        assertThat(doc, hasTextContent(eText))
        assertThat(doc, hasFormatting(eFormatting))
        assertThat(doc, hasMetadata(eMetadata))
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test parsing a document containing other documents")
    @CsvSource(
            "<p>mock text</p>,                           ->   , PARAGRAPH, mock text, _",
            "<div>mock text</div>,                       ->   , BLOCK,     mock text, _",
            "<div data-type=\"warning\">mock text</div>, ->   , BLOCK,     mock text, WARNING",
            "<div data-type=\"caution\">mock text</div>, ->   , BLOCK,     mock text, CAUTION",
            "<div data-type=\"note\">mock text</div>,    ->   , BLOCK,     mock text, NOTE",
            "<div data-type=\"tip\">mock text</div>,     ->   , BLOCK,     mock text, TIP",
            "<div class=\"preface\">mock text</div>,     ->   , SECTION,   mock text, LEVEL_0",
            "<div class=\"colophon\">mock text</div>,    ->   , SECTION,   mock text, LEVEL_0",
            "<div class=\"chapter\">mock text</div>,     ->   , SECTION,   mock text, LEVEL_0",
            "<div class=\"index\">mock text</div>,       ->   , SECTION,   mock text, LEVEL_IND",
            "<div class=\"sect1\">mock text</div>,       ->   , SECTION,   mock text, LEVEL_1",
            "<div class=\"sect2\">mock text</div>,       ->   , SECTION,   mock text, LEVEL_2",
            "<div class=\"sect3\">mock text</div>,       ->   , SECTION,   mock text, LEVEL_3",
            "<div class=\"sect4\">mock text</div>,       ->   , SECTION,   mock text, LEVEL_4",
            "<div data-type=\"part\">mock text</div>,    ->   , SECTION,   mock text, _",
            "<div class=\"appendix\">mock text</div>,    ->   , SECTION,   mock text, _",
            "<section>mock text</section>,               ->   , SECTION,   mock text, _",
            "<section data-type=\"appendix\">mock text</section>,   ->   , SECTION, mock text, LEVEL_0",
            "<section data-type=\"sect1\">mock text</section>,      ->   , SECTION, mock text, LEVEL_1",
            "<section data-type=\"sect2\">mock text</section>,      ->   , SECTION, mock text, LEVEL_2",
            "<section data-type=\"colophon\">mock text</section>,   ->   , SECTION, mock text, LEVEL_0",
            "<section data-type=\"chapter\">mock text</section>,    ->   , SECTION, mock text, LEVEL_0",
            "<pre>mock text</pre>,                                  ->   , BLOCK, mock text, LISTING",
            "<pre data-type=\"programlisting\">mock text</pre>,     ->   , BLOCK, mock text, LISTING; CODE",
            "<ul>mock text</ul>,                                    ->   , BLOCK, mock text, UNORDERED_LIST",
            "<ol>mock text</ol>,                                    ->   , BLOCK, mock text, ORDERED_LIST",
            "<li>mock text</li>,                                    ->   , BLOCK, mock text, LIST_ITEM",
            "<ul class=\"simplelist\">mock text</ul>,               ->   , BLOCK, mock text, UNORDERED_LIST; SIMPLE",
            "<dl>mock text</dl>,                                    ->   , BLOCK, mock text, UNORDERED_LIST; DEFINITION",
            "<dt>mock text</dt>,                                    ->   , BLOCK, mock text, DEFINITION_TERM",
            "<dd>mock text</dd>,                                    ->   , BLOCK, mock text, DEFINITION_DESCR",
            "<figure>mock text</figure>,                            ->   , BLOCK, mock text, FIGURE",
            "<aside data-type=\"sidebar\">mock text</aside>,        ->   , BLOCK, mock text, SIDEBAR",
            "<blockquote data-type=\"epigraph\">mock text</blockquote>,          ->   , BLOCK, mock text, EPIGRAPH",
            "<div class=\"ormenabled\" data-type=\"note\">mock text</div>,       ->   , BLOCK,   mock text, NOTE; FRAMED",
            "<div class=\"pagenumrestart\" data-type=\"part\">mock text</div>,   ->   , SECTION, mock text, _",
            "<section class=\"pagebreak-before less_space\" data-type=\"sect1\">mock text</section>,   ->   , SECTION, mock text, _",
            "<section class=\"abouttheauthor\" data-type=\"colophon\">mock text</section>,             ->   , SECTION, mock text, _",
            "<section class=\"pagebreak-before\" data-type=\"sect1\">mock text</section>,              ->   , SECTION, mock text, _"
    )
    internal fun testParsingDocumentContainingOtherDocuments(htmlString: String, _d: String,
                                                             eType: String, eText: String, eFormatting: String) {
        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, isOfType(eType))
        assertThat(doc, hasTextContent(eText))
        assertThat(doc, hasFormatting(eFormatting))
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test parsing node")
    @CsvSource(
            "<div><p>mock text</p></div>,                    ->   , mock text, _",
            "<div class=\"figure\"><p>mock text</p></div>,   ->   , mock text, _",
            "<div><p>mock text</p>another text</div>,        ->   , mock text, another text"
    )
    internal fun testParseNestedDocuments(htmlString: String, _d: String,
                                          parText: String, additionalText: String) {
        val doc = parser.parseFileContent(htmlString)

        val parContent = parText.toTextDoc()?.wrapInPar()
        val additionalContent = additionalText.toTextDoc()
        val expectedContent: List<Document<*>> = listOfNotNull(parContent, additionalContent)

        assertThat(doc, isOfType(DocType.BLOCK))
        assertThat(doc, hasContent(expectedContent))
    }

    @Test
    internal fun testParsingTableNode() {
        val doc = parser.parseFileContent("<table>mock text</table>")

        assertThat(doc, isOfType(TABLE))
        assertThat(doc, hasTextContent("mock text"))
        assertThat(doc, hasFormatting("_"))
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test parsing a table node")
    @CsvSource(
            "<table><thead>mock text</thead></table>,               ->   , TABLE,   mock text, TABLE_HEADER",
            "<table><tbody>mock text</tbody></table>,               ->   , TABLE,   mock text, TABLE_BODY",
            "<table><caption>mock text</caption></table>,           ->   , INLINED, mock text, CAPTION"
    )
    internal fun testParsingInternalTableNode(htmlString: String, _d: String,
                                      eType: String, eText: String, eFormatting: String) {
        val table = parser.parseFileContent(htmlString) as EnclosingDocument
        val doc = table.content[0]

        assertThat(doc, isOfType(eType))
        assertThat(doc, hasTextContent(eText))
        assertThat(doc, hasFormatting(eFormatting))
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test parsing a table node")
    @CsvSource(
            "<table><tr>mock text</tr></table>,                     ->   , TABLE,   mock text, TABLE_ROW",
            "<table><tr class=\"footnotes\">mock text</tr></table>, ->   , TABLE,   mock text, TABLE_ROW; FOOTNOTE"
    )
    internal fun testParsingTableRowNode(htmlString: String, _d: String,
                                              eType: String, eText: String, eFormatting: String) {
        val table = parser.parseFileContent(htmlString) as EnclosingDocument
        val tbody = table.content[0] as EnclosingDocument
        val doc = tbody.content[0]

        assertThat(doc, isOfType(eType))
        assertThat(doc, hasTextContent(eText))
        assertThat(doc, hasFormatting(eFormatting))
    }

    @Test
    @DisplayName("Colgroup tag is parsed as NULL document")
    internal fun testParsingColgroup() {
        val doc = parser.parseFileContent("<colgroup><col /></colgroup>")

        assertThat(doc, isNull())
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test parsing a table node")
    @CsvSource(
            "<table><th>mock text</th></table>,                     ->   , TABLE,   mock text, TABLE_HEADER_CELL",
            "<table><td>mock text</td></table>,                     ->   , TABLE,   mock text, TABLE_CELL"
    )
    internal fun testParsingTableCellNode(htmlString: String, _d: String,
                                         eType: String, eText: String, eFormatting: String) {
        val table = parser.parseFileContent(htmlString) as EnclosingDocument
        val tbody = table.content[0] as EnclosingDocument
        val tr = tbody.content[0] as EnclosingDocument
        val doc = tr.content[0]

        assertThat(doc, isOfType(eType))
        assertThat(doc, hasTextContent(eText))
        assertThat(doc, hasFormatting(eFormatting))
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test parsing an image node with closed or unclosed tag")
    @ValueSource(strings = ["<img src=\"path/to/file\">", "<img src=\"path/to/file\" />"])
    internal fun testParsingImageWithClosedOrUnclosedTag(htmlString: String) {
        assertDoesNotThrow { parser.parseFileContent(htmlString) }
    }

    @Test
    @DisplayName("Test parsing an image node")
    internal fun testParsingImageNode() {
        val imagePath = "library/book-one/media/square.jpg"
        val imageUri: URI = javaClass.classLoader.getResource(imagePath).toURI()
                ?: throw IllegalStateException("Image [$imagePath] not found")
        val htmlString = "<img id=\"42\" src=\"${imageUri}\" width=\"200\" height=\"300\"/>"

        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, isOfType(DocType.IMAGE))
        assertThat(doc, hasId("42"))
        assertThat(doc, hasMetadata("@src=>$imageUri; @width=>200; @height=>300; &tag=>img"))
        assertThat(doc, hasImage(imageUri.toBytes()))
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test parsing a footnote node")
    @CsvSource(
            "<a data-type=\"noteref\" id=\"42a\" href=\"#42b\" class=\"totri-footnote\">1</a>,   ->   , HREF,  42a, 1,         type=>noteref; @href=>#42b; @class=>totri-footnote; &tag=>a, FOOTNOTE_REF",
            "<a id=\"42b\" href=\"#42a\" class=\"totri-footnote\">1</a>,                         ->   , HREF,  42b, 1,         @href=>#42a; @class=>totri-footnote; &tag=>a,                FOOTNOTE",
            "<div data-type=\"footnotes\">mock text</div>,                                       ->   , BLOCK, _,   mock text, type=>footnotes; &tag=>div,                                  FOOTNOTE"
    )
    internal fun testParsingFootnoteNode(htmlString: String, _d: String,
                                         eType: String, eId: String, eText: String, eMetadata: String, eFormatting: String) {
        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, isOfType(eType))
        assertThat(doc, hasId(eId))
        assertThat(doc, hasTextContent(eText))
        assertThat(doc, hasMetadata(eMetadata))
        assertThat(doc, hasFormatting(eFormatting))
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test parsing a node to be ignored")
    @CsvSource(
            "div,      annotator-outer annotator-viewer viewer annotator-hide",
            "div,      annotator-modal-wrapper annotator-editor-modal annotator-editor annotator-hide",
            "div,      annotator-modal-wrapper annotator-delete-confirm-modal",
            "div,      annotator-adder",
            "div,      annotator-outer",
            "a,        js-close-delete-confirm annotator-cancel close",
            "textarea, js-editor",
            "ul,       adders",
            "li,       copy",
            "li,       add-highlight",
            "li,       add-note",
            "div,      delete-confirm",
            "li,       delete annotator-hide",
            "li,       cancel",
            "li,       save",
            "a,        link-to-markdown",
            "form,     _"
    )
    internal fun testParsingNodeToBeIgnored(tag: String, cssClass: String) {
        val htmlString = (tag to cssClass).toHtmlString()

        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, isNull())
    }

    @Test
    @DisplayName("Test parsing a break rule")
    internal fun testBreakRule() {
        val htmlString = "<br />"

        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, isBreakRule())
    }

    @Test
    @DisplayName("Test parsing a horizontal rule")
    internal fun testHorizontalRule() {
        val htmlString = "<hr />"

        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, isHorizontalRule())
    }

    @Test
    @DisplayName("NULL document is not added to content")
    internal fun testNullDocIsNotAddedToContent() {
        val htmlString = "<div>$TAG_TO_IGNORE<p>mock text</p></div>"

        val doc = parser.parseFileContent(htmlString)

        val eContent: List<Document<*>> = listOfNotNull("mock text".toTextDoc()?.wrapInPar())

        assertThat(doc, hasContent(eContent))
    }

    @Test
    @DisplayName("Line breaks in text nodes are preserved")
    internal fun testLineBreaksInTextPreserved() {
        val doc = parser.parseFileContent("line 1\n line 2")

        assertThat(doc, hasTextContent("line 1\n line 2"))
    }

    @Test
    @DisplayName("Special characters in text nodes are preserved")
    internal fun testSpecialCharactersInTextPreserved() {
        val doc = parser.parseFileContent("> line 1")

        assertThat(doc, hasTextContent("> line 1"))
    }

    @Test
    @DisplayName("If a node contains id, it gets picked up")
    internal fun testNodeId() {
        val htmlString = "<div id=\"mock-id\">some text</div>"

        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, hasId("mock-id"))
    }

    @Test
    @DisplayName("If a node has an unrecognised tag literal, it falls back to just tag name")
    internal fun testTagLiteralFallsBackToTagName() {
        val htmlString = "<div class=\"fake-css-class\">mock text</div>"

        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, isOfType(DocType.BLOCK))
        assertThat(doc, hasFormatting("_"))
    }

    @Test
    @DisplayName("If a node has an unknown tag name, then it is ignored")
    internal fun testUnknownTagName() {
        val htmlString = "<tagname>mock text</tagname>"

        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, isNull())
    }

    @Test
    @DisplayName("Data-attributes from enclosing html tags are all collected")
    internal fun testDataAttributes() {
        val htmlString = "<div data-type=\"mock-type\" data-value=\"mock-value\" data-blah-blah=\"mock-blah\">mock text</div>"

        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, containsMetadata(mapOf("type" to "mock-type", "value" to "mock-value", "blah-blah" to "mock-blah")))
    }

    @Test
    @DisplayName("Data-attributes and regular attributes and the tag name are all collected")
    internal fun testAttributesCollected() {
        val htmlString = "<div data-type=\"xref\" name=\"checkbox\" width=\"200px\" disabled=\"true\">mock text</div>"

        val doc = parser.parseFileContent(htmlString)

        assertThat(doc, hasMetadata(mapOf("type" to "xref", "@name" to "checkbox", "@width" to "200px", "@disabled" to "true", "&tag" to "div")))
    }
}
package org.finder.html

import org.apache.commons.io.IOUtils
import spock.lang.Specification

import java.nio.charset.StandardCharsets

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull

class HtmlParserTest extends Specification {

    private HtmlParser parser = HtmlParser.build();

    def 'Should find element by ID'() {
        given:
        String html = """
            <div class="parent"> 
                <a id="awesome-button">Button</a>
            </div>
        """

        when:
        Optional<PageElement> actual = parser.findElementById(
                IOUtils.toInputStream(html, StandardCharsets.UTF_8),
                "awesome-button")

        then:
        PageElement elem = actual.get()
        assertEquals("a", elem.tagName)
        assertEquals(['id': 'awesome-button'], elem.attributes)
        assertEquals("Button", elem.text)

        assertEquals("div", elem.parent?.tagName)
        assertEquals(['class': 'parent'], elem.parent?.attributes)
        assertEquals('', elem.parent?.text)
        assertNull(elem.parent.parent)
    }

    def 'Should parse all elements into list'() {
        given:
        String html = """
            <div class="grandpa"> 
                <div class="parent"> 
                    <a id="awesome-button">Button</a>
                </div>
            </div>
        """

        when:
        List<PageElement> actual = parser.parseAllElements(IOUtils.toInputStream(html, StandardCharsets.UTF_8))

        then:
        PageElement grandPa = PageElement.create("div").attr("class", "grandpa").text('').build()
        PageElement parent = PageElement.create("div").attr("class", "parent").text('').parent(grandPa).build()
        PageElement button = PageElement.create("a").attr("id", "awesome-button").text('Button').parent(parent).build()


        [grandPa, parent, button] == actual
    }

}

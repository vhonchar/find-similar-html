package org.finder.command

import org.apache.commons.io.IOUtils
import org.finder.html.PageElement
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class FindSimilarCommandTest extends Specification {

    private FindSimilarCommand command = FindSimilarCommand.buildDefault();

    def 'Should find elem if text were changed'() {
        given:
        String src = """
            <div class="grandpa"> 
                <div class="parent"> 
                    <a id="awesome-button">Button</a>
                </div>
            </div>
        """
        String changed = """
            <div class="grandpa"> 
                <div class="parent"> 
                    <a id="awesome-button">Link</a>
                </div>
            </div>
        """

        when:
        PageElement elem = command.findSimilar("awesome-button",
                IOUtils.toInputStream(src, StandardCharsets.UTF_8),
                IOUtils.toInputStream(changed, StandardCharsets.UTF_8))

        then:
        elem.toString() == 'div > div > a'
    }

    def 'Should find elem if attr were changed'() {
        given:
        String src = """
            <div class="grandpa"> 
                <div class="parent"> 
                    <a id="awesome-button">Button</a>
                </div>
            </div>
        """
        String changed = """
            <div class="grandpa"> 
                <div class="parent"> 
                    <a id="button">Button</a>
                </div>
            </div>
        """

        when:
        PageElement elem = command.findSimilar("awesome-button",
                IOUtils.toInputStream(src, StandardCharsets.UTF_8),
                IOUtils.toInputStream(changed, StandardCharsets.UTF_8))

        then:
        elem.toString() == 'div > div > a'
    }

    def 'Should find elem if tag name were changed'() {
        given:
        String src = """
            <div class="grandpa"> 
                <div class="parent"> 
                    <a id="awesome-button">Button</a>
                </div>
            </div>
        """
        String changed = """
            <div class="grandpa"> 
                <div class="parent"> 
                    <button id="button">Button</button>
                </div>
            </div>
        """

        when:
        PageElement elem = command.findSimilar("awesome-button",
                IOUtils.toInputStream(src, StandardCharsets.UTF_8),
                IOUtils.toInputStream(changed, StandardCharsets.UTF_8))

        then:
        elem.toString() == 'div > div > button'
    }

    def "Test changing of place, text and attributes"() {
        given:
        String src = """
            <div class="grandpa"> 
                <div class="parent"> 
                    <a id="make-everything-ok-button" 
                        class="btn btn-success" 
                        href="#ok" 
                        title="Make-Button" 
                        rel="next" 
                        onclick="javascript:window.okDone(); return false;">
                        
                        Make everything OK
                    </a>
                </div>
            </div>
        """
        String changed = """
            <div class="grandpa"> 
                <div class="parent"> 
                    <button class="btn btn-success" onclick="javascript:location='http://google.com';" style="display:none">
                            Make everything OK
                    </button>
                </div>
                <div class="another-parent"> 
                    <a class="btn btn-success" href="#ok" 
                        title="Make-Button" 
                        rel="next" 
                        onclick="javascript:window.okFinalize(); return false;">
                        
                        Do all GREAT
                    </a>
                </div>
            </div>
        """

        when:
        PageElement elem = command.findSimilar("make-everything-ok-button",
                IOUtils.toInputStream(src, StandardCharsets.UTF_8),
                IOUtils.toInputStream(changed, StandardCharsets.UTF_8))

        then:
        elem.toString() == 'div > div [1] > a'
    }

    def 'sample-2-container-and-clone'() {
        given:
        String src = '/sample-0-origin.html'
        String dest = '/sample-2-container-and-clone.html'

        when:
        PageElement elem = command.findSimilar("make-everything-ok-button",
                IOUtils.getResourceAsStream(src),
                IOUtils.getResourceAsStream(dest))

        then:
        elem.toString() == 'html > body [1] > div > div [1] > div [2] > div > div > div [1] > div > a'
    }

    def 'sample-1-evil-gemini'() {
        given:
        String src = '/sample-0-origin.html'
        String dest = '/sample-1-evil-gemini.html'

        when:
        PageElement elem = command.findSimilar("make-everything-ok-button",
                IOUtils.getResourceAsStream(src),
                IOUtils.getResourceAsStream(dest))

        then:
        elem.toString() == 'html > body [1] > div > div [1] > div [2] > div > div > div [1] > a [1]'
    }

    def 'sample-3-the-escape'() {
        given:
        String src = '/sample-0-origin.html'
        String dest = '/sample-3-the-escape.html'

        when:
        PageElement elem = command.findSimilar("make-everything-ok-button",
                IOUtils.getResourceAsStream(src),
                IOUtils.getResourceAsStream(dest))

        then:
        elem.toString() == 'html > body [1] > div > div [1] > div [2] > div > div > div [2] > a'
    }

    def 'sample-4-the-mash'() {
        given:
        String src = '/sample-0-origin.html'
        String dest = '/sample-4-the-mash.html'

        when:
        PageElement elem = command.findSimilar("make-everything-ok-button",
                IOUtils.getResourceAsStream(src),
                IOUtils.getResourceAsStream(dest))

        then:
        elem.toString() == 'html > body [1] > div > div [1] > div [2] > div > div > div [2] > a'
    }
}

package org.finder.command;

import org.finder.html.HtmlParser;
import org.finder.html.PageElement;

import java.io.InputStream;
import java.util.Comparator;
import java.util.Map;

public class FindSimilarCommand {

    public static FindSimilarCommand buildDefault(){
        return new FindSimilarCommand(new HtmlParser());
    }

    private HtmlParser parser;

    public FindSimilarCommand(final HtmlParser parser) {
        this.parser = parser;
    }

    public PageElement findSimilar(String id, InputStream src, InputStream target) {
        PageElement elemToFind = parser.findElementById(src, id)
                .orElseThrow(IllegalArgumentException::new);
        return findSimilar(elemToFind, target);
    }

    public PageElement findSimilar(PageElement srcElem, InputStream html) {
        return parser.parseAllElements(html)
                .stream()
                .min(Comparator.comparingInt(target -> calculateDistance(srcElem, target)))
                .orElseThrow(IllegalArgumentException::new);
    }

    private int calculateDistance(final PageElement srcElem, final PageElement targetElem) {
        Integer distance = Integer.MAX_VALUE;
        if (srcElem.getTagName().equals(targetElem.getTagName())) {
            distance--;
        }
        if (srcElem.getText().equals(targetElem.getTagName())) {
            distance--;
        }
        if (srcElem.getParent() != null && targetElem.getParent() != null) {
            distance -= Integer.MAX_VALUE - calculateDistance(srcElem.getParent(), targetElem.getParent());
        }

        for (Map.Entry<String, String> attr : srcElem.getAttributes().entrySet()) {
            Map<String, String> targetAttrs = targetElem.getAttributes();
            String name = attr.getKey();
            String value = attr.getValue();

            if (targetAttrs.containsKey(name) && targetAttrs.get(name).equals(value)) {
                distance--;
            }
        }
        if(srcElem.getIndex() == targetElem.getIndex()) {
            distance--;
        }
        return distance;
    }
}

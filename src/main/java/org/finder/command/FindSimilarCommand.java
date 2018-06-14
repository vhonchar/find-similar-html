package org.finder.command;

import com.google.common.collect.ImmutableMap;
import org.finder.html.HtmlParser;
import org.finder.html.PageElement;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class FindSimilarCommand {

    private static Map<String, Integer> WEIGHT = ImmutableMap.of(
            "title", 100,
            "text", 100,
            "href", 100,
            "onclick", 150,
            "class", 100
    );

    public static FindSimilarCommand buildDefault() {
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
                .min(Comparator.comparingInt(target -> calculateDistance(srcElem, target, WEIGHT)))
                .orElseThrow(IllegalArgumentException::new);
    }

    private int calculateDistance(final PageElement srcElem, final PageElement targetElem, final Map<String, Integer> weight) {
        Integer distance = Integer.MAX_VALUE;
        if (srcElem.getTagName().equals(targetElem.getTagName())) {
            distance -= weight.getOrDefault("tag", 1);
        }
        if (srcElem.getText().equals(targetElem.getTagName())) {
            distance -= weight.getOrDefault("text", 1);
        }
        if (srcElem.getParent() != null && targetElem.getParent() != null) {
            distance -= Integer.MAX_VALUE - calculateDistance(srcElem.getParent(), targetElem.getParent(), Collections.emptyMap());
        }

        for (Map.Entry<String, String> attr : srcElem.getAttributes().entrySet()) {
            Map<String, String> targetAttrs = targetElem.getAttributes();
            String name = attr.getKey();
            String value = attr.getValue();

            if (targetAttrs.containsKey(name) && targetAttrs.get(name).equals(value)) {
                distance -= weight.getOrDefault(name, 1);
            }
        }
        if (srcElem.getIndex() == targetElem.getIndex()) {
            distance -= weight.getOrDefault("index", 1);
        }
        return distance;
    }
}

package org.finder.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class HtmlParser {

    private static final String CHARSET_NAME = "utf8";

    public static HtmlParser build() {
        return new HtmlParser();
    }

    public Document parse(File file) {
        try {
            return parse(new FileInputStream(file), file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Document parse(InputStream html, String baseUrl) {
        try {
            return Jsoup.parse(html, CHARSET_NAME, baseUrl, Parser.xmlParser());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PageElement> parseAllElements(InputStream html) {
        Document doc = parse(html, "don't care");
        return doc.getAllElements().stream()
                .map(this::parseElement)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<PageElement> parseAllElements(final File htmlFile) {
        try {
            return parseAllElements(new FileInputStream(htmlFile));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Optional<PageElement> findElementById(File htmlFile, String id) {
        Document doc = parse(htmlFile);
        return Optional.of(doc.getElementById(id))
                .map(this::parseElement);

    }

    public Optional<PageElement> findElementById(InputStream html, String id) {
        Document doc = parse(html, "don't care");
        return Optional.ofNullable(doc.getElementById(id))
                .map(this::parseElement);
    }

    private PageElement parseElement(final Element htmlElem) {
        if(htmlElem == null || "#root".equals(htmlElem.tagName())) {
            return null;
        }
        return PageElement.create(htmlElem.tagName())
                .attrs(toMap(htmlElem.attributes()))
                .text(htmlElem.ownText())
                .parent(parseElement(htmlElem.parent()))
                .index(htmlElem.elementSiblingIndex())
                .build();
    }

    private Map<String, String> toMap(final Attributes attributes) {
        return attributes.asList()
                .stream()
                .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
    }
}

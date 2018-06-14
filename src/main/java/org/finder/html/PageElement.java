package org.finder.html;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;

public class PageElement {


    public static PageElementBuilder create(String tagName) {
        return new PageElement().new PageElementBuilder(tagName);
    }

    private int index = 0;
    private String tagName;
    private Map<String, String> attributes = new HashMap<>();
    private String text;
    private PageElement parent;

    private PageElement() {
    }

    public String getTagName() {
        return tagName;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getText() {
        return text;
    }

    public PageElement getParent() {
        return parent;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PageElement that = (PageElement) o;

        return new EqualsBuilder()
                .append(tagName, that.tagName)
                .append(attributes, that.attributes)
                .append(text, that.text)
                .append(parent, that.parent)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tagName)
                .append(attributes)
                .append(text)
                .append(parent)
                .toHashCode();
    }

    @Override
    public String toString() {
        String self = String.format(" > %s%s",
                tagName,
                index > 0 ? " [" + index + "]" : "");

        return parent != null
                ? parent.toString() + self
                : tagName;
    }

    public class PageElementBuilder {

        public PageElementBuilder(final String tagName) {
            PageElement.this.tagName = tagName;
        }

        public PageElementBuilder attr(String name, String value) {
            PageElement.this.attributes.put(name, value);
            return this;
        }

        public PageElementBuilder attrs(Map<String, String> attrs) {
            PageElement.this.attributes.putAll(attrs);
            return this;
        }

        public PageElementBuilder text(final String text) {
            PageElement.this.text = text;
            return this;
        }

        public PageElementBuilder parent(final PageElement parent) {
            PageElement.this.parent = parent;
            return this;
        }

        public PageElementBuilder index(final int i) {
            PageElement.this.index = i;
            return this;
        }

        public PageElement build() {
            return PageElement.this;
        }
    }
}

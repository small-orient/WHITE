package com.baidetu.util;

import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Map;

/**
 * Markdown转换成HTML工具类（前端使用Markdown解析器解析了，未使用到）
 */
public class MarkdownUtils {

    /**
     * Markdown转换HTML格式
     *
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 处理标签属性
     */
    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String s, Map<String, String> map) {
            //改变a标签target属性为_blank
            if (node instanceof Link) {
                map.put("target", "_blank");
            }
        }
    }

}

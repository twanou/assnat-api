package net.daneau.assnat.scrappers;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import net.daneau.assnat.scrappers.configuration.AssNatWebClient;
import net.daneau.assnat.scrappers.exceptions.ScrapingException;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;
import net.daneau.assnat.scrappers.utils.ScrapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlParagraph;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AssNatLogScraper {

    private final AssNatWebClient webClient;

    public ScrapedLogNode scrape(String relativeUrl) {
        HtmlPage page = this.webClient.getRelativePage(relativeUrl);
        ArrayList<InternalLogNode> internalLogNodes = this.buildSummary(page.getByXPath("//div[@class='tableMatieresJournal']//a"));
        this.addParagraphs(internalLogNodes, page.getByXPath("//contenu//p"));
        return this.toLogNode(internalLogNodes.get(0));
    }

    private ArrayList<InternalLogNode> buildSummary(List<HtmlAnchor> anchors) {
        ArrayList<InternalLogNode> nodes = new ArrayList<>();
        nodes.add(InternalLogNode.builder()
                .title("Journal des débats")
                .margin(-1)
                .build());

        for (HtmlAnchor anchor : anchors) {
            float anchorMargin = this.getAnchorMargin(anchor);
            InternalLogNode parentNode = nodes.get(nodes.size() - 1);
            while (anchorMargin <= parentNode.margin) {
                parentNode = parentNode.previous;
            }
            InternalLogNode newNode = InternalLogNode.builder()
                    .title(anchor.getVisibleText().replace("\n", " "))
                    .previous(nodes.get(nodes.size() - 1))
                    .margin(anchorMargin)
                    .href(anchor.getHrefAttribute().substring(1))
                    .build();
            parentNode.children.add(newNode);
            nodes.add(newNode);
        }
        return nodes;
    }

    private void addParagraphs(List<InternalLogNode> internalLogNodes, List<HtmlParagraph> paragraphs) {
        Iterator<InternalLogNode> summaryNodeIterator = internalLogNodes.iterator();
        InternalLogNode parent = summaryNodeIterator.next();
        InternalLogNode current = summaryNodeIterator.next();
        for (HtmlParagraph paragraph : paragraphs) {
            if (this.isAnchorMatch(current.href, paragraph.getByXPath(".//a"))) {
                parent = current;
                current = summaryNodeIterator.hasNext() ? summaryNodeIterator.next() : InternalLogNode.builder().build();
            } else {
                parent.paragraphs.add(paragraph.getVisibleText());
            }
        }

        if (summaryNodeIterator.hasNext()) {
            throw new ScrapingException("Des éléments du sommaires ont été ignorés");
        }
    }

    private ScrapedLogNode toLogNode(InternalLogNode internalLogNode) {
        return ScrapedLogNode.builder()
                .title(internalLogNode.title)
                .paragraphs(Collections.unmodifiableList(internalLogNode.paragraphs))
                .children(internalLogNode.children
                        .stream()
                        .map(this::toLogNode)
                        .toList())
                .build();
    }

    private boolean isAnchorMatch(String href, List<HtmlAnchor> anchors) {
        return anchors.stream().anyMatch(anchor -> StringUtils.contains(anchor.getNameAttribute(), href));
    }

    private float getAnchorMargin(HtmlAnchor anchor) {
        Node styleAttribute = anchor.getParentNode().getAttributes().getNamedItem("style");
        String marginTextValue = StringUtils.substringBetween(styleAttribute.getNodeValue(), "margin-left:", ";");
        String textIndentTextValue = StringUtils.substringBetween(styleAttribute.getNodeValue(), "text-indent:", ";");
        float margin = ScrapeUtils.extractFloat(marginTextValue);
        float textIndent = textIndentTextValue != null ? ScrapeUtils.extractFloat(textIndentTextValue) : 0;
        return margin + textIndent;
    }

    @Builder
    private static class InternalLogNode {
        public final String title;
        public final InternalLogNode previous;
        public final float margin;
        public final String href;
        public final ArrayList<InternalLogNode> children = new ArrayList<>();
        public final ArrayList<String> paragraphs = new ArrayList<>();
    }
}
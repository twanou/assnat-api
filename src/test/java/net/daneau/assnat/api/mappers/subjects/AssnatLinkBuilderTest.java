package net.daneau.assnat.api.mappers.subjects;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssnatLinkBuilderTest {

    private final AssnatLinkBuilder assnatLinkBuilder = new AssnatLinkBuilder("https://baseurl.com", "/page");

    @Test
    void getUrl() {
        String url = this.assnatLinkBuilder.getUrl(4, 18, LocalDate.of(1995, 10, 30), "123456", "#anchor");
        assertEquals("https://baseurl.com/page/4-18/journal-debats/19951030/123456.html#anchor", url);
    }
}
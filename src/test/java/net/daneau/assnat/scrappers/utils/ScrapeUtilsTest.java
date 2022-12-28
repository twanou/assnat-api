package net.daneau.assnat.scrappers.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScrapeUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"abcdefg1234hijklmnop56", "1234.56pt"})
    void onlyDigitsToInt() {
        int result = ScrapeUtils.onlyDigitsToInt("abcdefg1234hijklmnop56");
        assertEquals(123456, result);
    }
}
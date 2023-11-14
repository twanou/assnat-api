package net.daneau.assnat.scrappers.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScrapeUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"1234.56cm", "1234.56pt"})
    void onlyDigitsToInt(String value) {
        int result = ScrapeUtils.onlyDigitsToInt(value);
        assertEquals(1234.56, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234.56cm", "1234.56pt", "  1234.56cm ", "1234.56"})
    void extractFloatWithDecimal(String value) {
        float result = ScrapeUtils.extractFloat(value);
        assertEquals(1234.56f, result, 0.001f);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "1234pt", "  1234cm ",})
    void extractFloatNoDecimals(String value) {
        float result = ScrapeUtils.extractFloat(value);
        assertEquals(1234f, result, 0.001f);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1234", "-1234pt", "  -1234cm ",})
    void extractFloatNegative(String value) {
        float result = ScrapeUtils.extractFloat(value);
        assertEquals(-1234f, result, 0.001f);
    }
}

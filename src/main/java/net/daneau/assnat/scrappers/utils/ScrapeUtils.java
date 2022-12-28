package net.daneau.assnat.scrappers.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RegExUtils;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScrapeUtils {

    private static final Pattern NOT_DIGITS = Pattern.compile("\\D");

    public static int onlyDigitsToInt(String str) {
        return Integer.parseInt(RegExUtils.removeAll(str, NOT_DIGITS));
    }
}

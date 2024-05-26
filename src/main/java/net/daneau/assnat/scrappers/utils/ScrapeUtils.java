package net.daneau.assnat.scrappers.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RegExUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScrapeUtils {

    private static final Pattern NOT_DIGITS = Pattern.compile("\\D");
    private static final Pattern FLOAT_PATTERN = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");

    public static int onlyDigitsToInt(String str) {
        return Integer.parseInt(RegExUtils.removeAll(str, NOT_DIGITS));
    }

    public static float extractFloat(String str) {
        Matcher matcher = FLOAT_PATTERN.matcher(str);
        if(matcher.find()) {
            return Float.parseFloat(matcher.group());
        }
        throw new IllegalArgumentException();
    }
}

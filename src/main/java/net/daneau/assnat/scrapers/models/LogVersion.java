package net.daneau.assnat.scrapers.models;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@RequiredArgsConstructor
public enum LogVersion {
    FINAL("finale"),
    PRELIMINARY("préliminaire");

    private final String text;

    public static LogVersion fromText(String text) {
        return Arrays.stream(LogVersion.values())
                .filter(v -> StringUtils.equalsIgnoreCase(v.text, text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aucun enum trouvé correspondant à " + text));
    }
}

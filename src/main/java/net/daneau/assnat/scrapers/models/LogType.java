package net.daneau.assnat.scrapers.models;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@RequiredArgsConstructor
public enum LogType {
    ASSEMBLY("assemblée"),
    COMMITTEE("commission");

    private final String text;

    public static LogType fromName(String text) {
        return Arrays.stream(LogType.values())
                .filter(t -> StringUtils.equalsIgnoreCase(t.text, text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aucun enum trouvé correspondant à " + text));
    }
}

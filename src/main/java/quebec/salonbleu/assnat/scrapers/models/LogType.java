package quebec.salonbleu.assnat.scrapers.models;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Strings;

import java.util.Arrays;

@RequiredArgsConstructor
public enum LogType {
    ASSEMBLY("assemblée"),
    COMMITTEE("commission");

    private final String text;

    public static LogType fromName(String text) {
        return Arrays.stream(LogType.values())
                .filter(t -> Strings.CI.equals(t.text, text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aucun enum trouvé correspondant à " + text));
    }
}

package net.daneau.assnat.scrapers.models;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Value
@Builder
@Jacksonized
public class ScrapedLogEntry {

    LocalDate date;
    String relativeUrl;
    LogType type;
    LogVersion version;
    int legislature;
    int session;
}

package net.daneau.assnat.api.models.subjects;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class Sujet {
    LocalDate date;
    int legislature;
    int session;
    SujetDetails details;
}

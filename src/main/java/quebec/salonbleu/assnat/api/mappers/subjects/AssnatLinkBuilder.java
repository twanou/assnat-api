package quebec.salonbleu.assnat.api.mappers.subjects;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class AssnatLinkBuilder {

    @Value("${api.assnat.baseurl}")
    private final String baseUrl;

    @Value("${api.assnat.log.page.prefix}")
    private final String pagePrefix;

    private static final String URL_FORMAT = "%s%s/%s-%s/journal-debats/%s/%s.html%s";

    public String getUrl(int legislature, int session, LocalDate date, String pageId, String anchor) {
        return String.format(URL_FORMAT,
                baseUrl,
                pagePrefix,
                legislature,
                session,
                date.format(DateTimeFormatter.BASIC_ISO_DATE),
                pageId,
                anchor);
    }
}

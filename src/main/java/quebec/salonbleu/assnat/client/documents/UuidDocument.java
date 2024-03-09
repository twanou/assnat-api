package quebec.salonbleu.assnat.client.documents;

import java.util.UUID;


public interface UuidDocument {

    UuidDocument withId(UUID id);

    UUID getId();
}

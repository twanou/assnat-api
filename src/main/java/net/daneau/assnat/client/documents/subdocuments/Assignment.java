package net.daneau.assnat.client.documents.subdocuments;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Assignment {

    String deputyId;
    String ridingId;
    String partyId;
}

package net.daneau.assnat.client.documents.subdocuments.interventions;

public enum InterventionType {

    DEPUTY_DECLARATION(Constants.DEPUTY_DECLARATION);

    InterventionType(String type) {
        if (!type.equals(this.name())) {
            throw new IllegalArgumentException();
        }
    }

    public static class Constants {
        public static final String DEPUTY_DECLARATION = "DEPUTY_DECLARATION";
    }
}

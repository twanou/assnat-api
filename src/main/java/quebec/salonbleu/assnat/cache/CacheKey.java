package quebec.salonbleu.assnat.cache;

public enum CacheKey {

    CURRENT_ASSIGNMENTS(Constants.CURRENT_ASSIGNMENTS),
    ALL_ASSIGNMENTS(Constants.ALL_ASSIGNMENTS),
    LAST_UPDATE(Constants.LAST_UPDATE),
    NEXT_UPDATE(Constants.NEXT_UPDATE),
    CURRENTLY_LOADING(Constants.CURRENTLY_LOADING),
    DEPUTIES(Constants.DEPUTIES),
    DISTRICTS(Constants.DISTRICTS),
    PARTIES(Constants.PARTIES);


    CacheKey(String cacheKey) {
        if (!cacheKey.equals(this.name()))
            throw new IllegalArgumentException();
    }

    public static class Constants {
        public static final String CURRENT_ASSIGNMENTS = "CURRENT_ASSIGNMENTS";
        public static final String ALL_ASSIGNMENTS = "ALL_ASSIGNMENTS";
        public static final String LAST_UPDATE = "LAST_UPDATE";
        public static final String NEXT_UPDATE = "NEXT_UPDATE";
        public static final String CURRENTLY_LOADING = "CURRENTLY_LOADING";
        public static final String DEPUTIES = "DEPUTIES";
        public static final String DISTRICTS = "DISTRICTS";
        public static final String PARTIES = "PARTIES";
    }
}

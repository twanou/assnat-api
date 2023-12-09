package net.daneau.assnat.cache;

public enum CacheKey {

    CURRENT_ASSIGNMENTS(Constants.CURRENT_ASSIGNMENTS),
    ALL_ASSIGNMENTS(Constants.ALL_ASSIGNMENTS);

    CacheKey(String cacheKey) {
        if (!cacheKey.equals(this.name()))
            throw new IllegalArgumentException();
    }

    public static class Constants {
        public static final String CURRENT_ASSIGNMENTS = "CURRENT_ASSIGNMENTS";
        public static final String ALL_ASSIGNMENTS = "ALL_ASSIGNMENTS";
    }
}
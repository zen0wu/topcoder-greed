package greed.util;

/**
 * Greed is good! Cheers!
 */
public final class ResourcePath {

    private final String relativePath;
    private final boolean internal;

    public ResourcePath(String relativePath, boolean internal) {
        this.relativePath = relativePath;
        this.internal = internal;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public boolean isInternal() {
        return internal;
    }
}

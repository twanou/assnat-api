package quebec.salonbleu.assnat.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhotoDirectoryTest {

    private final PhotoDirectory photoDirectory = new PhotoDirectory();

    @Test
    void exists() {
        assertTrue(this.photoDirectory.exists("photo_introuvable"));
    }

    @Test
    void dontExists() {
        assertFalse(this.photoDirectory.exists("00000"));
    }
}
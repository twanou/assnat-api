package net.daneau.assnat.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhotoUtilsTest {

    @Mock
    private PhotoDirectory photoDirectory;

    private PhotoUtils photoUtils;

    @BeforeEach
    void setup() {
        this.photoUtils = new PhotoUtils("http://url.com", photoDirectory);
    }

    @Test
    void getUniqueId() {
        String fileName = this.photoUtils.getUniqueId("René", "Lévesque", "Taillon", "Parti Québecois");
        assertEquals("59j7uu1cagfhezvxu1jjc1lvj1wy6re2s9ctw15a2p3en865sw", fileName);
    }

    @Test
    void getPhotoUrl() {
        when(photoDirectory.exists("59j7uu1cagfhezvxu1jjc1lvj1wy6re2s9ctw15a2p3en865sw")).thenReturn(true);
        String url = this.photoUtils.getPhotoUrl("René", "Lévesque", "Taillon", "Parti Québecois");
        assertEquals("http://url.com/59j7uu1cagfhezvxu1jjc1lvj1wy6re2s9ctw15a2p3en865sw.jpg", url);
    }

    @Test
    void getPhotoUrlNotFound() {
        when(photoDirectory.exists("59j7uu1cagfhezvxu1jjc1lvj1wy6re2s9ctw15a2p3en865sw")).thenReturn(false);
        String url = this.photoUtils.getPhotoUrl("René", "Lévesque", "Taillon", "Parti Québecois");
        assertEquals("http://url.com/photo_introuvable.jpg", url);
    }
}
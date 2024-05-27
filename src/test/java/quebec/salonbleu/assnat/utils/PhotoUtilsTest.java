package quebec.salonbleu.assnat.utils;

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
        String fileName = this.photoUtils.getUniqueId("René", "Lévesque", "Taillon");
        assertEquals("2bn1vn7qi5t12u0fgf2vio888ko2mido0xpan4xxrgaxybj82f", fileName);
    }

    @Test
    void getPhotoUrl() {
        when(photoDirectory.exists("2bn1vn7qi5t12u0fgf2vio888ko2mido0xpan4xxrgaxybj82f")).thenReturn(true);
        String url = this.photoUtils.getPhotoUrl("René", "Lévesque", "Taillon");
        assertEquals("http://url.com/2bn1vn7qi5t12u0fgf2vio888ko2mido0xpan4xxrgaxybj82f.jpg", url);
    }

    @Test
    void getPhotoUrlNotFound() {
        when(photoDirectory.exists("2bn1vn7qi5t12u0fgf2vio888ko2mido0xpan4xxrgaxybj82f")).thenReturn(false);
        String url = this.photoUtils.getPhotoUrl("René", "Lévesque", "Taillon");
        assertEquals("http://url.com/photo_introuvable.jpg", url);
    }
}

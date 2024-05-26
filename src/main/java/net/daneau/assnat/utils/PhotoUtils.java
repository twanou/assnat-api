package net.daneau.assnat.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class PhotoUtils {

    @Value("${api.photo.url}")
    private final String url;
    private final PhotoDirectory photoDirectory;
    private static final String FILE_NAME_FORMAT = "%s%s%s%s";
    private static final String URL_FORMAT = "%s/%s.jpg";
    private static final String PHOTO_INTROUVABLE = "photo_introuvable";


    public String getUniqueId(String firstName, String lastName, String districtName, String partyName) {
        String hexString = DigestUtils.sha256Hex(FILE_NAME_FORMAT.formatted(firstName, lastName, districtName, partyName));
        return new BigInteger(hexString, 16).toString(36);
    }

    public String getPhotoUrl(String firstName, String lastName, String districtName, String partyName) {
        String id = this.getUniqueId(firstName, lastName, districtName, partyName);
        return URL_FORMAT.formatted(url, this.photoDirectory.exists(id) ? id : PHOTO_INTROUVABLE);
    }
}

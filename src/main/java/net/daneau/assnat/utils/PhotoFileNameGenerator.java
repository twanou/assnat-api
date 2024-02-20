package net.daneau.assnat.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class PhotoFileNameGenerator {

    private static final String FILE_NAME_FORMAT = "%s%s%s%s";

    public String generate(String firstName, String lastName, String districtName, String partyName) {
        String hexString = DigestUtils.sha256Hex(FILE_NAME_FORMAT.formatted(firstName, lastName, districtName, partyName));
        return new BigInteger(hexString, 16).toString(36);
    }
}

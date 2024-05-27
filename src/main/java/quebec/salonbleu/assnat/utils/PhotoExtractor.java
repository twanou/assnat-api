package quebec.salonbleu.assnat.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.repositories.DeputyRepository;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PhotoExtractor {

    private final DeputyRepository deputyRepository;
    private final PhotoUtils photoUtils;
    private static final String PATH = "photos/%s.jpg";

    public void extract() throws IOException {
        List<Deputy> deputies = this.deputyRepository.findAll();
        for (Deputy deputy : deputies) {
            String fileName = this.photoUtils.getUniqueId(deputy.getFirstName(), deputy.getLastName(), deputy.getLastDistrict());
            byte[] decodedBytes = Base64.getDecoder().decode(deputy.getPhoto());
            FileUtils.writeByteArrayToFile(new File(PATH.formatted(fileName)), decodedBytes);
        }
    }
}

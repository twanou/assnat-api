package quebec.salonbleu.assnat.utils;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class PhotoDirectory {

    private final HashSet<String> photos = new HashSet<>();

    public PhotoDirectory() {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        try {
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                    .getResources("classpath:static/photos/*.jpg");
            for (Resource resource : resources) {
                this.photos.add(resource.getFilename().split("\\.")[0]);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Erreur lors de la lecture des photos", e);
        }
    }

    boolean exists(String id) {
        return this.photos.contains(id);
    }
}

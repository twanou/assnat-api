package net.daneau.assnat.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashSet;

@Component
public class PhotoDirectory {

    private final HashSet<String> photos = new HashSet<>();

    public PhotoDirectory() {
        try {
            File fileResource = ResourceUtils.getFile("classpath:static/photos");
            for (File file : fileResource.listFiles()) {
                this.photos.add(file.getName().split("\\.")[0]);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Erreur lors de la lecture des photos");
        }
    }

    boolean exists(String id) {
        return this.photos.contains(id);
    }
}

package net.daneau.assnat.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhotoFileNameGeneratorTest {

    private final PhotoFileNameGenerator photoFileNameGenerator = new PhotoFileNameGenerator();

    @Test
    void generate() {
        String fileName = this.photoFileNameGenerator.generate("René", "Lévesque", "Taillon", "Parti Québecois");
        assertEquals("59j7uu1cagfhezvxu1jjc1lvj1wy6re2s9ctw15a2p3en865sw", fileName);
    }
}
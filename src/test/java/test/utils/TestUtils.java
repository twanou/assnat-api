package test.utils;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestUtils {

    private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder().build();

    public static <T> void assertFileEquals(String resultFilePath, T response, TypeReference<T> typeReference) {
        T expectedResults = OBJECT_MAPPER.readValue(new File(resultFilePath), typeReference);
        assertEquals(expectedResults, response);
    }
}

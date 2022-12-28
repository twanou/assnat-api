package test.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestUtils {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static <T> void assertFileEquals(String resultFilePath, T response, TypeReference<T> typeReference) throws IOException {
        T expectedResults = objectMapper.readValue(new File(resultFilePath), typeReference);
        assertEquals(expectedResults, response);
    }
}

package quebec.salonbleu.assnat.utils;

import quebec.salonbleu.assnat.loaders.exceptions.LoadingException;
import quebec.salonbleu.assnat.scrapers.exceptions.ScrapingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void assertSize() {
        this.errorHandler.assertSize(5, List.of(0, 1, 2, 3, 4), RuntimeException::new);
        Assertions.assertThrows(RuntimeException.class, () -> this.errorHandler.assertSize(6, List.of(0, 1, 2, 3, 4), RuntimeException::new));
    }

    @Test
    void assertNotNull() {
        this.errorHandler.assertNotNull("test", RuntimeException::new);
        Assertions.assertThrows(RuntimeException.class, () -> this.errorHandler.assertNotNull(null, () -> new ScrapingException("message")));
    }

    @Test
    void assertLessThan() {
        this.errorHandler.assertLessThan(5, List.of(0, 1, 2, 3), RuntimeException::new);
        Assertions.assertThrows(RuntimeException.class, () -> this.errorHandler.assertLessThan(5, List.of(0, 1, 2, 3, 4), () -> new LoadingException("message")));
    }
}
package quebec.salonbleu.assnat.scrapers.exceptions;

public class ScrapingException extends RuntimeException {

    public ScrapingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScrapingException(String message) {
        super(message, null);
    }
}

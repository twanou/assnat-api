package quebec.salonbleu.assnat.api.controllers.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import quebec.salonbleu.assnat.api.models.errors.ErreurReponse;

@Slf4j
@RestControllerAdvice
public class ErrorControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurReponse> handleException(Exception exception) {
        log.error("Une exception est survenue : ", exception);
        return ResponseEntity.status(500)
                .body(ErreurReponse.builder()
                        .code("301095")
                        .message("Une erreur est survenue.")
                        .build());
    }

    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public void handleAsyncRequestNotUsableException() {
        // Pour éviter de polluer les logs quand un client abandonne la requête.
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return this.getValidationException();
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return this.getValidationException();
    }

    private ResponseEntity<Object> getValidationException() {
        return ResponseEntity.status(400)
                .body(ErreurReponse.builder()
                        .code("200580")
                        .message("Erreur de validation.")
                        .build());
    }
}

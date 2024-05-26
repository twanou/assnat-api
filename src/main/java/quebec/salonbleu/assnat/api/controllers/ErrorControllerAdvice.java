package quebec.salonbleu.assnat.api.controllers;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import quebec.salonbleu.assnat.api.models.errors.ErreurReponse;

@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErreurReponse> handleConstraintViolationException() {
        return ResponseEntity.status(400)
                .body(ErreurReponse.builder()
                        .code("200580")
                        .message("Erreur de validation.")
                        .build());
    }

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
}

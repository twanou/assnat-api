package quebec.salonbleu.assnat.api.controllers;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import quebec.salonbleu.assnat.api.models.errors.ErreurReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErreurReponse> handleNoResourceFoundException() {
        return ResponseEntity.status(404)
                .body(ErreurReponse.builder()
                        .code("151176")
                        .message("Ressource introuvable.")
                        .build());
    }

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
}

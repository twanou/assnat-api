package net.daneau.assnat.api.controllers;

import jakarta.validation.ConstraintViolationException;
import net.daneau.assnat.api.models.errors.ErreurReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurReponse> gottaCatchEmAll() {
        return ResponseEntity.status(500)
                .body(ErreurReponse.builder()
                        .code("301095")
                        .message("Une erreur est survenue.")
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
}
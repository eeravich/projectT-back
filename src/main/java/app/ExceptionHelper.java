package app;

import app.entities.pojos.ErrorPojo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHelper {
    @ExceptionHandler(InvalidDataException.class)
    public final ResponseEntity<?> handleException(InvalidDataException ex) {
        ErrorPojo errorPojo = new ErrorPojo(ex.getMessage(), ex.getErrors(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorPojo);
    }
}

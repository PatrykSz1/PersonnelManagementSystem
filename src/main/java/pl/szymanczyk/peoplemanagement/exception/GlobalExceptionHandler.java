package pl.szymanczyk.peoplemanagement.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
//        String errorMessage = "Data integrity violation occurred.";
//
//        if (ex.getMessage().contains("ConstraintViolationException")) {
//            errorMessage = "Other constraint violation occurred.";
//        }
//        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> handleMethodNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        ex.getFieldErrors().forEach(fieldError -> {
            String errorMessage = fieldError.getField() + " " + fieldError.getDefaultMessage();
            errorMessages.add("Validation error: " + errorMessage);
        });
        return errorMessages;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ExceptionDto(ex.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleEntityNotFoundException(EntityExistsException ex) {
        return new ExceptionDto(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleIllegalStateException(IllegalStateException ex) {
        return new ExceptionDto(ex.getMessage());
    }
}
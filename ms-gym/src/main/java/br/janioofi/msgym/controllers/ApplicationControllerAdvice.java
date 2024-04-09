package br.janioofi.msgym.controllers;

import br.janioofi.msgym.exceptions.RecordNotFoundException;
import br.janioofi.msgym.exceptions.ValidationErrors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(RecordNotFoundException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrors validationErrors(ConstraintViolationException ex, HttpServletRequest request){
        ValidationErrors field = new ValidationErrors();
        field.setPath(request.getRequestURI());
        field.setStatus(HttpStatus.BAD_REQUEST.value());
        field.setError("Validation Error");
        for(var x : ex.getConstraintViolations()){
            field.addErrors(x.getPropertyPath().toString(), x.getMessage());
        }
        return field;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrors validationErrors(DataIntegrityViolationException ex, HttpServletRequest request){
        ValidationErrors field = new ValidationErrors();
        field.setPath(request.getRequestURI());
        field.setStatus(HttpStatus.BAD_REQUEST.value());
        field.setError("Validation Error");
        field.addErrors("Data Integrity Violation", ex.getMessage());
        return field;
    }
}

package be.thebeehive.tdd.todoapp.api.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import be.thebeehive.tdd.todoapp.api.dto.ErrorMessage;
import be.thebeehive.tdd.todoapp.api.exception.NotFoundException;

@RestControllerAdvice
public class TodoAppRestAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFound(NotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ErrorMessage("Required requestbody is missing");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return new ErrorMessage(ex.getFieldError().getDefaultMessage());
    }
}

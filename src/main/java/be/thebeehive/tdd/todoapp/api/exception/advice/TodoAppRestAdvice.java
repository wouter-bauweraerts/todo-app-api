package be.thebeehive.tdd.todoapp.api.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import be.thebeehive.tdd.todoapp.api.dto.ErrorMessage;
import be.thebeehive.tdd.todoapp.api.exception.NotFoundException;
import be.thebeehive.tdd.todoapp.api.exception.TodoAlreadyCompletedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class TodoAppRestAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFound(NotFoundException ex) {
        log.warn("Todo not found!", ex);
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Body not readable", ex);
        return new ErrorMessage("Required requestbody is missing");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("Invalid body", ex);
        return new ErrorMessage(ex.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleTodoAlreadyCompleted(TodoAlreadyCompletedException ex) {
        log.warn("Todo already completed", ex);
        return new ErrorMessage(ex.getMessage());
    }
}

package be.thebeehive.tdd.todoapp.api.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.thebeehive.tdd.todoapp.api.dto.CreateTodoDto;
import be.thebeehive.tdd.todoapp.api.dto.TodoDto;
import be.thebeehive.tdd.todoapp.api.dto.UpdateTodoDto;
import be.thebeehive.tdd.todoapp.service.TodoService;

@RestController
@RequestMapping("/todo")
public class TodoController {
    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TodoDto> findAll() {
        return todoService.findAll();
    }

    @GetMapping(value = "/incomplete", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TodoDto> findAllIncomplete() {
        return todoService.findAllIncomplete();
    }


    @GetMapping(value = "/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoDto findById(@PathVariable int todoId) {
        return todoService.findById(todoId);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoDto createTodo(@Valid @RequestBody @NotNull(message = "Requestbody is missing") CreateTodoDto createTodoDto) {
        return todoService.create(createTodoDto);
    }

    @PutMapping(value = "/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoDto updateTodo(@PathVariable int todoId, @Valid @RequestBody @NotNull(message = "Requestbody is missing") UpdateTodoDto updateTodoDto) {
        return todoService.update(todoId, updateTodoDto);
    }

    @PutMapping(value = "/complete/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoDto updateTodo(@PathVariable int todoId) {
        return todoService.complete(todoId);
    }
}

package be.thebeehive.tdd.todoapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.thebeehive.tdd.todoapp.api.dto.TodoDto;
import be.thebeehive.tdd.todoapp.api.exception.NotFoundException;
import be.thebeehive.tdd.todoapp.repository.TodoRepository;
import be.thebeehive.tdd.todoapp.service.mapper.TodoMapper;

@Service
public class TodoService {
    private TodoRepository todoRepository;
    private TodoMapper mapper;

    public TodoService(TodoRepository todoRepository, TodoMapper mapper) {
        this.todoRepository = todoRepository;
        this.mapper = mapper;
    }

    public List<TodoDto> findAll() {
        return mapper.toTodoDto(
                todoRepository.findAll()
        );
    }

    public TodoDto findById(int todoId) {
        return todoRepository.findById(todoId)
                .map(mapper::toDto)
                .orElseThrow(() -> NotFoundException.withId(todoId));
    }
}

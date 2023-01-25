package be.thebeehive.tdd.todoapp.service;

import static java.util.Collections.emptyList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import be.thebeehive.tdd.todoapp.api.dto.CreateTodoDto;
import be.thebeehive.tdd.todoapp.api.dto.TodoDto;
import be.thebeehive.tdd.todoapp.api.dto.UpdateTodoDto;
import be.thebeehive.tdd.todoapp.api.exception.NotFoundException;
import be.thebeehive.tdd.todoapp.api.exception.TodoAlreadyCompletedException;
import be.thebeehive.tdd.todoapp.api.exception.TodoNotCreatedException;
import be.thebeehive.tdd.todoapp.model.TodoEntity;
import be.thebeehive.tdd.todoapp.repository.TodoRepository;
import be.thebeehive.tdd.todoapp.service.mapper.TodoMapper;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final TodoMapper mapper;

    public TodoService(TodoRepository todoRepository, TodoMapper mapper) {
        this.todoRepository = todoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public List<TodoDto> findAll() {
        return mapper.toTodoDto(
                todoRepository.findAll()
        );
    }

    @Transactional
    public TodoDto findById(int todoId) {
        return todoRepository.findById(todoId)
                .map(mapper::toDto)
                .orElseThrow(() -> NotFoundException.withId(todoId));
    }

    @Transactional
    public TodoDto create(CreateTodoDto createTodoDto) {
        return Optional.ofNullable(mapper.toEntity(createTodoDto))
                .map(todoRepository::save)
                .map(mapper::toDto)
                .orElseThrow(() -> TodoNotCreatedException.withMessage("Can not create empty todo"));
    }

    @Transactional
    public TodoDto update(int todoId, UpdateTodoDto updateTodoDto) {
        return todoRepository.findById(todoId)
                .map(todo -> {
                    mapper.update(todo, updateTodoDto);
                    return todoRepository.save(todo);
                })
                .map(mapper::toDto)
                .orElseThrow(() -> NotFoundException.withId(todoId));
    }

    public List<TodoDto> findAllIncomplete() {
        return Optional.ofNullable(todoRepository.findAllByCompleteIsFalse())
                .map(mapper::toTodoDto)
                .orElse(emptyList());
    }

    @Transactional
    public TodoDto complete(int todoId) {
        return todoRepository.findById(todoId)
                .map(todo -> mapper.toDto(complete(todo)))
                .orElseThrow(() -> NotFoundException.withId(todoId));
    }

    private TodoEntity complete(TodoEntity entity) {
        if (entity.isComplete()) {
            throw TodoAlreadyCompletedException.withId(entity.getTodoId());
        }

        entity.setComplete(true);
        return entity;
    }
}

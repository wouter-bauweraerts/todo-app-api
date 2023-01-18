package be.thebeehive.tdd.todoapp.api.dto;

import be.thebeehive.tdd.todoapp.model.TodoEntity;

public class TodoDtoFixtures {
    public static TodoDto ofEntity(TodoEntity entity) {
        return TodoDto.builder()
                .todoId(entity.getTodoId())
                .build();
    }
}

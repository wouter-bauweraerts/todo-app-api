package be.thebeehive.tdd.todoapp.model;

import com.github.javafaker.Faker;

public class TodoEntityFixtures {
    public static final Faker FAKER = Faker.instance();

    public static TodoEntity todoEntity() {
        return TodoEntity.builder()
                .todoId(Math.toIntExact(FAKER.number().randomNumber(4, false)))
                .build();
    }

    public static TodoEntity withTodoId(Integer todoId) {
        return TodoEntity.builder()
                .todoId(todoId)
                .build();
    }

    public static TodoEntity unpersistedEntityWithDescription(String description) {
        return TodoEntity.builder()
                .description(description)
                .build();
    }

    public static TodoEntity todoEntityWithDescription(String description) {
        return TodoEntity.builder()
                .todoId(Math.toIntExact(FAKER.number().randomNumber(4, false)))
                .description(description)
                .build();
    }
}
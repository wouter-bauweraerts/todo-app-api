package be.thebeehive.tdd.todoapp.model;

import com.github.javafaker.Faker;

public class TodoEntityFixtures {
    public static final Faker FAKER = Faker.instance();

    public static TodoEntity todoEntity() {
        return TodoEntity.builder()
                .todoId(Math.toIntExact(FAKER.number().randomNumber(4, false)))
                .description(truncateIfRequired(FAKER.yoda().quote()))
                .build();
    }

    public static TodoEntity withTodoId(Integer todoId) {
        return TodoEntity.builder()
                .todoId(todoId)
                .build();
    }

    public static TodoEntity unpersistedEntityWithDescription(String description) {
        return TodoEntity.builder()
                .description(truncateIfRequired(description))
                .build();
    }

    public static TodoEntity todoEntityWithDescription(String description) {
        return TodoEntity.builder()
                .todoId(Math.toIntExact(FAKER.number().randomNumber(4, false)))
                .description(truncateIfRequired(description))
                .build();
    }

    public static TodoEntity copy(TodoEntity original, String description) {
        return original.toBuilder()
                .description(truncateIfRequired(description))
                .build();
    }

    public static TodoEntity copy(TodoEntity original) {
        return original.toBuilder()
                .build();
    }

    private static String truncateIfRequired(String input) {
        return input.length() > 255
                ? input.substring(0, 250)
                : input;
    }
}
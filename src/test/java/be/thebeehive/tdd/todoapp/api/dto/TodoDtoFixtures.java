package be.thebeehive.tdd.todoapp.api.dto;

import java.util.Objects;

import com.github.javafaker.Faker;

import be.thebeehive.tdd.todoapp.model.TodoEntity;

public class TodoDtoFixtures {
    private static final Faker FAKER = Faker.instance();
    public static TodoDto fromEntity(TodoEntity entity) {
        return TodoDto.builder()
                .todoId(Objects.isNull(entity.getTodoId()) ? -1 : entity.getTodoId())
                .description(entity.getDescription())
                .build();
    }

    public static TodoDto todoDtoFixture() {
        return TodoDto.builder()
                .todoId(FAKER.number().numberBetween(0, 100))
                .description(truncateIfRequired(FAKER.yoda().quote()))
                .build();
    }

    private static String truncateIfRequired(String input) {
        return input.length() > 255
                ? input.substring(0, 250)
                : input;
    }
}

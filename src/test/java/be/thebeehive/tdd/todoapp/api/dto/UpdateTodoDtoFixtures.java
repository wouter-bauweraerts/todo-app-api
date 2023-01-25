package be.thebeehive.tdd.todoapp.api.dto;

import com.github.javafaker.Faker;

public class UpdateTodoDtoFixtures {
    public static final Faker FAKER = Faker.instance();
    public static UpdateTodoDto updateTodoFixture() {
        return UpdateTodoDto.builder()
                .description(truncateIfRequired(FAKER.yoda().quote()))
                .build();
    }

    private static String truncateIfRequired(String input) {
        return input.length() > 255
                ? input.substring(0, 250)
                : input;
    }
}

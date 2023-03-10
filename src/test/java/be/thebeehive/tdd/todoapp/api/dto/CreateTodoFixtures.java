package be.thebeehive.tdd.todoapp.api.dto;

import com.github.javafaker.Faker;

public class CreateTodoFixtures {
    private static final Faker FAKER = Faker.instance();
    public static CreateTodoDto createTodoDto() {
        return new CreateTodoDto(truncateIfRequired(FAKER.yoda().quote()));
    }

    public static CreateTodoDto createTodoDto(String description) {
        return new CreateTodoDto(description);
    }

    private static String truncateIfRequired(String input) {
        return input.length() > 255
                ? input.substring(0, 250)
                : input;
    }
}

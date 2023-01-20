package be.thebeehive.tdd.todoapp.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public record CreateTodoDto(
        @NotNull(message = "description is required")
        @NotBlank(message = "description is required")
        @Length(max = 255, message = "description is too long. Max length: 255")
        String description
) {
}

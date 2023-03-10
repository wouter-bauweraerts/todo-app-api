package be.thebeehive.tdd.todoapp.api.dto;

import lombok.Builder;

public record TodoDto(int todoId, String description, boolean complete) {
    @Builder
    public TodoDto {}
}

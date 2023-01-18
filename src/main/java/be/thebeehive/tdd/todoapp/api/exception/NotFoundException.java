package be.thebeehive.tdd.todoapp.api.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException withId(int todoId) {
        return new NotFoundException("Todo with id %s not found".formatted(todoId));
    }
}

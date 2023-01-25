package be.thebeehive.tdd.todoapp.api.exception;

public class TodoAlreadyCompletedException extends RuntimeException {
    public TodoAlreadyCompletedException(String message) {
        super(message);
    }

    public static TodoAlreadyCompletedException withId(int todoId) {
        return new TodoAlreadyCompletedException("Todo %d is already completed".formatted(todoId));
    }
}

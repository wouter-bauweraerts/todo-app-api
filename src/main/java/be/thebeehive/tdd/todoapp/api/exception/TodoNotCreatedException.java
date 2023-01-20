package be.thebeehive.tdd.todoapp.api.exception;

public class TodoNotCreatedException extends RuntimeException {
    public TodoNotCreatedException(String message) {
        super(message);
    }

    public static TodoNotCreatedException withMessage(String message) {
        return new TodoNotCreatedException(message);
    }
}

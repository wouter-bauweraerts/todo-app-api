package be.thebeehive.tdd.todoapp.api.dto;

public record TodoDto(int todoId) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int todoId;

        public Builder todoId(int todoId) {
            this.todoId = todoId;
            return this;
        }

        public TodoDto build() {
            return new TodoDto(todoId);
        }
    }
}

package be.thebeehive.tdd.todoapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "todos")
public class TodoEntity {
    @Id
    @GeneratedValue
    private int todoId;

    public TodoEntity() {
    }

    public TodoEntity(int todoId) {
        this.todoId = todoId;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }
}

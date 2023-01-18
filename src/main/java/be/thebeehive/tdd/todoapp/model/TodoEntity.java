package be.thebeehive.tdd.todoapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TODOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodoEntity {
    @Id
    @GeneratedValue
    @Column(name = "TODO_ID", unique = true, nullable = false, updatable = false)
    private int todoId;
}

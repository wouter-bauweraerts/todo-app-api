package be.thebeehive.tdd.todoapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.github.javafaker.Weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TODOS", indexes = @Index(columnList = "completed", name = "idx_todos_complete"))
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TodoEntity {
    @Id
    @SequenceGenerator(name = "todo_id_seq", sequenceName = "todo_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_id_seq")
    @Column(name = "TODO_ID", unique = true, nullable = false, updatable = false)
    private Integer todoId;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @Builder.Default
    @Column(name = "COMPLETED", nullable = false)
    private boolean complete = false;
}

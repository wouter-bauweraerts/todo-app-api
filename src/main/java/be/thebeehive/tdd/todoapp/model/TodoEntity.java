package be.thebeehive.tdd.todoapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.javafaker.Weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TODOS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TodoEntity {
    @Id
    @GeneratedValue
    @Column(name = "TODO_ID", unique = true, nullable = false, updatable = false)
    private Integer todoId;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

}

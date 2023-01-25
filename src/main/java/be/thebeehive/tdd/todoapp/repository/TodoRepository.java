package be.thebeehive.tdd.todoapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.thebeehive.tdd.todoapp.model.TodoEntity;

@Repository
public interface TodoRepository extends CrudRepository<TodoEntity, Integer> {
    List<TodoEntity> findAllByCompleteIsFalse();
}

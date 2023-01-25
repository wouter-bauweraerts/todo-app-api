package be.thebeehive.tdd.todoapp.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.thebeehive.tdd.todoapp.model.TodoEntity;
import be.thebeehive.tdd.todoapp.repository.TodoRepository;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "todos.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "cleanup.sql")
class CompleteTodoIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TodoRepository todoRepository;

    @Test
    void completeTodoCompletesTodoAsExpectedAndReturnsExpectedDto() throws Exception {

        completeTodo()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.todoId").value(1))
                .andExpect(jsonPath("$.description").value("Do the dishes"))
                .andExpect(jsonPath("$.complete").value(true));

        Optional<TodoEntity> optionalTodo = todoRepository.findById(1);
        assertThat(optionalTodo).isPresent()
                .hasValue(TodoEntity.builder()
                        .todoId(1)
                        .complete(true)
                        .description("Do the dishes")
                        .build()
                );

    }

    @Test
    void completeTodoTwiceThrowsExpected() throws Exception {

        completeTodo();
        completeTodo()
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.msg").value("Todo 1 is already completed"));

    }

    private ResultActions completeTodo() throws Exception {
        return mockMvc.perform(put("/todo/complete/1"));
    }

}

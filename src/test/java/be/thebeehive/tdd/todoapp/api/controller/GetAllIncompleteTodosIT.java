package be.thebeehive.tdd.todoapp.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import be.thebeehive.tdd.todoapp.api.dto.TodoDto;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "todos.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "cleanup.sql")
class GetAllIncompleteTodosIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllCompleteTodosReturnsExpected() throws Exception {
        var expected = List.of(
                TodoDto.builder()
                        .todoId(1)
                        .description("Do the dishes")
                        .complete(false)
                        .build(),
                TodoDto.builder()
                        .todoId(2)
                        .description("Empty the dishwasher")
                        .complete(false)
                        .build(),
                TodoDto.builder()
                        .todoId(5)
                        .description("Chill")
                        .complete(false)
                        .build()
        );

        var jsonResponse = mockMvc.perform(get("/todo/incomplete"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var result = objectMapper.readValue(jsonResponse, new TypeReference<List<TodoDto>>() {});

        assertThat(result).hasSize(expected.size())
                .containsExactlyInAnyOrderElementsOf(expected);
    }
}

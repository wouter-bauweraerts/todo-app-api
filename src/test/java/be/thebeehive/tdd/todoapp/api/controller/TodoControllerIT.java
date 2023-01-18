package be.thebeehive.tdd.todoapp.api.controller;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import be.thebeehive.tdd.todoapp.api.dto.TodoDto;
import be.thebeehive.tdd.todoapp.model.TodoEntity;
import be.thebeehive.tdd.todoapp.model.TodoEntityFixtures;
import be.thebeehive.tdd.todoapp.repository.TodoRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerIT {
    private static final Faker FAKER = Faker.instance();

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext ctx;
    @MockBean
    TodoRepository todoRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @ParameterizedTest
    @MethodSource("getTodosSource")
    void getTodosReturnsExpectedResponse(List<TodoEntity> entities, List<TodoDto> expectedResponse) throws Exception {
        when(todoRepository.findAll()).thenReturn(entities);

        mockMvc.perform(
                        get("/todo").contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse), true));
    }

    @Test
    void getTodoByIdReturns404WithExpectedMessageWhenTodoNotFound() throws Exception {
        var todoId = FAKER.number().randomDigit();

        when(todoRepository.findById(todoId)).thenReturn(empty());

        mockMvc.perform(
                get("/todo/%s".formatted(todoId))
        ).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.msg").value("Todo with id %s not found".formatted(todoId)));
    }

    @Test
    void getTodoByIdReturnsExpectedResult() throws Exception {
        var entity = TodoEntityFixtures.todoEntity();
        var dto = TodoDto.builder()
                        .todoId(entity.getTodoId())
                                .build();

        when(todoRepository.findById(entity.getTodoId())).thenReturn(of(entity));

        mockMvc.perform(
                        get("/todo/%s".formatted(entity.getTodoId()))
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dto), true));
    }

    private static Stream<Arguments> getTodosSource() {
        return Stream.of(
                Arguments.of(emptyList(), emptyList()),
                Arguments.of(
                        singletonList(new TodoEntity(1)),
                        singletonList(new TodoDto(1))
                ),
                Arguments.of(
                        List.of(new TodoEntity(1), new TodoEntity(2)),
                        List.of(new TodoDto(1), new TodoDto(2))
                )
        );
    }
}

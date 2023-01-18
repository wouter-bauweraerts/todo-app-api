package be.thebeehive.tdd.todoapp.api.controller;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
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

import be.thebeehive.tdd.todoapp.api.dto.TodoDto;
import be.thebeehive.tdd.todoapp.model.TodoEntity;
import be.thebeehive.tdd.todoapp.repository.TodoRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerIT {
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
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse), false));
    }

    private static Stream<Arguments> getTodosSource() {
        return Stream.of(
                Arguments.of(emptyList(), emptyList()),
                Arguments.of(
                        singletonList(new TodoEntity(1)),
                        singletonList(new TodoDto(1))
                ),
                Arguments.of(
                        of(new TodoEntity(1), new TodoEntity(2)),
                        of(new TodoDto(1), new TodoDto(2))
                )
        );
    }
}

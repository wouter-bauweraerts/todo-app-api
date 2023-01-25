package be.thebeehive.tdd.todoapp.api.controller;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import be.thebeehive.tdd.todoapp.api.dto.CreateTodoDto;
import be.thebeehive.tdd.todoapp.api.dto.CreateTodoFixtures;
import be.thebeehive.tdd.todoapp.api.dto.TodoDto;
import be.thebeehive.tdd.todoapp.api.dto.TodoDtoFixtures;
import be.thebeehive.tdd.todoapp.api.dto.UpdateTodoDto;
import be.thebeehive.tdd.todoapp.api.dto.UpdateTodoDtoFixtures;
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
        var dto = TodoDtoFixtures.fromEntity(entity);

        when(todoRepository.findById(entity.getTodoId())).thenReturn(of(entity));

        mockMvc.perform(
                        get("/todo/%s".formatted(entity.getTodoId()))
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dto), true));
    }

    @ParameterizedTest
    @MethodSource("createWithInvalidBodySource")
    void createTodoWithEmptyBodyReturnsBadRequestWithExpectedMessage(CreateTodoDto dto, String msg) throws Exception {
        mockMvc.perform(buildCreateTodoRequest(dto))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value(msg));

        verifyNoInteractions(todoRepository);
    }

    @Test
    void createTodoSuccessReturnsExpectedDto() throws Exception {
        var createDto = CreateTodoFixtures.createTodoDto();
        var entityToSave = TodoEntityFixtures.unpersistedEntityWithDescription(createDto.description());
        var savedEntity = TodoEntityFixtures.todoEntityWithDescription(createDto.description());
        var todoDto = TodoDtoFixtures.fromEntity(savedEntity);

        when(todoRepository.save(entityToSave)).thenReturn(savedEntity);

        mockMvc.perform(buildCreateTodoRequest(createDto))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(todoDto), true));

        verify(todoRepository).save(entityToSave);
    }

    @ParameterizedTest
    @MethodSource("updateWithInvalidBodySource")
    void updateTodoWithEmptyBodyReturnsBadRequestWithExpectedMessage(UpdateTodoDto dto, String msg) throws Exception {
        var entity = TodoEntityFixtures.todoEntity();

        when(todoRepository.findById(entity.getTodoId())).thenReturn(Optional.of(entity));

        mockMvc.perform(buildUpdateTodoRequest(entity.getTodoId(), dto))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value(msg));

        verifyNoInteractions(todoRepository);
    }

    @Test
    void updateTodoWithTodoNotFoundReturnsExpected() throws  Exception {
        int todoId = FAKER.number().numberBetween(0, 100);
        when(todoRepository.findById(anyInt())).thenReturn(empty());

        mockMvc.perform(buildUpdateTodoRequest(todoId, UpdateTodoDtoFixtures.updateTodoFixture()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Todo with id %d not found".formatted(todoId)));
    }

    @Test
    void updateTodoSuccess() throws Exception {
        var original = TodoEntityFixtures.todoEntity();
        var updateDto = UpdateTodoDtoFixtures.updateTodoFixture();
        var updatedEntity = TodoEntityFixtures.copy(original, updateDto.description());

        when(todoRepository.findById(anyInt())).thenReturn(Optional.of(TodoEntityFixtures.copy(original)));
        when(todoRepository.save(any())).thenReturn(updatedEntity);

        mockMvc.perform(buildUpdateTodoRequest(original.getTodoId(), updateDto))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedEntity), true));

        verify(todoRepository).findById(original.getTodoId());
        verify(todoRepository).save(updatedEntity);
    }

    @Test
    void getIncompleteTodosReturnsExpectedWhenNothingFound() throws Exception {
        List<TodoDto> dtos = emptyList();
        when(todoRepository.findAllByCompleteIsFalse()).thenReturn(emptyList());

        mockMvc.perform(get("/todo/incomplete"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dtos), true));
    }

    private MockHttpServletRequestBuilder buildCreateTodoRequest(CreateTodoDto dto) throws JsonProcessingException {
        MockHttpServletRequestBuilder requestBuilder = post("/todo")
                .contentType(MediaType.APPLICATION_JSON);

        return Objects.isNull(dto)
                ? requestBuilder
                : requestBuilder.content(objectMapper.writeValueAsString(dto));
    }

    private MockHttpServletRequestBuilder buildUpdateTodoRequest(int todoId, UpdateTodoDto dto) throws JsonProcessingException {
        MockHttpServletRequestBuilder requestBuilder = put("/todo/%d".formatted(todoId))
                .contentType(MediaType.APPLICATION_JSON);

        return Objects.isNull(dto)
                ? requestBuilder
                : requestBuilder.content(objectMapper.writeValueAsString(dto));
    }


    private static Stream<Arguments> getTodosSource() {
        return Stream.of(
                Arguments.of(emptyList(), emptyList()),
                getTodosArguments(1),
                getTodosArguments(2),
                getTodosArguments(25)
        );
    }

    private static Stream<Arguments> createWithInvalidBodySource() {
        return Stream.of(
                Arguments.of(null, "Required requestbody is missing"),
                Arguments.of(new CreateTodoDto(""), "description is required"),
                Arguments.of(new CreateTodoDto(FAKER.lorem().characters(255, 600)), "description is too long. Max length: 255"),
                Arguments.of(new CreateTodoDto(" "), "description is required"),
                Arguments.of(new CreateTodoDto(null), "description is required")
        );
    }

    private static Stream<Arguments> updateWithInvalidBodySource() {
        return Stream.of(
                Arguments.of(null, "Required requestbody is missing"),
                Arguments.of(new UpdateTodoDto(""), "description is required"),
                Arguments.of(new UpdateTodoDto(FAKER.lorem().characters(255, 600)), "description is too long. Max length: 255"),
                Arguments.of(new UpdateTodoDto(" "), "description is required"),
                Arguments.of(new UpdateTodoDto(null), "description is required")
        );
    }

    private static Arguments getTodosArguments(int size) {
        List<TodoEntity> entities = new ArrayList<>();
        List<TodoDto> dtos = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            TodoEntity entity = TodoEntityFixtures.todoEntity();
            entities.add(entity);
            dtos.add(TodoDtoFixtures.fromEntity(entity));
        }
        return Arguments.of(entities, dtos);
    }
}

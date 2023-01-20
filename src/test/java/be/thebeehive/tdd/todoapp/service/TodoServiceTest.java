package be.thebeehive.tdd.todoapp.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

import be.thebeehive.tdd.todoapp.api.dto.CreateTodoDto;
import be.thebeehive.tdd.todoapp.api.dto.CreateTodoFixtures;
import be.thebeehive.tdd.todoapp.api.dto.TodoDto;
import be.thebeehive.tdd.todoapp.api.dto.TodoDtoFixtures;
import be.thebeehive.tdd.todoapp.api.exception.NotFoundException;
import be.thebeehive.tdd.todoapp.api.exception.TodoNotCreatedException;
import be.thebeehive.tdd.todoapp.model.TodoEntity;
import be.thebeehive.tdd.todoapp.model.TodoEntityFixtures;
import be.thebeehive.tdd.todoapp.repository.TodoRepository;
import be.thebeehive.tdd.todoapp.service.mapper.TodoMapper;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    public static final Faker FAKER = Faker.instance();

    @InjectMocks
    TodoService service;
    @Mock
    TodoRepository repository;
    @Mock
    TodoMapper mapper;

    @ParameterizedTest
    @MethodSource("findAllSource")
    void findAllReturnsExpectedDtosWhenTodosPresent(List<TodoEntity> entities, List<TodoDto> dtos) {
        when(repository.findAll()).thenReturn(entities);
        when(mapper.toTodoDto(entities)).thenReturn(dtos);

        assertThat(service.findAll()).isEqualTo(dtos);

        verify(repository).findAll();
        verify(mapper).toTodoDto(entities);
    }

    private static Stream<Arguments> findAllSource() {
        return Stream.of(
                Arguments.of(emptyList(), emptyList()),
                Arguments.of(singletonList(TodoEntityFixtures.withTodoId(1)), singletonList(TodoDtoFixtures.todoDtoFixture())),
                Arguments.of(of(TodoEntityFixtures.withTodoId(1), TodoEntityFixtures.withTodoId(2)), of(TodoDtoFixtures.todoDtoFixture(), TodoDtoFixtures.todoDtoFixture()))
        );
    }

    @Test
    void findByIdThrowsExpectedExceptionWhenTodoEntityNotFound() {
        var todoId = FAKER.number().randomDigit();

        assertThatThrownBy(() -> service.findById(todoId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Todo with id %s not found".formatted(todoId));
    }

    @Test
    void findByIdReturnsExpectedDtoWhenTodoEntityIsPresent() {
        var entity = TodoEntityFixtures.todoEntity();
        var dto = TodoDtoFixtures.fromEntity(entity);

        when(repository.findById(entity.getTodoId())).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        assertThat(service.findById(entity.getTodoId())).isSameAs(dto);

        verify(repository).findById(entity.getTodoId());
        verify(mapper).toDto(entity);
    }

    @Test
    void createCallsExpectedMethods() {
        var createDto = CreateTodoFixtures.createTodoDto();
        var entityToCreate = mock(TodoEntity.class);
        var persistedEntity = mock(TodoEntity.class);
        var todoDto = TodoDtoFixtures.todoDtoFixture();

        when(mapper.toEntity(createDto)).thenReturn(entityToCreate);
        when(repository.save(entityToCreate)).thenReturn(persistedEntity);
        when(mapper.toDto(persistedEntity)).thenReturn(todoDto);

        assertThat(service.create(createDto)).isSameAs(todoDto);

        verify(mapper).toEntity(createDto);
        verify(repository).save(entityToCreate);
        verify(mapper).toDto(persistedEntity);
    }

    @Test
    void createCallsExpectedAndThrowsExceptionWhenCreateDtoIsNull() {
        when(mapper.toEntity(null)).thenReturn(null);

        assertThatThrownBy(() -> service.create(null))
                .isInstanceOf(TodoNotCreatedException.class)
                .hasMessage("Can not create empty todo");

        verifyNoMoreInteractions(mapper);
        verifyNoInteractions(repository);
    }
}
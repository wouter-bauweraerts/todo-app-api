package be.thebeehive.tdd.todoapp.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import be.thebeehive.tdd.todoapp.api.dto.TodoDto;
import be.thebeehive.tdd.todoapp.model.TodoEntity;
import be.thebeehive.tdd.todoapp.repository.TodoRepository;
import be.thebeehive.tdd.todoapp.service.mapper.TodoMapper;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

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
                Arguments.of(singletonList(new TodoEntity(1)), singletonList(new TodoDto(1))),
                Arguments.of(of(new TodoEntity(1), new TodoEntity(2)), of(new TodoDto(1), new TodoDto(2)))
        );
    }
}
package be.thebeehive.tdd.todoapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;

import com.github.javafaker.Faker;

import be.thebeehive.tdd.todoapp.api.dto.CreateTodoDto;
import be.thebeehive.tdd.todoapp.api.dto.CreateTodoFixtures;
import be.thebeehive.tdd.todoapp.api.dto.TodoDto;
import be.thebeehive.tdd.todoapp.api.dto.TodoDtoFixtures;
import be.thebeehive.tdd.todoapp.api.dto.UpdateTodoDto;
import be.thebeehive.tdd.todoapp.model.TodoEntity;
import be.thebeehive.tdd.todoapp.model.TodoEntityFixtures;

class TodoMapperTest {
    private static final Faker FAKER = Faker.instance();
    TodoMapper mapper = Mappers.getMapper(TodoMapper.class);

    @ParameterizedTest
    @MethodSource("toDtoSrc")
    void toDtoMapsToExpectedTodoDto(TodoEntity entity, TodoDto dto) {
        assertThat(mapper.toDto(entity)).isEqualTo(dto);
    }

    private static Stream<Arguments> toDtoSrc() {
        return Stream.of(
                Arguments.of(null, null),
                getToDtoArguments(TodoEntityFixtures.withTodoId(null)),
                getToDtoArguments(TodoEntityFixtures.todoEntity())
        );
    }

    private static Arguments getToDtoArguments(TodoEntity entity) {
        return Arguments.of(entity, TodoDtoFixtures.fromEntity(entity));
    }

    @ParameterizedTest
    @MethodSource("toEntityFromCreateTodoSrc")
    void toEntityReturnsExpected(CreateTodoDto dto, TodoEntity expected) {
        assertThat(mapper.toEntity(dto)).isEqualTo(expected);
    }

    private static Stream<Arguments> toEntityFromCreateTodoSrc() {
        String description = FAKER.yoda().quote();

        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(CreateTodoFixtures.createTodoDto(description), TodoEntity.builder().description(description).build())
        );
    }

    @Test
    void updateCallsNothingWhenDtoIsNull() {
        var entity = mock(TodoEntity.class);

        mapper.update(entity, null);

        verifyNoInteractions(entity);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "some string"})
    void updateUpdatesDescriptionOnly(String description) {
        var entity = mock(TodoEntity.class);
        var dto = UpdateTodoDto.builder()
                .description(description)
                .build();

        mapper.update(entity, dto);

        verify(entity).setDescription(Objects.isNull(dto.description()) ? "" : description );
        verifyNoMoreInteractions(entity);
    }

}
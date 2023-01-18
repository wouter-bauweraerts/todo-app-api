package be.thebeehive.tdd.todoapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import be.thebeehive.tdd.todoapp.api.dto.TodoDto;
import be.thebeehive.tdd.todoapp.model.TodoEntityFixtures;

class TodoMapperTest {
    TodoMapper mapper = Mappers.getMapper(TodoMapper.class);

    @Test
    void toDtoMapsNullToNull() {
        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    void toDtoMapsEntityAsExpected() {
        var entity = TodoEntityFixtures.todoEntity();
        var dto = TodoDto.builder()
                .todoId(entity.getTodoId())
                .build();

        assertThat(mapper.toDto(entity)).isEqualTo(dto);
    }

}
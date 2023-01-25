package be.thebeehive.tdd.todoapp.service.mapper;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import be.thebeehive.tdd.todoapp.api.dto.CreateTodoDto;
import be.thebeehive.tdd.todoapp.api.dto.TodoDto;
import be.thebeehive.tdd.todoapp.api.dto.UpdateTodoDto;
import be.thebeehive.tdd.todoapp.model.TodoEntity;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface TodoMapper {
    default List<TodoDto> toTodoDto(Iterable<TodoEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::toDto)
                .toList();
    }

    @Mapping(source = "todoId", target = "todoId", defaultValue = "-1")
    TodoDto toDto(TodoEntity entity);

    @Mapping(source = "description", target = "description")
    @Mapping(target = "todoId", ignore = true)
    @Mapping(target = "complete", ignore = true)
    TodoEntity toEntity(CreateTodoDto dto);

    @Mapping(target = "todo.todoId", ignore = true)
    @Mapping(target = "todo.complete", ignore = true)
    @Mapping(source = "dto.description", target = "todo.description")
    void update(@MappingTarget TodoEntity todo, UpdateTodoDto dto);
}

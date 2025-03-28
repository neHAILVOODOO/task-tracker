package com.example.tasktracker.mapper;

import com.example.tasktracker.model.dto.CreateUpdateTaskDto;
import com.example.tasktracker.model.dto.GetTaskDto;
import com.example.tasktracker.model.dto.GetTaskForListDto;
import com.example.tasktracker.model.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

    public Task mapCreateUpdateTaskDtoToTask(CreateUpdateTaskDto createUpdateTaskDto) {

        return Task.builder()
                .name(createUpdateTaskDto.getName())
                .description(createUpdateTaskDto.getDescription())
                .priority(createUpdateTaskDto.getPriority())
                .status(createUpdateTaskDto.getStatus())
                .build();

    }

    public GetTaskForListDto mapTaskToTaskForListDto(Task task) {

        return GetTaskForListDto.builder()
                .name(task.getName())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .commentsCount(task.getComments().size())
                .build();
    }

    public GetTaskDto mapTaskToGetTaskDto(Task task) {

        return GetTaskDto.builder()
                .name(task.getName())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .author(userMapper.mapUserToUserPreviewDto(task.getUser()))
                .build();

    }


}

package com.example.tasktracker.mapper;

import com.example.tasktracker.model.dto.task.CreateUpdateTaskDto;
import com.example.tasktracker.model.dto.task.GetTaskDto;
import com.example.tasktracker.model.dto.task.GetTaskForListDto;
import com.example.tasktracker.model.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final UserMapper userMapper;

    public Task mapCreateUpdateTaskDtoToTask(CreateUpdateTaskDto createUpdateTaskDto) {

        return Task.builder()
                .name(createUpdateTaskDto.getName())
                .description(createUpdateTaskDto.getDescription())
                .priority(createUpdateTaskDto.getPriority())
                .status(createUpdateTaskDto.getStatus())
                .onChecking(false)
                .build();

    }

    public GetTaskForListDto mapTaskToTaskForListDto(Task task) {

        return GetTaskForListDto.builder()
                .id(task.getId())
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
                .onChecking(task.isOnChecking())
                .author(userMapper.mapUserToUserPreviewDto(task.getAuthor()))
                .executor(userMapper.mapUserToUserPreviewDto(task.getExecutor()))
                .build();

    }


}

package com.example.tasktracker.service.impl;

import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.model.dto.CreateUpdateTaskDto;
import com.example.tasktracker.model.dto.GetTaskDto;
import com.example.tasktracker.model.dto.GetTaskForListDto;
import com.example.tasktracker.model.entity.Task;
import com.example.tasktracker.model.entity.User;
import com.example.tasktracker.model.enums.TaskPriority;
import com.example.tasktracker.model.enums.TaskStatus;
import com.example.tasktracker.repo.TaskRepo;
import com.example.tasktracker.repo.UserRepo;
import com.example.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;

    private final TaskMapper taskMapper;

    @Override
    public void createTask(CreateUpdateTaskDto createUpdateTaskDto, long userId) {
        Task task = taskMapper.mapCreateUpdateTaskDtoToTask(createUpdateTaskDto);
        task.setUser(userRepo.findUserById(userId));
        taskRepo.save(task);
    }

    @Override
    public List<GetTaskForListDto> findTasksByUser(long userId) {
        User user = userRepo.findUserById(userId);
        return taskRepo.findAllByUser(user).stream().map(taskMapper::mapTaskToTaskForListDto).collect(Collectors.toList());
    }

    @Override
    public GetTaskDto findTaskByUserAndId(long userId, long taskId) {
        User user = userRepo.findUserById(userId);
        Task foundTask = taskRepo.findByUserAndId(user, taskId);

        if (foundTask == null) {
            throw new NotFoundException("Такой задачи не существует");
        }

        return taskMapper.mapTaskToGetTaskDto(foundTask);
    }

    @Override
    public void updateTask(CreateUpdateTaskDto createUpdateTaskDto, long taskId, long userId) {
        Task task = taskRepo.findByUserAndId(userRepo.findUserById(userId), taskId);
        BeanUtils.copyProperties(createUpdateTaskDto, task, "user", "comments");
        taskRepo.save(task);
    }
}

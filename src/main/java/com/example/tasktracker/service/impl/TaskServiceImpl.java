package com.example.tasktracker.service.impl;

import com.example.tasktracker.exception.CannotChangeTaskChecking;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.model.dto.task.CreateUpdateTaskDto;
import com.example.tasktracker.model.dto.task.GetTaskDto;
import com.example.tasktracker.model.dto.task.GetTaskForListDto;
import com.example.tasktracker.model.dto.task.TaskCheckingDto;
import com.example.tasktracker.model.entity.Task;
import com.example.tasktracker.model.entity.User;
import com.example.tasktracker.model.enums.UserRole;
import com.example.tasktracker.repo.TaskRepo;
import com.example.tasktracker.repo.UserRepo;
import com.example.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.internal.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;

    private final TaskMapper taskMapper;

    @Override
    public void createTask(CreateUpdateTaskDto createUpdateTaskDto, long authorId) {
        Task task = taskMapper.mapCreateUpdateTaskDtoToTask(createUpdateTaskDto);
        task.setAuthor(userRepo.findUserById(authorId));
        task.setExecutor(userRepo.findUserById(createUpdateTaskDto.getExecutorId()));
        taskRepo.save(task);
    }

    @Override
    public Page<GetTaskForListDto> findTasksByAuthor(long authorId, int page, int size, String sortBy, String direction) {
        return findTasksByUser(authorId, page, size, sortBy, direction, taskRepo::findAllByAuthor);
    }

    @Override
    public Page<GetTaskForListDto> findTasksByExecutor(long executorId, int page, int size, String sortBy, String direction) {
        return findTasksByUser(executorId, page, size, sortBy, direction, taskRepo::findAllByExecutor);
    }

    @Override
    public Page<GetTaskForListDto> findAllTasks(int page, int size, String sortBy, String direction) {
        return findTasks(page, size, sortBy, direction, taskRepo::findAll);
    }

    @Override
    public GetTaskDto findTaskByExecutorAndId(long executorId, long taskId) {
        Task task = taskRepo.findByExecutorAndId(userRepo.findUserById(executorId), taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        return taskMapper.mapTaskToGetTaskDto(task);
    }

    @Override
    public void updateTask(CreateUpdateTaskDto createUpdateTaskDto, long taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));
        BeanUtils.copyProperties(createUpdateTaskDto, task, "user", "comments");
        taskRepo.save(task);
    }

    @Override
    public void deleteTask(long taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));
        taskRepo.delete(task);
    }

    @Override
    public void setTaskChecking(TaskCheckingDto taskChecking, long taskId, long userId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        if (userRepo.findUserById(userId).getRole().equals(UserRole.EMPLOYEE) && task.isOnChecking()) {
            throw new CannotChangeTaskChecking("Задача уже на проверке");
        }

        if (userRepo.findUserById(userId).getRole().equals(UserRole.MANAGER) && !task.isOnChecking()) {
            throw new CannotChangeTaskChecking("Задача не послана на проверку");
        }

        BeanUtils.copyProperties(taskChecking, task, "user", "comments");
        taskRepo.save(task);
    }

    private Sort createSort(String sortBy, String direction) {
        Set<String> allowedFields = Set.of("id", "name", "priority", "status", "onChecking");
        String validSortBy = allowedFields.contains(sortBy) ? sortBy : "id";

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(sortDirection, validSortBy);
    }

    private Page<GetTaskForListDto> findTasksByUser(
            long userId,
            int page,
            int size,
            String sortBy,
            String direction,
            BiFunction<User, Pageable, Page<Task>> taskFinder
    ) {
        int validSize = List.of(5, 10, 15).contains(size) ? size : 10;
        Sort sort = createSort(sortBy, direction);
        Pageable pageable = PageRequest.of(page, validSize, sort);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return taskFinder.apply(user, pageable)
                .map(taskMapper::mapTaskToTaskForListDto);
    }

    private Page<GetTaskForListDto> findTasks(
            int page,
            int size,
            String sortBy,
            String direction,
            Function<Pageable, Page<Task>> taskFinder
    ) {
        int validSize = List.of(5, 10, 15).contains(size) ? size : 10;
        Sort sort = createSort(sortBy, direction);
        Pageable pageable = PageRequest.of(page, validSize, sort);

        return taskFinder.apply(pageable)
                .map(taskMapper::mapTaskToTaskForListDto);
    }

}

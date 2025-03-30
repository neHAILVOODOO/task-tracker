package com.example.tasktracker.service;

import com.example.tasktracker.model.dto.CreateUpdateTaskDto;
import com.example.tasktracker.model.dto.GetTaskDto;
import com.example.tasktracker.model.dto.GetTaskForListDto;
import com.example.tasktracker.model.dto.TaskCheckingDto;
import org.springframework.data.domain.Page;

public interface TaskService {

    void createTask(CreateUpdateTaskDto createUpdateTaskDto, long authorId);
    Page<GetTaskForListDto> findTasksByAuthor(long authorId, int page, int size, String sortBy, String direction);
    Page<GetTaskForListDto> findTasksByExecutor(long executorId, int page, int size, String sortBy, String direction);
    Page<GetTaskForListDto> findAllTasks(int page, int size, String sortBy, String direction);
    GetTaskDto findTaskByExecutorAndId(long executorId, long taskId);
    void updateTask(CreateUpdateTaskDto createUpdateTaskDto, long taskId);
    void deleteTask(long taskId);
    void setTaskChecking(TaskCheckingDto taskChecking, long taskId, long userId);

}

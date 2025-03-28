package com.example.tasktracker.service;

import com.example.tasktracker.model.dto.CreateUpdateTaskDto;
import com.example.tasktracker.model.dto.GetTaskDto;
import com.example.tasktracker.model.dto.GetTaskForListDto;
import com.example.tasktracker.model.entity.User;

import java.util.List;

public interface TaskService {

    void createTask(CreateUpdateTaskDto createUpdateTaskDto, long userId);

    List<GetTaskForListDto> findTasksByUser(long userId);

    GetTaskDto findTaskByUserAndId(long userId, long taskId);

    void updateTask(CreateUpdateTaskDto createUpdateTaskDto, long taskId, long userId);

}

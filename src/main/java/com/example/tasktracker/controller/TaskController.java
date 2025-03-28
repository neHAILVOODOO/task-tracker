package com.example.tasktracker.controller;

import com.example.tasktracker.model.dto.CreateUpdateTaskDto;
import com.example.tasktracker.model.dto.GetTaskDto;
import com.example.tasktracker.model.dto.GetTaskForListDto;
import com.example.tasktracker.security.UserPrincipal;
import com.example.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<GetTaskForListDto>> getUserTasks(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<GetTaskForListDto> tasks = taskService.findTasksByUser(userPrincipal.getUserId());
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Void> createTask(@Valid @RequestBody CreateUpdateTaskDto createUpdateTaskDto,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {

        taskService.createTask(createUpdateTaskDto, userPrincipal.getUserId());
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/{taskId}")
    public ResponseEntity<GetTaskDto> getTaskByIdAndUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                            @PathVariable long taskId) {

        GetTaskDto getTaskDto = taskService.findTaskByUserAndId(userPrincipal.getUserId(), taskId);
        return ResponseEntity.ok(getTaskDto);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<GetTaskDto> updateTaskForUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                        @Valid @RequestBody CreateUpdateTaskDto createUpdateTaskDto,
                                                        @PathVariable long taskId) {

        taskService.updateTask(createUpdateTaskDto,taskId, userPrincipal.getUserId());
        return ResponseEntity.noContent().build();

    }




}

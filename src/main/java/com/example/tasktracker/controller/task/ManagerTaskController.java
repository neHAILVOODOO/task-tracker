package com.example.tasktracker.controller.task;

import com.example.tasktracker.model.dto.CreateUpdateTaskDto;
import com.example.tasktracker.model.dto.GetTaskForListDto;
import com.example.tasktracker.model.dto.TaskCheckingDto;
import com.example.tasktracker.security.UserPrincipal;
import com.example.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerTaskController {

    private final TaskService taskService;

    @GetMapping("/tasks")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Page<GetTaskForListDto>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Min(5) @Max(15) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<GetTaskForListDto> tasks = taskService.findAllTasks(page, size, sortBy, direction);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/tasks")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> createTask(
            @Valid @RequestBody CreateUpdateTaskDto createUpdateTaskDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        taskService.createTask(createUpdateTaskDto, userPrincipal.getUserId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/tasks/my")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Page<GetTaskForListDto>> getAuthorTasks(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Min(5) @Max(15) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<GetTaskForListDto> tasks = taskService.findTasksByAuthor(userPrincipal.getUserId(), page, size, sortBy, direction);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/tasks/{taskId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> updateTask(
            @Valid @RequestBody CreateUpdateTaskDto createUpdateTaskDto,
            @PathVariable long taskId) {

        taskService.updateTask(createUpdateTaskDto, taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/tasks/{taskId}")
    @PreAuthorize("hasRole('MANAGER')")
    ResponseEntity<Void> deleteTask(@PathVariable long taskId) {

        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/tasks/{taskId}/check")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> sendChangeTaskCheckingStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody TaskCheckingDto taskCheckingDto,
            @PathVariable long taskId) {

        taskService.setTaskChecking(taskCheckingDto, taskId, userPrincipal.getUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @GetMapping("/user/{userId}/tasks")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Page<GetTaskForListDto>> getUserTasks(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Min(5) @Max(15) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<GetTaskForListDto> tasks = taskService.findTasksByExecutor(userId, page, size, sortBy, direction);
        return ResponseEntity.ok(tasks);
    }


}

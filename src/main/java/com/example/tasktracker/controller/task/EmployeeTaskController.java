package com.example.tasktracker.controller.task;

import com.example.tasktracker.annotation.IsManagerOrCurrentUser;
import com.example.tasktracker.model.dto.task.GetTaskDto;
import com.example.tasktracker.model.dto.task.GetTaskForListDto;
import com.example.tasktracker.model.dto.task.TaskCheckingDto;
import com.example.tasktracker.service.TaskService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeTaskController {

    private final TaskService taskService;

    @GetMapping("/{userId}/tasks")
    @IsManagerOrCurrentUser
    public ResponseEntity<Page<GetTaskForListDto>> getExecutorTasks(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Min(5) @Max(15) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<GetTaskForListDto> tasks = taskService.findTasksByExecutor(userId, page, size, sortBy, direction);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{userId}/tasks/{taskId}")
    @IsManagerOrCurrentUser
    public ResponseEntity<GetTaskDto> getTaskById(@PathVariable long userId,
                                                  @PathVariable long taskId) {

        GetTaskDto getTaskDto = taskService.findTaskByExecutorAndId(userId, taskId);
        return ResponseEntity.ok(getTaskDto);
    }

    @PatchMapping("/{userId}/tasks/{taskId}")
    @IsManagerOrCurrentUser
    public ResponseEntity<Void> sendTaskOnChecking(@PathVariable long userId,
                                                   @RequestBody TaskCheckingDto taskCheckingDto,
                                                   @PathVariable long taskId) {

        taskService.setTaskChecking(taskCheckingDto, taskId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

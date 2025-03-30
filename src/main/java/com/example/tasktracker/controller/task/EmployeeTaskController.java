package com.example.tasktracker.controller.task;

import com.example.tasktracker.model.dto.GetTaskDto;
import com.example.tasktracker.model.dto.GetTaskForListDto;
import com.example.tasktracker.model.dto.TaskCheckingDto;
import com.example.tasktracker.security.UserPrincipal;
import com.example.tasktracker.service.TaskService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee/tasks")
public class EmployeeTaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<GetTaskForListDto>> getExecutorTasks(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Min(5) @Max(15) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<GetTaskForListDto> tasks = taskService.findTasksByExecutor(userPrincipal.getUserId(), page, size, sortBy, direction);
        System.out.println(tasks);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<GetTaskDto> getTaskById(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @PathVariable long taskId) {

        GetTaskDto getTaskDto = taskService.findTaskByExecutorAndId(userPrincipal.getUserId(), taskId);
        return ResponseEntity.ok(getTaskDto);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<Void> sendTaskOnChecking(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @RequestBody TaskCheckingDto taskCheckingDto,
                                                   @PathVariable long taskId) {

        taskService.setTaskChecking(taskCheckingDto, taskId, userPrincipal.getUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

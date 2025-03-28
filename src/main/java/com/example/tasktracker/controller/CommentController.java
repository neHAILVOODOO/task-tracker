package com.example.tasktracker.controller;

import com.example.tasktracker.model.dto.CreateUpdateCommentDto;
import com.example.tasktracker.model.dto.GetCommentDto;
import com.example.tasktracker.security.UserPrincipal;
import com.example.tasktracker.service.CommentService;
import com.example.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{taskId}/comments")
    private ResponseEntity<List<GetCommentDto>> getTaskComments(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                @PathVariable long taskId) {

        List<GetCommentDto> comments = commentService.findAllCommentsByTask(taskId, userPrincipal.getUserId());
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{taskId}/comments")
    private ResponseEntity<String> commentTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @Valid @RequestBody CreateUpdateCommentDto createUpdateCommentDto,
                                               @PathVariable long taskId) {

        commentService.addCommentForTask(createUpdateCommentDto, taskId, userPrincipal.getUserId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}

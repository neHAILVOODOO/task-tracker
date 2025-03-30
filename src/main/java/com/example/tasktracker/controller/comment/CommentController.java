package com.example.tasktracker.controller.comment;

import com.example.tasktracker.annotation.IsManagerOrCurrentUser;
import com.example.tasktracker.model.dto.CreateUpdateCommentDto;
import com.example.tasktracker.model.dto.GetCommentDto;
import com.example.tasktracker.security.UserPrincipal;
import com.example.tasktracker.service.CommentService;
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
@RequestMapping("/employee")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{userId}/tasks/{taskId}/comments")
    @IsManagerOrCurrentUser
    private ResponseEntity<List<GetCommentDto>> getTaskComments(@PathVariable long userId,
                                                                @PathVariable long taskId) {

        List<GetCommentDto> comments = commentService.findAllCommentsByTask(taskId, userId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{userId}/tasks/{taskId}/comments")
    @IsManagerOrCurrentUser
    private ResponseEntity<String> commentTask(@PathVariable long userId,
                                               @Valid @RequestBody CreateUpdateCommentDto createUpdateCommentDto,
                                               @PathVariable long taskId) {

        commentService.addCommentForTask(createUpdateCommentDto, taskId, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}

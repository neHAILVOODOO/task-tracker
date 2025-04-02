package com.example.tasktracker.controller.comment;

import com.example.tasktracker.annotation.IsManagerOrCurrentUser;
import com.example.tasktracker.model.dto.comment.CreateUpdateCommentDto;
import com.example.tasktracker.model.dto.comment.GetCommentDto;
import com.example.tasktracker.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/employee")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{userId}/tasks/{taskId}/comments")
    @IsManagerOrCurrentUser
    public ResponseEntity<List<GetCommentDto>> getTaskComments(
            @PathVariable long userId,
            @PathVariable long taskId) {

        List<GetCommentDto> comments = commentService.findAllCommentsByTask(taskId, userId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{userId}/tasks/{taskId}/comments")
    @IsManagerOrCurrentUser
    public ResponseEntity<String> commentTask(
            @PathVariable long userId,
            @Valid @RequestBody CreateUpdateCommentDto createUpdateCommentDto,
            @PathVariable long taskId) {

        commentService.addCommentForTask(createUpdateCommentDto, taskId, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}/tasks/{taskId}/comments/{commentId}")
    @IsManagerOrCurrentUser
    public ResponseEntity<String> updateComment(
            @PathVariable long userId,
            @PathVariable long taskId,
            @PathVariable long commentId,
            @Valid @RequestBody CreateUpdateCommentDto createUpdateCommentDto
    ) {

        commentService.updateComment(userId, taskId, commentId, createUpdateCommentDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{userId}/tasks/{taskId}/comments/{commentId}")
    @IsManagerOrCurrentUser
    public ResponseEntity<String> deleteComment(
            @PathVariable long userId,
            @PathVariable long taskId,
            @PathVariable long commentId
    ) {

        commentService.deleteComment(userId, taskId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

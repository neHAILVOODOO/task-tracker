package com.example.tasktracker.service;

import com.example.tasktracker.model.dto.comment.CreateUpdateCommentDto;
import com.example.tasktracker.model.dto.comment.GetCommentDto;

import java.util.List;

public interface CommentService {

    List<GetCommentDto> findAllCommentsByTask(long taskId, long userId);

    void addCommentForTask(CreateUpdateCommentDto createUpdateCommentDto, long taskId, long userId);

    void deleteComment(long userId, long taskId, long commentId);

    void updateComment(long userId, long taskId, long commentId, CreateUpdateCommentDto createUpdateCommentDto);

}

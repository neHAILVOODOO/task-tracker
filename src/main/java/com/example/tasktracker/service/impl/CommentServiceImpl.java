package com.example.tasktracker.service.impl;

import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.mapper.CommentMapper;
import com.example.tasktracker.model.dto.CreateUpdateCommentDto;
import com.example.tasktracker.model.dto.GetCommentDto;
import com.example.tasktracker.model.entity.Comment;
import com.example.tasktracker.model.entity.Task;
import com.example.tasktracker.repo.CommentRepo;
import com.example.tasktracker.repo.TaskRepo;
import com.example.tasktracker.repo.UserRepo;
import com.example.tasktracker.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;
    private final TaskRepo taskRepo;
    private final UserRepo userRepo;

    private final CommentMapper commentMapper;

    @Override
    public List<GetCommentDto> findAllCommentsByTask(long taskId, long userId) {


        Task task = taskRepo.findByExecutorAndId(userRepo.findUserById(userId), taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        List<Comment> comments = commentRepo.findAllByTask(task);
        return comments.stream().map(commentMapper::mapCommentToGetCommentDto).collect(Collectors.toList());

    }

    @Override
    public void addCommentForTask(CreateUpdateCommentDto createUpdateCommentDto, long taskId, long userId) {

        Task task = taskRepo.findByExecutorAndId(userRepo.findUserById(userId), taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));
        Comment comment = commentMapper.mapCreateUpdateCommentDtoToComment(createUpdateCommentDto);
        comment.setAuthor(userRepo.findUserById(userId));
        comment.setTask(task);
        commentRepo.save(comment);
    }

    @Override
    public void deleteComment(long userId, long taskId, long commentId) {

        Task task = taskRepo.findByExecutorAndId(userRepo.findUserById(userId),taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        Comment comment = commentRepo.findByIdAndTask(commentId, task)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        commentRepo.delete(comment);

    }

    @Override
    public void updateComment(long userId, long taskId, long commentId, CreateUpdateCommentDto createUpdateCommentDto) {

        Task task = taskRepo.findByExecutorAndId(userRepo.findUserById(userId),taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        Comment comment = commentRepo.findByIdAndTask(commentId, task)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        BeanUtils.copyProperties(createUpdateCommentDto, comment);
        commentRepo.save(comment);

    }

}

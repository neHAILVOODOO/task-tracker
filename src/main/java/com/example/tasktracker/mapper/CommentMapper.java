package com.example.tasktracker.mapper;

import com.example.tasktracker.model.dto.CreateUpdateCommentDto;
import com.example.tasktracker.model.dto.GetCommentDto;
import com.example.tasktracker.model.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;

    public GetCommentDto mapCommentToGetCommentDto(Comment comment) {

        return GetCommentDto.builder()
                .text(comment.getText())
                .author(userMapper.mapUserToUserPreviewDto(comment.getAuthor()))
                .build();


    }

    public Comment mapCreateUpdateCommentDtoToComment(CreateUpdateCommentDto createUpdateCommentDto) {

        return Comment.builder()
                .text(createUpdateCommentDto.getText())
                .build();

    }


}

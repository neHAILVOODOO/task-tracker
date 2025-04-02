package com.example.tasktracker.model.dto.comment;

import com.example.tasktracker.model.dto.user.UserPreviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCommentDto {

    private String text;
    private UserPreviewDto author;


}

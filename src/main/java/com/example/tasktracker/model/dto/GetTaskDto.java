package com.example.tasktracker.model.dto;

import com.example.tasktracker.model.enums.TaskPriority;
import com.example.tasktracker.model.enums.TaskStatus;
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
public class GetTaskDto {

    private String name;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private boolean onChecking;
    private UserPreviewDto author;
    private UserPreviewDto executor;


}

package com.example.tasktracker.model.dto;

import com.example.tasktracker.model.enums.TaskPriority;
import com.example.tasktracker.model.enums.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateUpdateTaskDto {

    @NotBlank(message = "Название задачи не должно быть пустым")
    private String name;
    @NotBlank(message = "Описание задачи не должно быть пустым")
    private String description;
    @NotNull(message = "Укажите приоритет задачи")
    private TaskPriority priority;
    @NotNull(message = "Укажите статус задачи")
    private TaskStatus status;
    @NotNull(message = "Укажите исполнителя задачи")
    private Long executorId;

}

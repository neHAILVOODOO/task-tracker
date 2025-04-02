package com.example.tasktracker.model.dto.task;

import com.example.tasktracker.model.enums.TaskStatus;
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
public class TaskCheckingDto {

    @NotNull(message = "Укажите, отправляете ли вы задачу на проверку")
    private Boolean onChecking;
    @NotNull(message = "Укажите статус задачи")
    private TaskStatus status;

}

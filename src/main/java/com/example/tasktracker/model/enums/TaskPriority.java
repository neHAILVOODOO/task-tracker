package com.example.tasktracker.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public enum TaskPriority {

    LOW("Низкий"),
    MEDIUM("Средний"),
    HIGH("Высокий");


    private final String displayPriority;

}

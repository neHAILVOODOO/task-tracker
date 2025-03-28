package com.example.tasktracker.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public enum TaskPriority {

    HIGH("Высокий"),
    MEDIUM("Средний"),
    LOW("Низкий");

    private final String displayPriority;

}

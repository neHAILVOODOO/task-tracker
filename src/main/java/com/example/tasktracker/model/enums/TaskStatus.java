package com.example.tasktracker.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public enum TaskStatus {

    WAITING("В ожидании"),
    IN_PROCESS("В процессе"),
    DONE("Завершено");


    private final String displayStatus;



}

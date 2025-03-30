package com.example.tasktracker.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
@Getter
@ToString
public enum UserRole implements GrantedAuthority {

    EMPLOYEE("Сотрудник"),
    MANAGER("Начальник");

    private final String displayRole;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }

}


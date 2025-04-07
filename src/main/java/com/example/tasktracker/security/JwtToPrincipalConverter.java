package com.example.tasktracker.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tasktracker.model.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class JwtToPrincipalConverter {

    public UserPrincipal convert(DecodedJWT jwt) {
        return UserPrincipal.builder()
                .userId(Long.valueOf(jwt.getSubject()))
                .login(jwt.getClaim("e").asString())
                .role(extractRoleFromClaim(jwt))
                .build();
    }

    private UserRole extractRoleFromClaim(DecodedJWT jwt) {
        if (jwt == null) return UserRole.EMPLOYEE;

        String roleClaim = jwt.getClaim("a").asList(String.class)
                .stream()
                .findFirst()
                .orElse(null);

        if (roleClaim == null) return UserRole.EMPLOYEE;

        String cleanRole = roleClaim.replaceFirst("^ROLE_", "").toUpperCase();

        try {
            return UserRole.valueOf(cleanRole);
        } catch (IllegalArgumentException e) {
            return UserRole.EMPLOYEE;
        }
    }


}

package com.example.tasktracker.exception.handler;

import com.example.tasktracker.exception.CannotChangeTaskChecking;
import com.example.tasktracker.exception.ConflictException;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверные учетные данные");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleEnumParseException(HttpMessageNotReadableException ex) {
        if (ex.getMessage().contains("not one of the values accepted for Enum class")) {

            String errorMessage = ex.getMessage();
            String enumName = errorMessage.substring(
                    errorMessage.indexOf("`") + 1,
                    errorMessage.lastIndexOf("`")
            ).replace("com.example.tasktracker.model.enums.", "");

            String allowedValues = errorMessage.substring(
                    errorMessage.indexOf("[") + 1,
                    errorMessage.indexOf("]")
            );

            return ResponseEntity
                    .badRequest()
                    .body("Недопустимое значение для " + enumName + ". Допустимые значения: " + allowedValues);
        }
        return ResponseEntity.badRequest().body("Неверный формат JSON");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        String requiredType = ex.getRequiredType().getSimpleName();
        String actualValue = ex.getValue().toString();

        String message = String.format(
                "Параметр '%s' должен быть типа %s, но получено: '%s'",
                parameterName, requiredType, actualValue
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(CannotChangeTaskChecking.class)
    public ResponseEntity<String> handleCannotChangeTaskChecking(CannotChangeTaskChecking ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нет доступа");
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}

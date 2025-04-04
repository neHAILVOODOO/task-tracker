package com.example.tasktracker.controller.user;

import com.example.tasktracker.model.dto.user.GetUserDto;
import com.example.tasktracker.model.dto.user.UpdateUserBioDto;
import com.example.tasktracker.model.dto.user.UpdateUserEmailDto;
import com.example.tasktracker.model.dto.user.UpdateUserPasswordDto;
import com.example.tasktracker.security.UserPrincipal;
import com.example.tasktracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/profile")
public class UserProfileController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserDto> getTaskComments(
            @PathVariable long userId
    ) {

        GetUserDto getUserDto = userService.getUserById(userId);
        return ResponseEntity.ok(getUserDto);
    }

    @PatchMapping("/edit/password")
    public ResponseEntity<String> updateUserPassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateUserPasswordDto updateUserPasswordDto
            ) {

        userService.updateUserPassword(updateUserPasswordDto, userPrincipal.getUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/edit/email")
    public ResponseEntity<String> updateUserEmail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateUserEmailDto updateUserEmailDto
    ) {

        userService.updateUserEmail(updateUserEmailDto, userPrincipal.getUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/edit/bio")
    public ResponseEntity<String> updateUserBio(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateUserBioDto updateUserBioDto
    ) {

        userService.updateUserBio(updateUserBioDto, userPrincipal.getUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

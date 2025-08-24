package com.studenthelper.user.controller;

import com.studenthelper.exceptions.AccessDeniedException;
import com.studenthelper.user.DTO.*;
import com.studenthelper.user.exceptions.InvalidPasswordException;
import com.studenthelper.user.exceptions.UserConflictException;
import com.studenthelper.user.exceptions.UserEmailConflictException;
import com.studenthelper.user.exceptions.UserNotFoundException;
import com.studenthelper.user.mapper.UserMapper;
import com.studenthelper.user.model.User;
import com.studenthelper.user.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }



    @GetMapping("/{id}")
    ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        User user = userService.getUserById(id);
        UserDTO userDTO = userMapper.toDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/username/{username}")
    ResponseEntity<UserDTO> getUserByName(@PathVariable String username) {
        User user = userService.getUserByName(username);
        UserDTO userDTO = userMapper.toDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping
    ResponseEntity<Void> addUser(@RequestBody UserCreateDTO user) {
        if(!userService.validatePassword(user.getPassword(), user.getConfirmPassword())) {
            throw new InvalidPasswordException();
        }
        User newUser = userMapper.toEntity(user);
        User savedUser = userService.addUser(newUser);
        Long id = savedUser.getId();
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").
                buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }


    @PutMapping("/{id}")
    ResponseEntity<Void> updateUser(@RequestBody UserUpdateDTO newUser, @PathVariable long id) {
        User user = userMapper.toEntity(newUser);
        userService.updateUser(id, user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/password")
    ResponseEntity<Void> updateUserPassword(@PathVariable long id, @RequestBody UserUpdatePasswordDTO newPassword) {
        userService.updateUserPassword(id,newPassword);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/email")
    ResponseEntity<Void> updateEmailPassword(@PathVariable long id, @RequestBody UserUpdateEmailDTO newEmail) {
        userService.updateUserEmail(id,newEmail);
        return ResponseEntity.noContent().build();
    }



    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @ControllerAdvice
    public static class UserExceptionHandler {
        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        @ExceptionHandler(UserConflictException.class)
        public ResponseEntity<String> handleUserConflict(UserConflictException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        @ExceptionHandler(InvalidPasswordException.class)
        public ResponseEntity<String> handleInvalidPassword(InvalidPasswordException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        @ExceptionHandler(UserEmailConflictException.class)
        public ResponseEntity<String> handleUserEmailConflict(UserEmailConflictException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}

package com.underfit.userservice.controller;

import com.underfit.userservice.configuration.Config;
import com.underfit.userservice.dto.UserDto;
import com.underfit.userservice.service.UserService;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserServiceController {
    private final Config config;
    private final UserService userService;

    @GetMapping("/get-description")
    public String getServiceDescription() {
        return "Service Name: " + config.getServiceName();
    }

    @GetMapping("/get-users")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        if (userDto.getId() != null) {
            userDto.setId(null);
        }
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PutMapping("/update-enabled-user/{id}/{enabled}")
    public ResponseEntity<?> updateEnabledUser(@PathVariable Long id, @PathVariable Boolean enabled) {
        userService.updateEnabledUser(id, enabled);
        return ResponseEntity.ok().body("Status changed");
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        userDto.setId(id);
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/exist-user/{id}")
    public ResponseEntity<?> existUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.existUserById(id));
    }

    @ExceptionHandler({EntityNotFoundException.class, FeignException.class})
    public ResponseEntity<?> handlerEntityNotFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}

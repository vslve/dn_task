package com.dntask.userphonebooks.controller;

import com.dntask.userphonebooks.service.UserService;
import com.dntask.userphonebooks.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<?> getUserByName(@RequestParam String name) {
        return ResponseEntity.ok().body(userService.getUserByName(name));
    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody UserEntity user) {
        return ResponseEntity.created(
                URI.create(String.format("/users/%d", user.getId())))
                .body(userService.addUser(user)
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserEntity user, @PathVariable Long id) {
        return ResponseEntity.ok().body(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.deleteUser(id));
    }
}

package com.ZETA.KN.controller;

import com.ZETA.KN.model.User;
import com.ZETA.KN.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService)
    {
        this.userService=userService;
    }
    @GetMapping("/login/{phone}")
    public ResponseEntity<Long> findPhoneNumber(@PathVariable Long phone) {
        System.out.println("subham kumar navratan");
        Optional<User> user = userService.findPhone(phone);
        return user.map(u -> ResponseEntity.ok(u.getPhone()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<String> saveUser(@RequestBody User user)
    {

        User savedUser = userService.save(user);
        return ResponseEntity.ok("User registered successfully.");
    }
}

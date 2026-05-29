package com.mohith.aitaskmanagementportal.controller;

import com.mohith.aitaskmanagementportal.model.Users;
import com.mohith.aitaskmanagementportal.service.JwtService;
import com.mohith.aitaskmanagementportal.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UsersService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users user){
        if(userService.register(user)) {
            return ResponseEntity.ok("User registered successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        if(authentication.isAuthenticated()) {
            return ResponseEntity.ok().body(jwtService.generateToken(user.getUsername()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login failed");
    }

}

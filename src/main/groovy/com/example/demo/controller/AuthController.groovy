package com.example.demo.controller


import com.example.demo.controller.response.AuthResponse
import com.example.demo.entity.User
import com.example.demo.service.AuthService
import jdk.jshell.spi.ExecutionControl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    AuthService authService

    @PostMapping("/signup")
    ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws ExecutionControl.UserException {

        var token = authService.signup(user)

        AuthResponse response = new AuthResponse(token, true)

        new ResponseEntity<AuthResponse>(response, HttpStatus.CREATED)
    }

    @PostMapping("/signin")
    ResponseEntity<AuthResponse> signing(@RequestBody User user) throws ExecutionControl.UserException {

        var token = authService.signin(user)

        AuthResponse response = new AuthResponse(token, true)

        new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED)
    }

}

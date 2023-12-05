package com.example.demo.controller

import com.example.demo.configuration.JwtProvider
import com.example.demo.controller.response.AuthResponse
import com.example.demo.entity.User

import com.example.demo.exception.UserException
import com.example.demo.repository.UserRepository
import com.example.demo.service.CustomUserDetailsServiceImplementation
import jdk.jshell.spi.ExecutionControl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    UserRepository userRepository
    @Autowired
    PasswordEncoder passwordEncoder
    @Autowired
    JwtProvider jwtProvider
    @Autowired
    CustomUserDetailsServiceImplementation customUserDetails

    @PostMapping("/signup")
    ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws ExecutionControl.UserException {

        var email = user.getEmail()
        var password = user.getPassword()
        var fullName = user.getFullName()

        var isEmailExist = userRepository.findByEmail(email)

        if (isEmailExist != null) {
            throw new UserException("Email is already used with another account")
        }

        var createdUser = new User()
        createdUser.setEmail(email)
        createdUser.setFullName(fullName)
        createdUser.setPassword(passwordEncoder.encode(password))

        var savedUser = userRepository.save(createdUser)

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password)
        SecurityContextHolder.getContext().setAuthentication(authentication)

        var token = jwtProvider.generateToken(authentication)

        AuthResponse response = new AuthResponse(token, true)

        new ResponseEntity<AuthResponse>(response, HttpStatus.CREATED)
    }

    @PostMapping("/signin")
    ResponseEntity<AuthResponse> signing(@RequestBody User user) throws ExecutionControl.UserException {
        var email = user.getEmail()
        var password = user.getPassword()

        Authentication authentication = authenticate(email, password)

        var token = jwtProvider.generateToken(authentication)

        AuthResponse response = new AuthResponse(token, true)

        new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED)
    }

    Authentication authenticate(String userName, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(userName)

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect email or password")
        }

        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
    }

}

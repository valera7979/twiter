package com.example.demo.service

import com.example.demo.configuration.JwtProvider
import com.example.demo.entity.User
import com.example.demo.exception.UserException
import com.example.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository
    @Autowired
    PasswordEncoder passwordEncoder
    @Autowired
    JwtProvider jwtProvider
    @Autowired
    CustomUserDetailsServiceImplementation customUserDetails

    @Override
    String signup(User user) {
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

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword())
        SecurityContextHolder.getContext().setAuthentication(authentication)

        jwtProvider.generateToken(authentication)
    }

    @Override
    String signin(User user) {
        var email = user.getEmail()
        var password = user.getPassword()

        Authentication authentication = authenticate(email, password)

        jwtProvider.generateToken(authentication)
    }

    Authentication authenticate(String userName, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(userName)

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect email or password")
        }

        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
    }
}

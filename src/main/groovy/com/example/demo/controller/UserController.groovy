package com.example.demo.controller

import com.example.demo.controller.dto.UserDto
import com.example.demo.entity.User
import com.example.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController {

    @Autowired
    UserService userService

    @PutMapping("/{userId}/subscribe")
    ResponseEntity<UserDto> subscribeToUser(@PathVariable("userId") String userId,
        @RequestHeader("Authorization") String jwt) {

        User currentUser = userService.findUserProfileByJwt(jwt)
        var user = userService.subscribeUser(userId, currentUser)

        new ResponseEntity<>(UserDto.toUserDto(user), HttpStatus.ACCEPTED)
    }

    @PutMapping
    ResponseEntity<UserDto> updateUser(@RequestBody User user, @RequestHeader("Authorization") String jwt) {
        User currentUser = userService.findUserProfileByJwt(jwt)
        new ResponseEntity<>(UserDto.toUserDto(userService.updateUser(currentUser, user)), HttpStatus.ACCEPTED)
    }

    @DeleteMapping
    ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String jwt) {
        var currentUser = userService.findUserProfileByJwt(jwt)
        userService.deleteUser(currentUser)
        new ResponseEntity<Void>(HttpStatus.NO_CONTENT)
    }
}

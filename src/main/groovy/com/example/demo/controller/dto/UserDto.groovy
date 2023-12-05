package com.example.demo.controller.dto

import com.example.demo.entity.User
import groovy.transform.TupleConstructor

import java.util.stream.Collectors

@TupleConstructor(includeFields = true)
class UserDto {
    String id

    String fullName
    String email

    List<UserDto> subscriptions

    static UserDto toUserDto(User user) {
        var userDto = new UserDto()
        userDto.setId(user.getId())
        userDto.setFullName(user.getFullName())
        userDto.setEmail(user.getEmail())
        userDto.setSubscriptions(toUserDtos(user.getSubscriptions()))
        userDto
    }

    static List<UserDto> toUserDtos(List<User> subscriptions) {
        subscriptions.stream()
        .map(user -> new UserDto().with {
            id = user.getId()
            fullName = user.getFullName()
            email = user.getEmail()
            it
        })
        .collect(Collectors.toList())
    }

}

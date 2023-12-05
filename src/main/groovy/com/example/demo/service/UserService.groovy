package com.example.demo.service

import com.example.demo.entity.User
import com.example.demo.exception.UserException

interface UserService {

    User findUserById(String userId) throws UserException
    User findUserProfileByJwt(String jwt) throws UserException
    User updateUser(User currentUser, User updateUser) throws UserException

    User subscribeUser(String userId, User user) throws UserException

    void deleteUser(User user)

}

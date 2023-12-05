package com.example.demo.service

import com.example.demo.configuration.JwtProvider
import com.example.demo.entity.User
import com.example.demo.exception.UserException

import com.example.demo.repository.LikeRepository
import com.example.demo.repository.PostRepository
import com.example.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository
    @Autowired
    JwtProvider jwtProvider
    @Autowired
    PostRepository postRepository
    @Autowired
    LikeRepository likeRepository
    @Autowired
    PasswordEncoder passwordEncoder

    @Override
    User findUserById(String userId) throws UserException {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User with id ${userId} was not found"))
    }

    @Override
    User findUserProfileByJwt(String jwt) throws UserException {
        var email = jwtProvider.getEmailFromToken(jwt)
        var user = userRepository.findByEmail(email)
        if (user == null) {
            throw new UserException("User was not found")
        }

        user
    }

    @Override
    User updateUser(User currentUser, User updateUser) throws UserException {
        if (updateUser.getEmail() != null) {
            if (updateUser.getEmail() != currentUser.getEmail()) {
                var isEmailExist = userRepository.findByEmail(updateUser.getEmail())

                if (isEmailExist != null) {
                    throw new UserException("Email is already used with another account")
                }
                currentUser.setEmail(updateUser.getEmail())
            }
        }

        if (updateUser.getPassword() != null) {
            currentUser.setPassword(passwordEncoder.encode(updateUser.getPassword()))
        }

        if (updateUser.getFullName() != null) {
            currentUser.setFullName(updateUser.getFullName())
        }

        userRepository.save(currentUser)
    }

    @Override
    User subscribeUser(String userId, User user) throws UserException {
        if (userId == user.getId()) {
            throw new UserException("You cannot subscribe to yourself.")
        }
        var unsubscribed = user.getSubscriptions().stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElse(null)

        if (unsubscribed != null) {
            user.getSubscriptions().remove(unsubscribed)
            userRepository.save(user)
        } else {

            var subscribeToUser = findUserById(userId)
            user.getSubscriptions().add(subscribeToUser)
            userRepository.save(user)
        }

    }

    @Override
    void deleteUser(User user) {
        likeRepository.deleteAllByUser(user)
        var posts = postRepository.findAllByUser(user)
        likeRepository.deleteAllByPostIn(posts)
        postRepository.deleteAll(posts)

        userRepository.delete(user)
    }


}

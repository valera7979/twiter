package com.example.demo.service


import com.example.demo.entity.Like
import com.example.demo.entity.User
import com.example.demo.exception.PostException
import com.example.demo.exception.UserException

interface LikeService {

    Like likePost(String postId, User user) throws UserException, PostException

    List<Like> getAllLikes(String postId) throws PostException
}

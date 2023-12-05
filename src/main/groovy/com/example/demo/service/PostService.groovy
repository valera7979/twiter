package com.example.demo.service

import com.example.demo.controller.request.CommentRequest
import com.example.demo.entity.Post
import com.example.demo.entity.User
import com.example.demo.exception.PostException
import com.example.demo.exception.UserException

interface PostService {

    Post createPost(Post req, User user) throws UserException
    Post findById(String id)
    Post editPost(String postId, Post req, User user)
    void deletePostById(String id, User user) throws UserException, PostException
    Post createComment(CommentRequest request, User user) throws PostException

    List<Post> getFeed(User user)
    List<Post> getComments(String postId)

}

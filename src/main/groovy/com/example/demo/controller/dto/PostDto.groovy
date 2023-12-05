package com.example.demo.controller.dto

import com.example.demo.entity.Like
import com.example.demo.entity.Post
import com.example.demo.entity.User

import java.time.LocalDateTime

class PostDto {
    String id
    UserDto user
    String content
    LocalDateTime createdAt
    LocalDateTime editedAt
    boolean isPost
    boolean isComment

    List<CommentDto> comments = []
    long likesNumber
    boolean isLiked

    static PostDto toDto(User currentUser, Post post) {

        var postDto = new PostDto().with {
            id = post.getId()
            content = post.getContent()
            user = UserDto.toUserDto(post.getUser())
            createdAt = post.getCreatedAt()
            editedAt = post.getEditedAt()
            isPost = post.getIsPost()
            isComment = post.getIsComment()
            comments = CommentDto.toDtos(post.getComments())
            likesNumber = post.getLikes() != null ? post.getLikes().size() : 0
            isLiked = isLiked(currentUser, post.getLikes())
            it
        }

        postDto
    }

    static boolean isLiked(User user, List<Like> likes) {
        likes.stream()
                .filter(like -> like.getUser().getId() == user.getId())
                .findFirst()
                .map(like -> true)
                .orElse(false)
    }

}

package com.example.demo.controller.dto

import com.example.demo.entity.Post

import java.time.LocalDateTime
import java.util.stream.Collectors

class CommentDto {

    String id
    UserDto user
    String content
    LocalDateTime createdAt
    LocalDateTime editedAt
    boolean isPost
    boolean isComment
    String commentFor

    static List<CommentDto> toDtos(List<Post> posts) {
        posts.stream()
                .filter(post -> post != null)
                .map(comment -> new CommentDto().with {
                    id = comment.getId()
                    content = comment.getContent()
                    user = UserDto.toUserDto(comment.getUser())
                    createdAt = comment.getCreatedAt()
                    editedAt = comment.getEditedAt()
                    isPost = comment.getIsPost()
                    isComment = comment.getIsComment()
                    commentFor = comment.getCommentFor().getId()
                    it
                })
                .collect(Collectors.toList())
    }

}

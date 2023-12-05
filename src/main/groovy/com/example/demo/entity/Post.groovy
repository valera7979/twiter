package com.example.demo.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDateTime

@Document
class Post {

    @Id
    String id

    @DBRef
    User user

    String content

    LocalDateTime createdAt
    LocalDateTime editedAt

    boolean isPost

    @JsonIgnore
    @DBRef
    Post commentFor

    boolean isComment

    @DBRef
    List<Post> comments = []

    @DBRef
    List<Like> likes = []

}

package com.example.demo.repository

import com.example.demo.entity.Post
import com.example.demo.entity.User
import org.springframework.data.mongodb.repository.MongoRepository

interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findAllByUserInAndIsPostOrderByCreatedAt(Collection<User> users, boolean isPost)

    List<Post> findAllByCommentForIdOrderByCreatedAt(String commentForId)

    List<Post> findAllByUser(User user)
}

package com.example.demo.repository

import com.example.demo.entity.Like
import com.example.demo.entity.Post
import com.example.demo.entity.User
import org.springframework.data.mongodb.repository.MongoRepository

interface LikeRepository extends MongoRepository<Like, String> {

    List<Like> findAllByPostId(String postId)

    void deleteAllByUser(User user)
    void deleteAllByPostIn(Collection<Post> posts)



}
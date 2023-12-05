package com.example.demo.repository

import com.example.demo.entity.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email)
}

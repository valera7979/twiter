package com.example.demo.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.TupleConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
@TupleConstructor(includeFields = true)
class User {

    @Id
    String id

    String fullName
    String email

    String password
    boolean reg_user
    boolean login_with_google

    @JsonIgnore
    @DBRef
    List<User> subscriptions = []

}

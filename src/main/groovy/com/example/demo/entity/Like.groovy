package com.example.demo.entity

import groovy.transform.TupleConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
@TupleConstructor(includeFields = true)
class Like {

    @Id
    String id

    @DBRef
    User user

    @DBRef
    Post post

}

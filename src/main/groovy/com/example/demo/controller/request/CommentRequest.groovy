package com.example.demo.controller.request

import groovy.transform.TupleConstructor

@TupleConstructor(includeFields = true)
class CommentRequest {
    String content
    String postId
}

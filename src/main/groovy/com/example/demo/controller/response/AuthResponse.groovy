package com.example.demo.controller.response

import groovy.transform.TupleConstructor

@TupleConstructor(includeFields = true)
class AuthResponse {

    String jwt
    boolean status
    String message
}

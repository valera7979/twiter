package com.example.demo.controller.response

import groovy.transform.TupleConstructor

@TupleConstructor(includeFields = true)
class LikeResponse {

    Action action


    enum Action {
        LIKE, UNLIKE
    }

}

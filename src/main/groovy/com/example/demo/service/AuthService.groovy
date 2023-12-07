package com.example.demo.service

import com.example.demo.entity.User

interface AuthService {

    String signup(User user)

    String signin(User user)

}
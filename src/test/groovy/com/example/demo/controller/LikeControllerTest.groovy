package com.example.demo.controller


import com.example.demo.entity.Like
import com.example.demo.entity.User
import com.example.demo.service.LikeService
import com.example.demo.service.UserService
import org.junit.runner.RunWith
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner.class)
@WebMvcTest(value = LikeController.class)
@AutoConfigureMockMvc(addFilters = false)
class LikeControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    LikeController likeController

    @SpringBean
    LikeService likeService = Mock()

    @SpringBean
    UserService userService = Mock()

    def "when context is loaded then all expected beans are created"() {
        expect: "the LikeController is created"
        likeController
    }

    def "should like a post"() {
        given:
        def postId = "post123"
        def jwt = "jwtMock"
        def currentUser = new User(id: 1, fullName: "testUser")
        userService.findUserProfileByJwt(jwt) >> currentUser
        likeService.likePost(postId, currentUser) >> new Like()

        when:
        def result = mockMvc.perform(post("/api/likes/{postId}/like", postId)
                .header("Authorization", jwt))
                .andExpect(status().isCreated())
                .andReturn()

        then:
        result.response.contentAsString == '{"action":"LIKE"}'
    }

    def "should unlike a post"() {
        given:
        def postId = "post123"
        def jwt = "mockJwt"
        def currentUser = new User()

        userService.findUserProfileByJwt(jwt) >> currentUser
        likeService.likePost(postId, currentUser) >> null

        when:
        def result = mockMvc.perform(post("/api/likes/{postId}/like", postId)
                .header("Authorization", jwt))
                .andExpect(status().isNoContent())
                .andReturn()

        then:
        result.response.contentAsString == '{"action":"UNLIKE"}'
    }
}
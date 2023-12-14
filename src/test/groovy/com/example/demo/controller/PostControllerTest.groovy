package com.example.demo.controller

import com.example.demo.controller.request.CommentRequest
import com.example.demo.entity.Post
import com.example.demo.entity.User
import com.example.demo.service.PostService
import com.example.demo.service.UserService
import org.junit.jupiter.api.Disabled
import org.junit.runner.RunWith
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Ignore
import spock.lang.Specification
import org.springframework.http.HttpStatus
import com.example.demo.controller.dto.PostDto

@RunWith(SpringRunner.class)
@WebMvcTest(value = PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest extends Specification {

        @Autowired
        MockMvc mockMvc

        @Autowired
        PostController postController

        @SpringBean
        UserService userService = Mock()

        @SpringBean
        PostService postService = Mock()

    def "when context is loaded then all expected beans are created"() {
        expect: "the PostController is created"
        postController
    }

    @Ignore("Temporarily disabled due to userService mock issue. Need to fix")
    def "should get post by id"() {
        given:
        def postId = "postId"
        def jwt = "token"
        def currentUser = new User(id: 1, fullName: "testUser")
        def post = new Post(id: postId)

        userService.findUserProfileByJwt(jwt) >> currentUser
        postService.findById(postId) >> post

        when:
        def result = postController.getPost(postId, jwt)

        then:
        1 * userService.findUserProfileByJwt(jwt)
        1 * postService.findById(postId)

        result instanceof ResponseEntity
        result.statusCode == HttpStatus.OK
        result.body == PostDto.toDto(currentUser, post)
    }

    @Ignore("Temporarily disabled due to userService mock issue. Need to fix")
    def "should create a post"() {
        given:
        def jwt = "token"
        def currentUser = new User(id: 1, fullName: "testUser")
        def post = new Post()
        def createdPost = new Post()

        userService.findUserProfileByJwt(jwt) >> currentUser
        postService.createPost(post, currentUser) >> createdPost

        when:
        def result = postController.createPost(post, jwt)

        then:
        1 * userService.findUserProfileByJwt(jwt)
        1 * postService.createPost(post, currentUser)

        result instanceof ResponseEntity
        result.statusCode == HttpStatus.CREATED
        result.body == PostDto.toDto(currentUser, createdPost)
    }

    @Ignore("Temporarily disabled due to userService mock issue. Need to fix")
    def "should update a post"() {
        given:
        def postId = "postId"
        def jwt = "token"
        def currentUser = new User(id: 1, fullName: "testUser")
        def post = new Post()
        def editedPost = new Post()

        userService.findUserProfileByJwt(jwt) >> currentUser
        postService.editPost(postId, post, currentUser) >> editedPost

        when:
        def result = postController.updatePost(postId, post, jwt)

        then:
        1 * userService.findUserProfileByJwt(jwt)
        1 * postService.editPost(postId, post, currentUser)

        result instanceof ResponseEntity
        result.statusCode == HttpStatus.ACCEPTED
        result.body == PostDto.toDto(currentUser, editedPost)
    }

    @Ignore("Temporarily disabled due to userService mock issue. Need to fix")
    def "should create a comment"() {
        given:
        def jwt = "token"
        def currentUser = new User(id: 1, fullName: "testUser")
        def commentRequest = new CommentRequest()
        def post = new Post()

        userService.findUserProfileByJwt(jwt) >> currentUser
        postService.createComment(commentRequest, currentUser) >> post

        when:
        def result = postController.createComment(commentRequest, jwt)

        then:
        1 * userService.findUserProfileByJwt(jwt)
        1 * postService.createComment(commentRequest, currentUser)

        result instanceof ResponseEntity
        result.statusCode == HttpStatus.CREATED
        result.body == PostDto.toDto(currentUser, post)
    }

    @Ignore("Temporarily disabled due to userService mock issue. Need to fix")
    def "should delete a post"() {
        given:
        def postId = "postId"
        def jwt = "token"
        def currentUser = new User(id: 1, fullName: "testUser")

        userService.findUserProfileByJwt(jwt) >> currentUser
        postService.deletePostById(postId, currentUser)

        when:
        def result = postController.deletePost(postId, jwt)

        then:
        1 * userService.findUserProfileByJwt(jwt)
        1 * postService.deletePostById(postId, currentUser)

        result instanceof ResponseEntity
        result.statusCode == HttpStatus.NO_CONTENT
        result.body == null
    }
}

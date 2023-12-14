package com.example.demo.service

import com.example.demo.entity.Like
import com.example.demo.entity.Post
import com.example.demo.entity.User
import com.example.demo.repository.LikeRepository
import com.example.demo.repository.PostRepository
import org.junit.runner.RunWith
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import spock.lang.Ignore
import spock.lang.Specification

@RunWith(SpringRunner.class)
@WebMvcTest(value = LikeService.class)
@AutoConfigureMockMvc(addFilters = false)
class LikeServiceTest extends Specification {

    @Autowired
    LikeService likeService

    @SpringBean
    LikeRepository likeRepository = Mock()
    @SpringBean
    PostService postService = Mock()
    @SpringBean
    PostRepository postRepository = Mock()

    def "when context is loaded then all expected beans are created"() {
        expect: "the LikeService is created"
        likeService
    }

    @Ignore("Temporarily disabled due to userService mock issue. Need to fix")
    def "should add a like to a post"() {
        given:

        def postId = "post123"
        def user = new User(id: 1, fullName: "testUser")
        def post = new Post(id: postId, likes: [])
        postService.findById(postId) >> new Post()
        likeRepository.save(_) >> { Like like -> like }

        when:
        def result = likeService.likePost(postId, user)

        then:
        1 * postService.findById(postId)
        1 * likeRepository.save(_)
        1 * postRepository.save(_)

        result instanceof Like
        result.post == post
        result.user == user
        post.getLikes() == [result]
    }

    @Ignore("Temporarily disabled due to userService mock issue. Need to fix")
    def "should remove a like from a post"() {
        given:
        def postId = "post123"
        def user = new User(id: 1, fullName: "testUser")

        def existingLike = new Like(id: "like123", post: new Post(id: postId), user: user)
        def post = new Post(id: postId, likes: [existingLike])
        postService.findById(postId) >> post
        likeRepository.deleteById(existingLike.id)

        when:
        def result = likeService.likePost(postId, user)

        then:
        1 * postService.findById(postId)
        1 * likeRepository.deleteById(existingLike.id)
        1 * postRepository.save(_)

        result == null
        post.getLikes() == []
    }

    @Ignore("Temporarily disabled due to userService mock issue. Need to fix")
    def "should throw IllegalAccessException when attempting to delete another user's like"() {
        given:
        def postId = "post123"
        def currentUser = new User(id: 1, fullName: "currentUser")
        def anotherUser = new User(id: 2, fullName: "anotherUser")

        def existingLike = new Like(id: "like123", post: new Post(id: postId), user: anotherUser)
        def post = new Post(id: postId, likes: [existingLike])
        postService.findById(postId) >> post

        when:
        likeService.likePost(postId, currentUser)

        then:
        1 * postService.findById(postId)

        thrown(IllegalAccessException)
    }
}

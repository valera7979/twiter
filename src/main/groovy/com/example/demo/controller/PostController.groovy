package com.example.demo.controller

import com.example.demo.controller.dto.CommentDto
import com.example.demo.controller.dto.PostDto
import com.example.demo.controller.request.CommentRequest
import com.example.demo.entity.Post
import com.example.demo.service.PostService
import com.example.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import java.util.stream.Collectors

@RestController
@RequestMapping("/api/posts")
class PostController {

    @Autowired
    UserService userService
    @Autowired
    PostService postService

    @GetMapping("/{postId}")
    ResponseEntity<PostDto> getPost(@PathVariable("postId") String postId,
                                    @RequestHeader("Authorization") String jwt) {
        var currentUser = userService.findUserProfileByJwt(jwt)
        var post = postService.findById(postId)

        new ResponseEntity<>(PostDto.toDto(currentUser, post), HttpStatus.OK)
    }

    @PostMapping
    ResponseEntity<PostDto> createPost(@RequestBody Post post,
                                    @RequestHeader("Authorization") String jwt) {
        var currentUser = userService.findUserProfileByJwt(jwt)
        var createdPost = postService.createPost(post, currentUser)
        new ResponseEntity<>(PostDto.toDto(currentUser, createdPost), HttpStatus.CREATED)
    }

    @PutMapping("/{postId}")
    ResponseEntity<PostDto> updatePost(@PathVariable("postId") String postId,
                                        @RequestBody Post post,
                                       @RequestHeader("Authorization") String jwt) {
        var currentUser = userService.findUserProfileByJwt(jwt)
        var editedPost = postService.editPost(postId, post, currentUser)
        new ResponseEntity<>(PostDto.toDto(currentUser, editedPost), HttpStatus.ACCEPTED)
    }

    @PostMapping("/comment")
    ResponseEntity<PostDto> createComment(@RequestBody CommentRequest comment,
                                          @RequestHeader("Authorization") String jwt) {
        var currentUser = userService.findUserProfileByJwt(jwt)
        new ResponseEntity<>(PostDto.toDto(currentUser, postService.createComment(comment, currentUser)), HttpStatus.CREATED)
    }

    @DeleteMapping("/{postId}")
    ResponseEntity<Void> deletePost(@PathVariable("postId") String postId,
                                    @RequestHeader("Authorization") String jwt) {
        var currentUser = userService.findUserProfileByJwt(jwt)

        new ResponseEntity<>(postService.deletePostById(postId, currentUser), HttpStatus.NO_CONTENT)
    }

    @GetMapping("/feed")
    ResponseEntity<List<PostDto>> getFeed(@RequestHeader("Authorization") String jwt) {
        var currentUser = userService.findUserProfileByJwt(jwt)

        var posts = postService.getFeed(currentUser).stream()
            .map(post -> PostDto.toDto(currentUser, post))
            .collect(Collectors.toList())

        new ResponseEntity<>(posts, HttpStatus.OK)
    }

    @GetMapping("/feed/{userId}")
    ResponseEntity<List<PostDto>> getUserFeed(@PathVariable("userId") String userId,
            @RequestHeader("Authorization") String jwt) {
        var currentUser = userService.findUserProfileByJwt(jwt)
        var user = userService.findUserById(userId)

        var posts = postService.getFeed(user).stream()
                .map(post -> PostDto.toDto(currentUser, post))
                .collect(Collectors.toList())

        new ResponseEntity<>(posts, HttpStatus.OK)
    }

    @GetMapping("/{postId}/comments")
    ResponseEntity<List<CommentDto>> getComments(@PathVariable("postId") String postId) {
        var comments = CommentDto.toDtos(postService.getComments(postId))

        new ResponseEntity<>(comments, HttpStatus.OK)
    }

}

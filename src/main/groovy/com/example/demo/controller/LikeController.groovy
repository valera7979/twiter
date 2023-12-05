package com.example.demo.controller


import com.example.demo.controller.response.LikeResponse
import com.example.demo.entity.User
import com.example.demo.service.LikeService
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/likes")
class LikeController {

    LikeService likeService
    UserService userService

    LikeController(LikeService likeService, UserService userService) {
        this.likeService = likeService
        this.userService = userService
    }

    @PostMapping("/{postId}/like")
    ResponseEntity<LikeResponse> like(@PathVariable("postId") String postId, @RequestHeader("Authorization") String jwt) {
        User currentUser = userService.findUserProfileByJwt(jwt)
        var like = likeService.likePost(postId, currentUser)
        if (like != null) {
            return new ResponseEntity<>(new LikeResponse(LikeResponse.Action.LIKE), HttpStatus.CREATED)
        }
        new ResponseEntity<>(new LikeResponse(LikeResponse.Action.UNLIKE), HttpStatus.NO_CONTENT)
    }

}

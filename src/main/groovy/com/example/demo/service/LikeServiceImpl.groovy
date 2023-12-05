package com.example.demo.service

import com.example.demo.entity.Like
import com.example.demo.entity.Post
import com.example.demo.entity.User
import com.example.demo.exception.PostException
import com.example.demo.exception.UserException
import com.example.demo.repository.LikeRepository
import com.example.demo.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class LikeServiceImpl implements LikeService {

    LikeRepository likeRepository
    PostService postService
    PostRepository postRepository

    LikeServiceImpl(LikeRepository likeRepository, PostService postService, PostRepository postRepository) {
        this.likeRepository = likeRepository
        this.postService = postService
        this.postRepository = postRepository
    }

    @Override
//    @Transactional
    Like likePost(String postId, User user) throws UserException, PostException {
        Post post = postService.findById(postId)

        for (Like like : post.getLikes()) {
            if (like.getUser().getId() == user.getId() && like.getPost().getId() == post.getId()) {
                post.getLikes().remove(like)
                postRepository.save(post)
                likeRepository.deleteById(like.getId())
                return null
            }
        }

        Like like = new Like()
        like.setPost(post)
        like.setUser(user)

        var savedLike = likeRepository.save(like)

        post.getLikes().add(savedLike)
        postRepository.save(post)

        savedLike
    }

    @Override
    List<Like> getAllLikes(String postId) throws PostException {
        postService.findById(postId)
        likeRepository.findAllByPostId(postId)
    }
}

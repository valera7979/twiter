package com.example.demo.service

import com.example.demo.controller.request.CommentRequest
import com.example.demo.entity.Post
import com.example.demo.entity.User
import com.example.demo.exception.PostException
import com.example.demo.exception.UserException
import com.example.demo.repository.LikeRepository
import com.example.demo.repository.PostRepository

import org.springframework.stereotype.Service

import java.time.LocalDateTime

@Service
class PostServiceImpl implements PostService {

    PostRepository postRepository
    LikeRepository likeRepository

    PostServiceImpl(PostRepository postRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository
        this.likeRepository = likeRepository
    }

    @Override
    Post createPost(Post req, User user) throws UserException {
        var createdPost = new Post()
        createdPost.setContent(req.getContent())
        createdPost.setCreatedAt(LocalDateTime.now())
        createdPost.setUser(user)
        createdPost.setIsComment(false)
        createdPost.setIsPost(true)
        return postRepository.save(createdPost)
    }

    @Override
    Post findById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() ->
                        new PostException("Post with id ${id} is not found"))

    }

    @Override
    Post editPost(String postId, Post request, User user) {

        Post post = findById(postId)

        if (post.getUser().getId() != user.getId()) {
            throw new UserException("You cannot edit a post that is not yours.")
        }

        if (post.getContent() != null) {
            post.setContent(request.getContent())
            post.setEditedAt(LocalDateTime.now())
            postRepository.save(post)
        } else {
            post
        }
    }

    @Override
    void deletePostById(String id, User user) {

        Post post = findById(id)

        if (post.getUser().getId() != user.getId()) {
            throw new UserException("You cannot delete a tweet that is not yours.")
        }

        var comments = post.getComments()
        var postsToDelete = new ArrayList()
        if (comments != null) {
            postsToDelete.addAll(comments)
        }
        postsToDelete.add(post)
        var likes = post.getLikes()

        likeRepository.deleteAll(likes)

        postRepository.deleteAll(postsToDelete)
    }

    @Override
    Post createComment(CommentRequest request, User user) throws PostException {

        Post commentFor = findById(request.getPostId())

        Post comment = new Post()
        comment.setContent(request.getContent())
        comment.setCreatedAt(LocalDateTime.now())
        comment.setUser(user)
        comment.setIsComment(true)
        comment.setIsPost(false)
        comment.setCommentFor(commentFor)
        Post savedPost = postRepository.save(comment)

        commentFor.getComments().add(savedPost)
        postRepository.save(commentFor)

    }

    List<Post> getFeed(User user) {
        List<User> usersForFeed = new ArrayList<>(user.getSubscriptions())
        usersForFeed.add(user)
        postRepository.findAllByUserInAndIsPostOrderByCreatedAt(usersForFeed, true)
    }

    List<Post> getComments(String postId) {
        postRepository.findAllByCommentForIdOrderByCreatedAt(postId)
    }
}

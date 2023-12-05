package com.example.demo.service;

import com.example.demo.entity.Like;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.exception.PostException;
import com.example.demo.exception.UserException;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceMockitoTest {

    @InjectMocks
    private LikeServiceImpl likeService;

    @Mock
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private LikeRepository likeRepository;


    @Test
    public void shouldAddLikeToPost() throws PostException, UserException {
        when(postService.findById(any(String.class))).thenReturn(new Post());
        when(likeRepository.save(any(Like.class))).thenReturn(new Like());
        when(postRepository.save(any(Post.class))).thenReturn(new Post());

        var result = likeService.likePost("postId", new User());

        verify(postService).findById("postId");
        verify(likeRepository).save(any(Like.class));
        verify(postRepository).save(any(Post.class));

        assertEquals(Like.class, result.getClass());
    }

    @Test
    public void shouldRemoveLikeFromPost() throws PostException, UserException {
        var user = new User();
        user.setId("uId");
        var post = new Post();
        post.setId("pId");
        var like = new Like("lid", user, post);
        List<Like> likes = new ArrayList<>();
        likes.add(like);
        post.setLikes(likes);

        when(postService.findById(any(String.class))).thenReturn(post);

        var result = likeService.likePost("pId", user);

        verify(postService).findById("pId");
        verify(likeRepository).deleteById(any(String.class));
        verify(postRepository).save(any(Post.class));

        assertNull(result);
    }

}

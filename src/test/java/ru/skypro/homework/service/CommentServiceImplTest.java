package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.CommentServiceImpl;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CommentServiceImplTest {
    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private AdRepository adRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentMapper commentMapper;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetComments() {
        Integer adId = 1;
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comments.add(comment);
        when(commentRepository.findAllByAdId(adId)).thenReturn(comments);
        when(commentMapper.commentsToCommentsDTO(comments)).thenReturn(new ArrayList<>());
        CommentsDto result = commentService.getComments(adId);
        assertNotNull(result);
        assertEquals(1, result.getCount());
        verify(commentRepository, times(1)).findAllByAdId(adId);
    }
    @Test
    public void testSetComment() {
        Integer adId = 1;
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto();
        createOrUpdateCommentDto.setText("Test Comment");
        User user = new User();
        user.setEmail("user@example.com");
        Ad ad = new Ad();
        ad.setId(adId);
        when(adRepository.getReferenceById(adId)).thenReturn(ad);
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(commentMapper.commentToCommentDTO(any(Comment.class))).thenReturn(new CommentDto());
        CommentDto result = commentService.setComment(adId, createOrUpdateCommentDto, "user@example.com");
        assertNotNull(result);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
    @Test
    public void testDeleteComment() {
        Integer adId = 1;
        Integer commentId = 1;
        Ad ad = new Ad();
        Comment comment = new Comment();
        when(adRepository.getReferenceById(adId)).thenReturn(ad);
        when(commentRepository.getReferenceById(commentId)).thenReturn(comment);
        commentService.deleteComment(adId, commentId);
        assertFalse(ad.getComments().contains(comment)); // Проверяем, что комментарий удален из объявления
        verify(commentRepository, times(1)).deleteById(commentId);
    }
    @Test
    public void testUpdateComment() {
        Integer adId = 1;
        Integer commentId = 1;
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto();
        createOrUpdateCommentDto.setText("Updated Comment");
        Ad ad = new Ad();
        Comment comment = new Comment();
        when(adRepository.getReferenceById(adId)).thenReturn(ad);
        when(commentRepository.getReferenceById(commentId)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.commentToCommentDTO(comment)).thenReturn(new CommentDto());
        CommentDto result = commentService.updateComment(adId, commentId, createOrUpdateCommentDto);
        assertNotNull(result);
        assertEquals("Updated Comment", comment.getText());
        verify(commentRepository, times(1)).save(comment);
    }
}


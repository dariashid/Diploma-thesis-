package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentsControllerTest {
    private MockMvc mockMvc;
    @Mock
    private CommentServiceImpl commentService;
    @InjectMocks
    private CommentsController commentsController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentsController).build();
    }

    @Test
    public void testGetComment() throws Exception {
        CommentsDto commentsDto = new CommentsDto();
        when(commentService.getComments(1)).thenReturn(commentsDto);
        mockMvc.perform(get("/ads/1/comments").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$").exists());
        verify(commentService, times(1)).getComments(1);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/ads/1/comments/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(commentService, times(1)).deleteComment(1, 1);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void testUpdateComment() throws Exception {
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto();
        CommentDto commentDto = new CommentDto();
        when(commentService.updateComment(eq(1), eq(1), any(CreateOrUpdateCommentDto.class))).thenReturn(commentDto);
        mockMvc.perform(patch("/ads/1/comments/1").contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"Updated comment\"}")).andExpect(status().isOk()).andExpect(jsonPath("$").exists());
        verify(commentService, times(1)).updateComment(eq(1), eq(1), any(CreateOrUpdateCommentDto.class));
        verifyNoMoreInteractions(commentService);
    }
}

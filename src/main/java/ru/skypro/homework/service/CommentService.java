package ru.skypro.homework.service;


import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;

public interface CommentService {
    public CommentsDto getComments(Integer id);
    public CommentDto setComment(Integer adId, CreateOrUpdateCommentDto createOrUpdateCommentDto, String userName);
    public void deleteComment(Integer adId, Integer commentId);
    public CommentDto updateComment(Integer adId,
                                    Integer commentId,
                                    CreateOrUpdateCommentDto createOrUpdateCommentDto);
}

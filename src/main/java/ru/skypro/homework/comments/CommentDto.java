package ru.skypro.homework.comments;

import lombok.Data;
@Data
public class CommentDto {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Integer createdAt;
    private Integer pk;
    private String text;
}
package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.skypro.homework.comments.CommentDto;
import ru.skypro.homework.comments.CreateOrUpdateCommentDto;

import javax.xml.stream.events.Comment;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    @Mappings({
            @Mapping(target = "author", source = "author.id"),
            @Mapping(target = "authorImage", source = "author.image"),
            @Mapping(target = "authorFirstName", source = "author.firstName")
    })
    CommentDto commentToCommentDto(Comment comment);

    List<CommentDto> commentsToCommentsDto(List<Comment> comments);

    CreateOrUpdateCommentDto commentToCreateOrUpdateCommentDto(Comment comment);

    @Mapping(target = "author.id", source = "author")
    Comment commentDtoToComment(CommentDto commentDto);
}

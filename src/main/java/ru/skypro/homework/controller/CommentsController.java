package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comments.CommentsDto;

@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("ads")
public class CommentsController {
    @GetMapping("/{id}/comments")
    @Operation(
            summary = "Получение комментариев обЪявления",
            tags = "Комментарии"
    )
    public ResponseEntity getComment(@PathVariable Integer id,
                                     @RequestBody CommentsDto commentsDto) {
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{id}/comments")
    @Operation(
            summary = "Добавление комментария к объявлению",
            tags = "Комментарии"
    )
    public ResponseEntity<?> setComment(@PathVariable Integer id, CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(
            summary = "Удаление комментария",
            tags = "Комментарии"
    )
    public ResponseEntity deleteComment(@PathVariable Integer adId,
                                        @PathVariable Integer commentId) {
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation(
            summary = "Обновление комментария",
            tags = "Комментарии"
    )
    @PreAuthorize("hasRole( 'ADMIN' ) or @commentServiceImpl.find(commentId).author.username.equals(authentication.name)")
    public ResponseEntity updateComment(@PathVariable Integer adId,
                                        @PathVariable Integer commentId,
                                        @RequestBody CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        return ResponseEntity.ok().build();
    }
}
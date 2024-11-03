package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.ImageAdService;

@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
public class ImageController {
    private final AvatarService avatarService;
    private final ImageAdService imageAdService;
    @GetMapping(value = "/image-avatar/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, "image/*"})
    public byte[] getAvatar(@PathVariable Integer id) {
        log.info("Вызван метод контролера возращаюший массив байт аватара");
        return avatarService.getAvatar(id);
    }
    @GetMapping(value = "/image-ad/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/*"})
    public byte[] getImageAd(@PathVariable Integer id) {
        log.info("Вызван метод контролера возращаюший массив байт изображения объявления");
        return imageAdService.getImageAd(id);
    }

}

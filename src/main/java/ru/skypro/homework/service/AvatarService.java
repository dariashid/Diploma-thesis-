package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Avatar;

import java.io.IOException;

public interface AvatarService {
    Avatar updateImage(Authentication authentication, MultipartFile file) throws IOException;

    byte[] getAvatar(Integer id);

    default String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

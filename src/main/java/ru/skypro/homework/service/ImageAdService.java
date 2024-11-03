package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.ImageAd;

import java.io.IOException;

public interface ImageAdService {
    ImageAd updateAdImage(Integer id, MultipartFile file) throws IOException;

    byte[] getImageAd(Integer id);

    default String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exeption.AdNotFoundException;
import ru.skypro.homework.exeption.ImageAdNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.ImageAd;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageAdRepository;
import ru.skypro.homework.service.ImageAdService;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
public class ImageAdServiceImpl implements ImageAdService {
    Logger logger = LoggerFactory.getLogger(ImageAdServiceImpl.class);
    @Value("${path.to.ad-image.folder}")
    private String imageDir;
    private final ImageAdRepository imageAdRepository;

    private final AdRepository adRepository;

    public ImageAdServiceImpl(ImageAdRepository imageAdRepository, AdRepository adRepository) {
        this.imageAdRepository = imageAdRepository;
        this.adRepository = adRepository;
    }

    /**
     * Созранеет или меняет фотографию в объявлении.
     * @param id айди объявления
     * @param file файл объявления {@link MultipartFile}
     * @return Объект класса {@link ImageAd}
     * @throws IOException
     */
    @Override
    @Transactional
    public ImageAd updateAdImage(Integer id, MultipartFile file){
        logger.info("Вы вызвали метод обновления картинки у объявления");
        Ad ad = adRepository.findById(id).orElseThrow(() ->{
            log.info("Объявление не найдено", AdNotFoundException.class);
            return new AdNotFoundException();
        });
        ImageAd image = imageAdRepository.findImageAdByAdId(id).orElse(new ImageAd());
        String extension = "." + getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Path.of(imageDir, id + extension);
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (
                    InputStream is = file.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
            ) {
                bis.transferTo(bos);
            }

            image.setAd(ad);
            image.setFilePath(filePath.toString().replace(extension, "").replace("\\", "/"));
            image.setFileSize(file.getSize());
            image.setMediaType(file.getContentType());
            image.setData(file.getBytes());

        } catch (IOException e) {
            log.info("Ошибка ввода-вывода изображения объявления: " + e.getMessage());
            throw new RuntimeException();
        }
        imageAdRepository.save(image);
        ad.setImage(image);
        adRepository.save(ad);
        logger.info("Вы успешно обновили картинку в объявлении");
        return image;
    }

    /**
     * Возращает картинку объявления в виде массива байт
     * @param id айди объявления
     * @return массив байт
     */
    @Override
    public byte[] getImageAd(Integer id) {
        return imageAdRepository
                .findImageAdByAdId(id)
                .orElseThrow(() -> {
                    log.info("Изображение объявления не найдено", ImageAdNotFoundException.class);
                    return new ImageAdNotFoundException();
                })
                .getData();
    }


}

package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exeption.ImageAdNotFoundException;
import ru.skypro.homework.exeption.UserNotFoundException;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.model.ImageAd;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AvatarService;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
public class AvatarServiceImpl implements AvatarService {
    Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);
    @Value("${path.to.image-avatar.folder}")
    private String imageDir;
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;

    public AvatarServiceImpl(AvatarRepository avatarRepository, UserRepository userRepository) {
        this.avatarRepository = avatarRepository;
        this.userRepository = userRepository;
    }


    /**
     * Созранеет или меняет аватарку у пользователя.
     * @param file файл фотографии {@link MultipartFile}
     * @return Объект класса {@link ImageAd}
     * @throws IOException
     */
    @Override
    @Transactional
    public Avatar updateImage(Authentication authentication, MultipartFile file)  {
        logger.info("Вы вызвали метод по изменению аватар");
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() ->{
            log.info("Пользователь не найден", UserNotFoundException.class);
            return new UserNotFoundException();
        });
        Avatar avatar = avatarRepository.findImageByUserId(user.getId()).orElse(new Avatar());
        String extension = "." + getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Path.of(imageDir, user.getId() + extension);
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
            avatar.setUser(user);
            avatar.setFilePath(filePath.toString().replace(extension, ""));
            avatar.setFileSize(file.getSize());
            avatar.setMediaType(file.getContentType());
            avatar.setData(file.getBytes());
        } catch (IOException e) {
            log.info("Ошибка ввода-вывода аватара пользователя" + e.getMessage());
            throw new RuntimeException();
        }
        avatarRepository.save(avatar);
        user.setAvatar(avatar);
        userRepository.save(user);
        logger.info("Вы успешно изменили аватар");
        return avatar;
    }

    /**
     * Возращает аватар в виде массива байт.
     * @param id айди пользователя
     * @return массив байт
     */
    @Override
    public byte[] getAvatar(Integer id) {
        return avatarRepository
                .findImageByUserId(id)
                .orElseThrow(() -> {
                    log.info("Изображение объявления не найдено", ImageAdNotFoundException.class);
                    return new ImageAdNotFoundException();
                })
                .getData();
    }

}

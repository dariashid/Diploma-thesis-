package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.exeption.AdNotFoundException;
import ru.skypro.homework.exeption.UserNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageAdService;

import java.io.IOException;
import java.util.List;

/**
 * Сервис для работы с объявлениями.
 */
@Slf4j
@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserRepository userRepository;

    private final ImageAdService imageAdService;

    public AdServiceImpl(AdRepository adRepository,
                         AdMapper adMapper,
                         UserRepository userRepository, ImageAdService imageAdService) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userRepository = userRepository;
        this.imageAdService = imageAdService;
    }

    /**
     * Сохранение объявления.
     * @param adDTO DTO объявления
     * @param file Файл фотографии объявления
     * @param userName Имя пользователя или емейл
     * @return объект DTO {@link AdDto}
     */
    @Override
    public AdDto saveAd(AdDto adDTO, MultipartFile file, String userName) throws IOException {
        Ad ad = adRepository.save(adMapper.adDTOtoAd(adDTO));
        ad.setAuthor(userRepository.findByEmail(userName).orElseThrow(() ->{
            log.info("Пользователь не найден", UserNotFoundException.class);
            return new UserNotFoundException();
        }));
        ad.setImage(imageAdService.updateAdImage(ad.getId(), file));
        adRepository.save(ad);
        log.info("Вы успешно создали объявление");
        return adMapper.adToAdDTO(ad);
    }

    /**
     * Возращает список всех объявлений.
     * @return объект класса {@link AdsDto}
     */
    @Override
    public AdsDto getAllAds() {
        log.info("Вы вызвали метод получения всех объявлений");
        List<AdDto> listAdDTO = adMapper.toListAdDTO(adRepository.findAll());
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(listAdDTO.size());
        adsDto.setResults(listAdDTO);
        return adsDto;
    }

    /**
     * Возращает расширенную информацию по объявлению
     * @param id айди объявления
     * @return объект {@link ExtendedAdDto}
     */
    @Override
    public ExtendedAdDto getAdInfo(Integer id) {
        log.info("Вы вызвали метод получения информации об объявлении");
        Ad ad = adRepository.findById(id).orElseThrow(() ->{
            log.info("Объявление не найдено", AdNotFoundException.class);
            return new AdNotFoundException();
        });
        return adMapper.adToExtendedAd(ad);
    }

    /**
     * Удаляет объяевление.
     * @param id айди объявления
     */
    @Override
    public void deleteAd(Integer id) {
        adRepository.deleteById(id);
        log.info("Вы успешно удалили объявление");
    }

    /**
     * Редактирует информацию в объявлении.
     * @param id айди объявления
     * @param createOrUpdateAdDto обновленная информация
     * @return объект {@link AdDto}
     */
    @Override
    public AdDto updateInfoAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto) {
        Ad ad = adRepository.findById(id).orElseThrow(() ->{
            log.info("Объявление не найдено", AdNotFoundException.class);
            return new AdNotFoundException();
        });
        ad.setTitle(createOrUpdateAdDto.getTitle());
        ad.setPrice(createOrUpdateAdDto.getPrice());
        ad.setDescription(createOrUpdateAdDto.getDescription());
        log.info("Вы успешно изменили информацию в объявлении");
        return adMapper.adToAdDTO(adRepository.save(ad));
    }

    /**
     * Возращает список объявлений пользователя.
     * @return список объявлений {@link AdsDto}
     */
    @Override
    public AdsDto findMyAds() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Ad> ads = adRepository.findByAuthorEmail(auth.getName());
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(ads.size());
        adsDto.setResults(adMapper.toListAdDTO(ads));
        log.info("Вы нашли свои объявления");
        return adsDto;
    }


}


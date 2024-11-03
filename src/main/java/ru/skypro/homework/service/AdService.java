package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;

import java.io.IOException;

public interface AdService {
    AdDto saveAd(AdDto adDTO, MultipartFile file, String userName) throws IOException;

    AdsDto getAllAds();

    ExtendedAdDto getAdInfo(Integer id);

    void deleteAd(Integer id);

    AdDto updateInfoAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto);

    AdsDto findMyAds();


}

package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.model.Ad;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdMapper {
    @Mappings({
            @Mapping(target = "author", source = "author.id"),
            @Mapping(target = "pk", source = "id"),
            @Mapping(target = "image", source = "image.filePath")
    })
    AdDto adToAdDTO(Ad ad);

    List<AdDto> toListAdDTO(List<Ad> ads);

    @Mappings({
            @Mapping(target = "pk", source = "id"),
            @Mapping(target = "authorFirstName", source = "author.firstName"),
            @Mapping(target = "authorLastName", source = "author.lastName"),
            @Mapping(target = "phone", source = "author.phone"),
            @Mapping(target = "email", source = "author.email"),
            @Mapping(target = "image", source = "image.filePath")
    })
    ExtendedAdDto adToExtendedAd(Ad ad);
    @Mappings({
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "id", source = "pk"),
            @Mapping(target = "image", ignore = true)
    })
    Ad adDTOtoAd(AdDto adDTO);
}
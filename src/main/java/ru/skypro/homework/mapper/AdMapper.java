package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdMapper {
    @Mapping(target = "author", source = "author.id")
    AdDto adToAdDTO(Ad ad);
    List<AdDto> toListAdDTO(List<Ad> ads);
    CreateOrUpdateAdDto adToCreateOrUpdateAd(Ad ad);
    @Mappings({
            @Mapping(target = "authorFirstName", source = "author.firstName"),
            @Mapping(target = "authorLastName", source = "author.lastName"),
            @Mapping(target = "phone", source = "author.phone"),
            @Mapping(target = "email", source = "author.email")
    })
    ExtendedAdDto adToExtendedAd(Ad ad);
    @Mapping(target = "author.id", source = "author")
    Ad adDTOtoAd(AdDto adDTO);
}

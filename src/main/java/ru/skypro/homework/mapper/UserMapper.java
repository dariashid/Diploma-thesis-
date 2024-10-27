package ru.skypro.homework.mapper;

import org.apache.catalina.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.dto.users.GetUserInfoDto;
import ru.skypro.homework.dto.users.UpdateUserDto;
import ru.skypro.homework.dto.users.UpdateUserImageDto;
import ru.skypro.homework.dto.users.UserSetPasswordDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UpdateUserDto UserToUpdateUserDto(User user);

    User UpdateUserDtoToUser(UpdateUserDto updateUserDto);

    User UserSetPasswordDtoToUser(UserSetPasswordDto userSetPasswordDto);

    UserSetPasswordDto UserToUserSetPasswordDto(User user);

    UpdateUserImageDto UserToUpdateUserImageDto(User user);

    User UpdateUserImageDtoToUser(UpdateUserImageDto updateUserImageDto);

    GetUserInfoDto UserToGetUserInfo(User user);

    User GetUserInfoToUser(GetUserInfoDto getUserInfoDto);
}
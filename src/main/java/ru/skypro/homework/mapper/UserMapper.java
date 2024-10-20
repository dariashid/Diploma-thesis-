package ru.skypro.homework.mapper;

import org.apache.catalina.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.dto.users.UpdateUserDto;
import ru.skypro.homework.dto.users.UpdateUserImageDto;
import ru.skypro.homework.dto.users.UserSetPasswordDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UpdateUserDto UpdateUserDtoToUser(User user);
    UpdateUserImageDto UpdateUserImageDtoToUser(User user);
    UserSetPasswordDto UserSetPasswordDtoToUser(User user);
    User UserToUpdateUserDto(UpdateUserDto updateUserDto);
    User UserToUpdateUserImageDto(UpdateUserImageDto updateUserImageDto);
    User UserToUserSetPasswordDto(UserSetPasswordDto userSetPasswordDto);
}

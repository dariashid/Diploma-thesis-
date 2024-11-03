package ru.skypro.homework.service;

import ru.skypro.homework.dto.user.GetUserInfoDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserSetPasswordDto;


public interface UserService {
    public void changeToPassword(UserSetPasswordDto userSetPasswordDto);
    public GetUserInfoDto infoAboutUser(String name);
    public UpdateUserDto updateUser(UpdateUserDto updateUserDto, String email);

}

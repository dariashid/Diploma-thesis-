package ru.skypro.homework.dto.user;

import lombok.Data;

@Data
public class UserSetPasswordDto {
    private String currentPassword;
    private String newPassword;
}

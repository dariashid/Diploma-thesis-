package ru.skypro.homework.dto.user;

import lombok.Data;
import ru.skypro.homework.dto.RoleDto;

@Data
public class GetUserInfoDto {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private RoleDto role;
    private String image;
}

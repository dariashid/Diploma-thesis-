package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.GetUserInfoDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserSetPasswordDto;
import ru.skypro.homework.service.impl.AvatarServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;

@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl usersService;
    private final AvatarServiceImpl avatarService;
    @PostMapping("/set_password")
    @PreAuthorize("hasRole('ADMIN') or @userService.isUserName(principal.username, #email)")
    @Operation(summary = "Обновление пароля")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public void setPassword(@RequestBody UserSetPasswordDto userSetPasswordDto) {
        usersService.changeToPassword(userSetPasswordDto);
    }
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @GetMapping("/me")
    public ResponseEntity<GetUserInfoDto> infoAboutUser(Authentication authentication) {
        log.info("Вызван метод контролера получение информации о пользователе");
        GetUserInfoDto getUserInfoDto = usersService.infoAboutUser(authentication.getName());
        return ResponseEntity.ok(getUserInfoDto);
    }
    @Operation(summary = "Обновление пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/me")
    @PreAuthorize("hasRole('ADMIN') or @userService.isUserName(principal.username, #email)")
    public ResponseEntity<UpdateUserDto> updateUser (@RequestBody UpdateUserDto updateUserDto, Authentication authentication){
        log.info("Вызван метод контролера обновление пользователя");
        UpdateUserDto updateUserDto1 = usersService.updateUser(updateUserDto, authentication.getName());
        return ResponseEntity.ok(updateUserDto1);

    }

    @PatchMapping("/me/image")
    @PreAuthorize("hasRole('ADMIN') or @userService.isUserName(principal.username, #email)")
    @Operation(summary = "Обновление аватара авторизованного пользователя.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public void updateImage (Authentication authentication, @RequestBody MultipartFile image) throws IOException {
        log.info("Вызван метод контролера обновление аватара пользователя");
        avatarService.updateImage(authentication, image);
    }
}
package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.users.UpdateUserDto;
import ru.skypro.homework.dto.users.UserSetPasswordDto;

@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    // private final UsersService usersService;
    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<?> setPassword(@RequestBody UserSetPasswordDto userSetPasswordDto) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
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
    public ResponseEntity getUser() {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public ResponseEntity updateUser(@RequestBody UpdateUserDto updateUserDto) {
        if (updateUserDto != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //@PatchMapping("/me/image")
        // @Operation(summary = "Обновление аватара авторизованного пользователя")
        // @ApiResponses({
        //        @ApiResponse(responseCode = "200", description = "OK"),
        //        @ApiResponse(responseCode = "401", description = "Unauthorized")
        //})
        //public ResponseEntity updateImage (@RequestBody UpdateUserImageDto updateUserImageDto){
        //    return ResponseEntity.ok().build();
    }
}

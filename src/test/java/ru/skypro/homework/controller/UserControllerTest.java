package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.GetUserInfoDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserSetPasswordDto;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.service.impl.UserServiceImpl;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {
    private MockMvc mockMvc;
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private UserController userController;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    @Test
    public void testSetPassword() throws Exception {
        UserSetPasswordDto userSetPasswordDto = new UserSetPasswordDto();
        userSetPasswordDto.setNewPassword("newPassword");
        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"newPassword\"}"))
                .andExpect(status().isOk());
        verify(userService, times(1)).changeToPassword(any(UserSetPasswordDto.class));
        verifyNoMoreInteractions(userService);
    }
    @Test
    public void testInfoAboutUser() throws Exception {
        GetUserInfoDto getUserInfoDto = new GetUserInfoDto();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user");
        when(userService.infoAboutUser("user")).thenReturn(getUserInfoDto);
        mockMvc.perform(get("/users/me")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        verify(userService, times(1)).infoAboutUser("user");
        verifyNoMoreInteractions(userService);
    }
    @Test
    public void testUpdateUser() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        UpdateUserDto updatedUserDto = new UpdateUserDto();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user");
        when(userService.updateUser(any(UpdateUserDto.class), eq("user"))).thenReturn(updatedUserDto);
        mockMvc.perform(patch("/users/me")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated User\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        verify(userService, times(1)).updateUser(any(UpdateUserDto.class), eq("user"));
        verifyNoMoreInteractions(userService);
    }
    @Test
    public void testUpdateImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile("multipartFile", "image.jpg", "image/jpeg", "image content".getBytes());
        Avatar avatar = new Avatar();
        avatar.setData("image content".getBytes());
        avatar.setMediaType("image/jpeg");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user");
        when(userService.updateImage(eq(authentication), any(MultipartFile.class))).thenReturn(avatar);
        mockMvc.perform(multipart("/users/me/image")
                        .file(image)
                        //.principal(authentication)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/jpeg"))
                .andExpect(content().bytes("image content".getBytes()));
        verify(userService, times(1)).updateImage(eq(authentication), any(MultipartFile.class));
        verifyNoMoreInteractions(userService);
    }
}
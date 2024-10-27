package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.GetUserInfoDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserSetPasswordDto;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.MyUserDetailsService;
import ru.skypro.homework.service.impl.UserServiceImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private MyUserDetailsService userDetailsService;
    @Mock
    private AvatarRepository avatarRepository;
    @Mock
    private Authentication authentication;
    private User user;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("123456789");
    }
    @Test
    public void testChangeToPassword() {
        UserSetPasswordDto userSetPasswordDto = new UserSetPasswordDto();
        userSetPasswordDto.setCurrentPassword("oldPassword");
        userSetPasswordDto.setNewPassword("newPassword");
        doNothing().when(userDetailsService).changePassword("oldPassword", "newPassword");
        userService.changeToPassword(userSetPasswordDto);
        verify(userDetailsService, times(1)).changePassword("oldPassword", "newPassword");
    }
    @Test
    public void testInfoAboutUser() {
        GetUserInfoDto userInfoDto = new GetUserInfoDto();
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(userMapper.UserToGetUserInfo(user)).thenReturn(userInfoDto);
        GetUserInfoDto result = userService.infoAboutUser("user@example.com");
        assertNotNull(result);
        assertEquals(userInfoDto, result);
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }
    @Test
    public void testUpdateUser() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("Jane");
        updateUserDto.setLastName("Smith");
        updateUserDto.setPhone("987654321");
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(userMapper.UserToUpdateUserDto(user)).thenReturn(updateUserDto);
        UpdateUserDto result = userService.updateUser(updateUserDto, "user@example.com");
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("987654321", user.getPhone());
        verify(userRepository, times(1)).save(user);
    }
    @Test
    public void testUpdateImage() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("avatar.png");
        when(file.getInputStream()).thenReturn(Files.newInputStream(Path.of("src/test/resources/test-image.png")));
        when(file.getSize()).thenReturn(1024L);
        when(file.getContentType()).thenReturn("image/png");
        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        Avatar avatar = new Avatar();
        when(avatarRepository.findImageByUserId(user.getId())).thenReturn(avatar);
        Avatar result = userService.updateImage(authentication, file);
        assertNotNull(result);
        assertEquals(file.getSize(), result.getFileSize());
        assertEquals(file.getContentType(), result.getMediaType());
        verify(userRepository, times(1)).save(user);
    }
    @Test
    public void testGetExtension() {
        String fileName = "avatar.png";
        String extension = userService.getExtension(fileName);
        assertEquals("png", extension);
    }
}


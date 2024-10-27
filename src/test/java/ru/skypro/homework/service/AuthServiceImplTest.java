package ru.skypro.homework.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.impl.AuthServiceImpl;
import ru.skypro.homework.service.impl.MyUserDetailsService;
public class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private MyUserDetailsService manager;
    @Mock
    private PasswordEncoder encoder;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testLogin_Success() {
        String username = "testUser";
        String password = "password";
        String encodedPassword = "encodedPassword";
        UserDetails userDetails = User.builder()
                .username(username)
                .password(encodedPassword)
                .roles("USER")
                .build();
        when(manager.userExists(username)).thenReturn(true);
        when(manager.loadUserByUsername(username)).thenReturn(userDetails);
        when(encoder.matches(password, encodedPassword)).thenReturn(true);
        boolean result = authService.login(username, password);
        assertTrue(result);
        verify(manager, times(1)).userExists(username);
        verify(manager, times(1)).loadUserByUsername(username);
        verify(encoder, times(1)).matches(password, encodedPassword);
    }
    @Test
    public void testLogin_UserDoesNotExist() {
        String username = "nonExistentUser";
        String password = "password";
        when(manager.userExists(username)).thenReturn(false);
        boolean result = authService.login(username, password);
        assertFalse(result);
        verify(manager, times(1)).userExists(username);
        verify(manager, never()).loadUserByUsername(username);
        verify(encoder, never()).matches(anyString(), anyString());
    }
    @Test
    public void testLogin_InvalidPassword() {
        String username = "testUser";
        String password = "wrongPassword";
        String encodedPassword = "encodedPassword";
        UserDetails userDetails = User.builder()
                .username(username)
                .password(encodedPassword)
                .roles("USER")
                .build();
        when(manager.userExists(username)).thenReturn(true);
        when(manager.loadUserByUsername(username)).thenReturn(userDetails);
        when(encoder.matches(password, encodedPassword)).thenReturn(false);
        boolean result = authService.login(username, password);
        assertFalse(result);
        verify(manager, times(1)).userExists(username);
        verify(manager, times(1)).loadUserByUsername(username);
        verify(encoder, times(1)).matches(password, encodedPassword);
    }
    @Test
    public void testRegister_UserAlreadyExists() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("existingUser");
        registerDto.setPassword("password");
        when(manager.userExists(registerDto.getUsername())).thenReturn(true);
        boolean result = authService.register(registerDto);
        assertFalse(result);
        verify(manager, times(1)).userExists(registerDto.getUsername());
        verify(manager, never()).createUser(any(User.class));
    }
}


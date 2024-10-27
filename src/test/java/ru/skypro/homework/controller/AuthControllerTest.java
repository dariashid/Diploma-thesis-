package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.AuthService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("user");
        loginDto.setPassword("password");
        when(authService.login(loginDto.getUsername(), loginDto.getPassword())).thenReturn(true);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\", \"password\":\"password\"}"))
                .andExpect(status().isOk());
        verify(authService, times(1)).login(loginDto.getUsername(), loginDto.getPassword());
        verifyNoMoreInteractions(authService);
    }

    @Test
    public void testLoginFailure() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("user");
        loginDto.setPassword("wrongpassword");
        when(authService.login(loginDto.getUsername(), loginDto.getPassword())).thenReturn(false);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\", \"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());
        verify(authService, times(1)).login(loginDto.getUsername(), loginDto.getPassword());
        verifyNoMoreInteractions(authService);
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("newuser");
        registerDto.setPassword("password");
        when(authService.register(registerDto)).thenReturn(true);
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newuser\", \"password\":\"password\", \"email\":\"newuser@example.com\"}"))
                .andExpect(status().isCreated());
        verify(authService, times(1)).register(registerDto);
        verifyNoMoreInteractions(authService);
    }

    @Test
    public void testRegisterFailure() throws Exception {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("newuser");
        registerDto.setPassword("password");
        when(authService.register(registerDto)).thenReturn(false);
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newuser\", \"password\":\"password\", \"email\":\"newuser@example.com\"}"))
                .andExpect(status().isBadRequest());
        verify(authService, times(1)).register(registerDto);
        verifyNoMoreInteractions(authService);
    }
}

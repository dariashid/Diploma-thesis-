package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.impl.MyUserPrincipal;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;
public class MyUserPrincipalTest {
    private User user;
    private MyUserPrincipal userPrincipal;
    @BeforeEach
    public void setUp() {
        // Создаем объект User для тестирования
        user = new User();
        user.setEmail("user@example.com");
        user.setCurrentPassword("password");
        user.setRole(Role.USER); // Предполагаем, что у вас есть перечисление Role
        // Создаем экземпляр MyUserPrincipal
        userPrincipal = new MyUserPrincipal(user);
    }
    @Test
    public void testGetUsername() {
        assertEquals("user@example.com", userPrincipal.getUsername(), "Username should match the user's email");
    }
    @Test
    public void testGetPassword() {
        assertEquals("password", userPrincipal.getPassword(), "Password should match the user's current password");
    }
    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
        assertNotNull(authorities, "Authorities should not be null");
        assertEquals(1, authorities.size(), "There should be one authority");
        assertTrue(authorities.contains(new SimpleGrantedAuthority(Role.USER.toString())), "Authorities should contain the user's role");
    }
    @Test
    public void testIsAccountNonExpired() {
        assertTrue(userPrincipal.isAccountNonExpired(), "Account should not be expired");
    }
    @Test
    public void testIsAccountNonLocked() {
        assertTrue(userPrincipal.isAccountNonLocked(), "Account should not be locked");
    }
    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(userPrincipal.isCredentialsNonExpired(), "Credentials should not be expired");
    }
    @Test
    public void testIsEnabled() {
        assertTrue(userPrincipal.isEnabled(), "Account should be enabled");
    }
}

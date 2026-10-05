package com.example.expenseTracker.service;

import com.example.expenseTracker.dto.auth.AuthResponse;
import com.example.expenseTracker.dto.auth.LoginRequest;
import com.example.expenseTracker.dto.auth.RegisterRequest;
import com.example.expenseTracker.entity.User;
import com.example.expenseTracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerStoresEncodedPasswordAndReturnsBearerToken() {
        RegisterRequest request = new RegisterRequest("A User", "user@example.com", "password123");
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(jwtService.generateToken(request.email())).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("encoded-password", userCaptor.getValue().getPasswordHash());
        assertEquals(new AuthResponse("jwt-token", "Bearer"), response);
    }

    @Test
    void loginReturnsTokenForMatchingPassword() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPasswordHash("encoded-password");
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles("USER")
                .build();
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches("password123", "encoded-password")).thenReturn(true);
        when(jwtService.generateToken(user.getEmail())).thenReturn("jwt-token");

        AuthResponse response = authService.login(new LoginRequest(user.getEmail(), "password123"));

        assertEquals(new AuthResponse("jwt-token", "Bearer"), response);
    }
}

package com.example.demo.Test;

import com.example.demo.config.JwtUtils;
import com.example.demo.controllers.AuthenticationController;
import com.example.demo.dao.User;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.service.TokenServiceImpl;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDao userDao;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private TokenServiceImpl tokenService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateSuccess() {
        AuthenticationRequest request = new AuthenticationRequest();

        request.setEmail("admin@gmail.com");
        request.setPassword("Password");

        UserDetails mockUserDetails = mock(UserDetails.class);

        User mockUser = new User(mockUserDetails.getUsername(), mockUserDetails.getPassword());

        ResponseEntity<Map<String, Object>> response = authenticationController.authenticate(request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("token"));
        assertEquals("dummyToken", response.getBody().get("token"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).saveToken("test@example.com", "dummyToken");
    }

    @Test
    void testAuthenticateUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest();

        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userDao.findUserByEmail(request.getEmail())).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = authenticationController.authenticate(request);

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("User not found", response.getBody().get("error"));
    }

    @Test
    void testAuthenticateInvalidCredentials() {
        AuthenticationRequest request = new AuthenticationRequest();

        request.setEmail("test@example.com");
        request.setPassword("password");

        doThrow(new RuntimeException("Invalid credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<Map<String, Object>> response = authenticationController.authenticate(request);

        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Invalid email or password", response.getBody().get("error"));
    }

    @Test
    void testRegisterSuccess() {
        com.example.demo.dao.User user = new com.example.demo.dao.User();
        user.setUsername("test@example.com");
        user.setPassword("password");

        com.example.demo.dao.User savedUser = new com.example.demo.dao.User();
        savedUser.setUsername("test@example.com");
        savedUser.setPassword("password");


        when(userService.registerUser(user)).thenReturn(savedUser);

        ResponseEntity<Map<String, Object>> response = authenticationController.register(user);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("message"));
        assertTrue(response.getBody().containsKey("user"));
        assertEquals("User registered successfully", response.getBody().get("message"));
    }
    /*
    @Test
    void testLogoutSuccess() {
        String token = "Bearer dummyToken";
        String username = "test@example.com";

        when(jwtUtils.extractUsername("dummyToken")).thenReturn(username);
        when(tokenService.findTokenByUser(username)).thenReturn("dummyToken");

        ResponseEntity<Map<String, String>> response = authenticationController.logout(token);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged out successfully", response.getBody().get("message"));
        verify(tokenService).deleteTokenByUser(username);
    }
    */
    @Test
    void testLogoutInvalidToken() {
        String token = "Bearer invalidToken";

        doThrow(new RuntimeException("Invalid token"))
                .when(jwtUtils)
                .extractUsername("invalidToken");

        ResponseEntity<Map<String, String>> response = authenticationController.logout(token);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid token", response.getBody().get("error"));
    }
}

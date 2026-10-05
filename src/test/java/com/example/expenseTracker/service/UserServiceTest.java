package com.example.expenseTracker.service;

import com.example.expenseTracker.dto.user.UserRequestDTO;
import com.example.expenseTracker.dto.user.UserResponseDTO;
import com.example.expenseTracker.entity.User;
import com.example.expenseTracker.exception.DuplicateResourceException;
import com.example.expenseTracker.mapper.UserMapper;
import com.example.expenseTracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldCreateSuccessfully(){

        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName("Shiva");
        requestDTO.setEmail("shiva123@gmail.com");

        User user = new User();
        user.setName("Shiva");
        user.setEmail("shiva123@gmail.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Shiva");
        savedUser.setEmail("shiva123@gmail.com");

        UserResponseDTO responseDTO = new UserResponseDTO();

        when(userRepository.existsByEmail("shiva123@gmail.com"))
                .thenReturn(false);

        when(userMapper.toEntity(requestDTO))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenReturn(savedUser);
        when(userMapper.toResponse(savedUser))
                .thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO, result);


    }

    @Test
    void createUser_throwExceptionWhenExists(){

        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName("Shiva");
        requestDTO.setEmail("shiva123@gmail.com");

        when(userRepository.existsByEmail("shiva123@gmail.com"))
                .thenReturn(true);

        DuplicateResourceException exception
                = assertThrows(DuplicateResourceException.class,() ->
                 userService.createUser(requestDTO));

        assertTrue(exception.getMessage()
                .contains("Email already exists"));


        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toEntity(any());

    }

    @Test
    void getUserById_returnSuccessfully(){

    }

    @Test
    void deleteUser_Successfully(){

    }

}
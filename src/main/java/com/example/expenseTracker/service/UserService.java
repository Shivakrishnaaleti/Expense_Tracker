package com.example.expenseTracker.service;


import com.example.expenseTracker.exception.DuplicateResourceException;
import com.example.expenseTracker.exception.ResourceNotFoundException;
import com.example.expenseTracker.dto.user.UserRequestDTO;
import com.example.expenseTracker.dto.user.UserResponseDTO;
import com.example.expenseTracker.entity.User;
import com.example.expenseTracker.mapper.UserMapper;
import com.example.expenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserRequestDTO request){

        boolean emailExists = userRepository.existsByEmail(request.getEmail());

        if(emailExists){
            throw new DuplicateResourceException("Email already exists with "+request.getEmail());
        }
        User user = userMapper.toEntity(request);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);

    }

    public UserResponseDTO getUserById(Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : "+id));

        return userMapper.toResponse(user);

    }

    public Page<UserResponseDTO> getAllUsers(Pageable pageable){

        Page<User> users = userRepository.findAll(pageable);

        return users.map(userMapper::toResponse);

    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO request){

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " +id));

        if(!existingUser.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())){

            throw new DuplicateResourceException("Email already exists with "+request.getEmail());

        }

        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());

        User updatedUser = userRepository.save(existingUser);

        return userMapper.toResponse(updatedUser);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id "+id));

        userRepository.delete(user);
    }
}

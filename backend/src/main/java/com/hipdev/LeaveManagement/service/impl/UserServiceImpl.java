package com.hipdev.LeaveManagement.service.impl;

import com.hipdev.LeaveManagement.dto.UserDTO;
import com.hipdev.LeaveManagement.dto.response.ApiResponse;
import com.hipdev.LeaveManagement.entity.User;
import com.hipdev.LeaveManagement.exception.AppException;
import com.hipdev.LeaveManagement.exception.ErrorCode;
import com.hipdev.LeaveManagement.mapper.UserMapper;
import com.hipdev.LeaveManagement.repository.RoleRepository;
import com.hipdev.LeaveManagement.repository.UserRepository;
import com.hipdev.LeaveManagement.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = userMapper.toDtos(users);
        return userDTOS;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        var existedUser = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return userMapper.toDto(existedUser);
    }

    @Override
    public UserDTO getMyInfo(Long userId) {
        var existedUser = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return userMapper.toDto(existedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Long userId) {
        // Check if the user exists by ID
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // Get the existing user entity
        User user = optionalUser.get();
        userMapper.partialUpdate(user, userDTO);

//        // If password is provided, encode and set it
//        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
//            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//        }

        // Save the updated user
        userRepository.save(user);

        // Return the updated user DTO
        return userMapper.toDto(user);
    }

    @Override
    public Void deleteUser(Long userId) {
        // Check if the user exists by ID
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        // Delete the user
        userRepository.delete(user);
        return null;  // Return null as the return type is Void
    }
}

package com.hotel.auth_service.service.impl;

import com.hotel.auth_service.criteria.UserSearchCriteria;
import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.entity.Status;
import com.hotel.auth_service.entity.User;
import com.hotel.auth_service.mapper.UserMapper;
import com.hotel.auth_service.repository.UserRepository;
import com.hotel.auth_service.service.UserService;
import com.hotel.auth_service.spesification.UserSpesification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable, UserSearchCriteria criteria) {
        UserSpesification userSpesification = new UserSpesification(criteria);
        return userRepository.findAll(userSpesification, pageable).map(userMapper::toUserDto);
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toUserDto);
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalStateException("User with email " + userDto.getEmail() + " already exists");
        }

        User user = userMapper.toUserEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public Optional<UserDto> updateUser(String email, UserDto userDto) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {

            if (userDto.getPhoto() == null ) {
                userDto.setPhoto("https://ui-avatars.com/api/?name=" + userDto.getFullName().replace(" ", "+"));
            }

            User user = userMapper.toUserEntity(userDto);
            user.setEmail(existingUser.get().getEmail());
            user = userRepository.save(user);
            return Optional.of(userMapper.toUserDto(user));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDto> changeUserPassword(String email, String password) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setPassword(passwordEncoder.encode(password));
            user = userRepository.save(user);
            return Optional.of(userMapper.toUserDto(user));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDto> toggleUserStatus(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setStatus(user.getStatus() == Status.ACTIVE ? Status.INACTIVE : Status.ACTIVE);
            user = userRepository.save(user);
            return Optional.of(userMapper.toUserDto(user));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteUser(String email) {
        userRepository.deleteById(email);
    }
}

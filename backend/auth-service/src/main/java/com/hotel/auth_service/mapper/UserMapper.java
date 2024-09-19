package com.hotel.auth_service.mapper;

import com.hotel.auth_service.dto.UserDto;
import com.hotel.auth_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    User toUserEntity(UserDto userDto);
}

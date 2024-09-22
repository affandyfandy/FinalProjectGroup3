package com.hotel.room_service.dto;

import java.util.List;

import org.mapstruct.Mapper;

import com.hotel.room_service.dto.request.CreateRoomDto;
import com.hotel.room_service.dto.response.ReadRoomDto;
import com.hotel.room_service.entity.Room;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toEntity(CreateRoomDto dto);
    ReadRoomDto toDto(Room room);
    List<ReadRoomDto> toListDto(List<Room> listRoom);
}

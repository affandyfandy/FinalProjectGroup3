package com.hotel.room_service.dto;

import java.util.List;

import org.mapstruct.Mapper;

import com.hotel.room_service.data.model.Room;
import com.hotel.room_service.dto.request.CreateRoomDto;
import com.hotel.room_service.dto.response.ReadRoomDto;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toEntity(CreateRoomDto dto);
    ReadRoomDto toDto(Room room);
    List<ReadRoomDto> toListDto(List<Room> listRoom);
}

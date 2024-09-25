package com.hotel.room_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hotel.room_service.entity.Room;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE 'ACTIVE'")
    Page<Room> findAllActiveRooms(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE 'ACTIVE' AND capacity >= :capacity")
    Page<Room> findAllActiveRoomsAndCapacityGreaterThanEqual(int capacity, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status")
    Page<Room> findAllByStatus(@Param("status") String status, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND facility LIKE :facility")
    Page<Room> findAllByStatusAndFacility(@Param("status") String status, @Param("facility") String facility, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND room_type LIKE :roomType")
    Page<Room> findAllByStatusAndRoomType(@Param("status") String status, @Param("roomType") String roomType, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND capacity = :capacity")
    Page<Room> findAllByStatusAndCapacity(@Param("status") String status, @Param("capacity") Integer capacity, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND price BETWEEN :lowerLimit AND :upperLimit")
    Page<Room> findAllByStatusAndPriceRange(@Param("status") String status, @Param("lowerLimit") BigDecimal lowerLimit, 
                                            @Param("upperLimit") BigDecimal upperLimit, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND facility LIKE :facility AND room_type LIKE :roomType")
    Page<Room> findAllByStatusAndFacilityAndRoomType(@Param("status") String status, @Param("facility") String facility, 
                                                     @Param("roomType") String roomType, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND facility LIKE :facility AND capacity = :capacity")
    Page<Room> findAllByStatusAndFacilityAndCapacity(@Param("status") String status, @Param("facility") String facility, 
                                                     @Param("capacity") Integer capacity, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND facility LIKE :facility AND price BETWEEN :lowerLimit AND :upperLimit")
    Page<Room> findAllByStatusAndFacilityAndPriceRange(@Param("status") String status, @Param("facility") String facility, 
                                                       @Param("lowerLimit") BigDecimal lowerLimit, 
                                                       @Param("upperLimit") BigDecimal upperLimit, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND facility LIKE :facility AND room_type LIKE :roomType AND capacity = :capacity")
    Page<Room> findAllByStatusAndFacilityAndRoomTypeAndCapacity(@Param("status") String status, @Param("facility") String facility, 
                                                                @Param("roomType") String roomType, @Param("capacity") Integer capacity, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND facility LIKE :facility AND room_type LIKE :roomType AND price BETWEEN :lowerLimit AND :upperLimit")
    Page<Room> findAllByStatusAndFacilityAndRoomTypeAndPriceRange(@Param("status") String status, @Param("facility") String facility, 
                                                                  @Param("roomType") String roomType, 
                                                                  @Param("lowerLimit") BigDecimal lowerLimit, 
                                                                  @Param("upperLimit") BigDecimal upperLimit, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND room_type LIKE :roomType AND price BETWEEN :lowerLimit AND :upperLimit")
    Page<Room> findAllByStatusAndRoomTypeAndPriceRange(@Param("status") String status, @Param("roomType") String roomType, 
                                                       @Param("lowerLimit") BigDecimal lowerLimit, 
                                                       @Param("upperLimit") BigDecimal upperLimit, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND room_type LIKE :roomType AND capacity = :capacity")
    Page<Room> findAllByStatusAndRoomTypeAndCapacity(@Param("status") String status, @Param("roomType") String roomType, 
                                                     @Param("capacity") Integer capacity, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM rooms WHERE status LIKE :status AND room_type LIKE :roomType AND capacity = :capacity AND price BETWEEN :lowerLimit AND :upperLimit")
    Page<Room> findAllByStatusAndRoomTypeAndCapacityAndPriceRange(@Param("status") String status, @Param("roomType") String roomType, 
                                                                  @Param("capacity") Integer capacity, 
                                                                  @Param("lowerLimit") BigDecimal lowerLimit, 
                                                                  @Param("upperLimit") BigDecimal upperLimit, Pageable pageable);
}


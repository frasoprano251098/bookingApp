package com.soprano.francesco.repository;

import com.soprano.francesco.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value =  "SELECT * FROM rooms r " +
                    "WHERE ( :seats <= r.capacity ) AND " +
                            "NOT EXISTS (SELECT 1 " +
                                         "FROM bookings b WHERE b.room_id = r.id AND ( " +
                                         " :startTime < b.end_time AND :endTime <= b.start_time ))", nativeQuery = true)
    List<Room> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime, int seats);
}

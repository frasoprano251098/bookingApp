package com.soprano.francesco.repositories;

import com.soprano.francesco.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT * FROM bookings WHERE room_id = :roomId " +
            "AND (start_time BETWEEN :startTime AND :endTime " +
            "OR end_time BETWEEN :startTime AND :endTime " +
            "OR (start_time <= :startTime AND end_time >= :endTime))", nativeQuery = true)
    List<Booking> findBookingsInTimeRange(Long roomId, LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "SELECT SUM(seats) FROM bookings " +
            "WHERE room_id = :roomId " +
            "AND start_time < :endTime " +
            "AND end_time > :startTime", nativeQuery = true)
    Integer getTotalSeatsBooked(Long roomId, LocalDateTime startTime, LocalDateTime endTime);

    List<Booking> findByUsername(String username);

    List<Booking> findByRoomId(Long roomId);
}

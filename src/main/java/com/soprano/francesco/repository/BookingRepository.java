package com.soprano.francesco.repository;

import com.soprano.francesco.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUsername(String username);
    List<Booking> findByRoomId(Long roomId);
}

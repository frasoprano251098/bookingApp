package com.soprano.francesco.service;

import com.soprano.francesco.entity.Booking;
import com.soprano.francesco.entity.Room;
import com.soprano.francesco.repository.BookingRepository;
import com.soprano.francesco.repository.RoomRepository;
import com.soprano.francesco.rest.dtos.request.BookingSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    private final BookingRepository bookingRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    public List<Room> getAvailableRooms(BookingSearchRequest bookingSearchRequest) {
        return roomRepository.getAvailableRooms(
                bookingSearchRequest.getStartTime(),
                bookingSearchRequest.getEndTime(),
                bookingSearchRequest.getSeats());
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Transactional
    public Optional<Room> updateRoom(Long roomId, Room updatedRoom) {
        if (roomRepository.existsById(roomId)) {
            updatedRoom.setId(roomId);
            return Optional.of(roomRepository.save(updatedRoom));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Boolean> deleteRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            roomRepository.deleteById(roomId);
            return Optional.of(false);
        }

        List<Booking> roomsBooked = bookingRepository.findByRoomId(roomId);
        if(!roomsBooked.isEmpty()) {
            return Optional.empty();
        }

       return Optional.of(true);
    }
}


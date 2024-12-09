package com.soprano.francesco.services;

import com.soprano.francesco.entities.Booking;
import com.soprano.francesco.entities.Room;
import com.soprano.francesco.exceptions.RoomNotAvailableException;
import com.soprano.francesco.mappers.BookingMapper;
import com.soprano.francesco.repositories.BookingRepository;
import com.soprano.francesco.repositories.RoomRepository;
import com.soprano.francesco.rest.dtos.requests.AvailabilityRequest;
import com.soprano.francesco.rest.dtos.requests.BookingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingService(RoomRepository roomRepository,
                          BookingRepository bookingRepository,
                          BookingMapper bookingMapper) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Transactional
    public Optional<Booking> createBooking(BookingRequest bookingRequest) {
        Optional<Room> roomOptional = roomRepository.findById(bookingRequest.getRoomId());
        if (roomOptional.isEmpty()) {
            return Optional.empty();
        }

        Room room = roomOptional.get();
        boolean isAvailable = checkAvailability(room, bookingRequest.getStartTime(), bookingRequest.getEndTime(), bookingRequest.getSeats());

        if (!isAvailable) {
            throw new RoomNotAvailableException("Room is not available for the selected time.");
        }

        Booking booking = bookingMapper.toEntity(bookingRequest);
        booking.setRoom(room);
        bookingRepository.save(booking);

        return Optional.of(booking);
    }

    private boolean checkAvailability(Room room, LocalDateTime startTime, LocalDateTime endTime, int seats) {
        List<Room> availableRooms = roomRepository.getAvailableRooms(startTime, endTime, seats);
        return availableRooms.stream().anyMatch(r -> r.getId().equals(room.getId()));
    }

    public List<Booking> getUserBookings(String username) {
        return bookingRepository.findByUsername(username);
    }

    public List<Booking> getRoomBookings(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    public List<Room> getAvailableRooms(AvailabilityRequest availabilityRequest) {
        return roomRepository.getAvailableRooms(
                availabilityRequest.getStartTime(),
                availabilityRequest.getEndTime(),
                availabilityRequest.getSeats());
    }

    @Transactional
    public boolean cancelBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            return false;
        }

        bookingRepository.delete(bookingOptional.get());
        return true;
    }
}




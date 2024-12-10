package com.soprano.francesco.rest.contollers;

import com.soprano.francesco.entities.Booking;
import com.soprano.francesco.exceptions.RoomNotAvailableException;
import com.soprano.francesco.rest.dtos.requests.AvailabilityRequest;
import com.soprano.francesco.rest.dtos.requests.BookingRequest;
import com.soprano.francesco.rest.dtos.responses.AvailabilityResponse;
import com.soprano.francesco.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody BookingRequest bookingRequest) {
        Optional<Booking> bookingOptional = bookingService.createBooking(bookingRequest);

        if (bookingOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Room not found.");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingOptional.get());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable String username) {
        List<Booking> bookings = bookingService.getUserBookings(username);
        return bookings.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookings) :
                ResponseEntity.ok(bookings);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Booking>> getRoomBookings(@PathVariable Long roomId) {
        List<Booking> bookings = bookingService.getRoomBookings(roomId);
        return bookings.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookings) :
                ResponseEntity.ok(bookings);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailabilityResponse>> getAvailableRooms(@RequestBody AvailabilityRequest availabilityRequest) {
        List<AvailabilityResponse> availableRooms = bookingService.getAvailableRooms(availabilityRequest);
        return availableRooms.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(availableRooms) :
                ResponseEntity.ok(availableRooms);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Object> cancelBooking(@PathVariable Long bookingId) {
        boolean isCancelled = bookingService.cancelBooking(bookingId);

        if (!isCancelled) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Booking not found.");
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Booking cancelled successfully.");
    }

    @ExceptionHandler(RoomNotAvailableException.class)
    public ResponseEntity<Object> handleRoomNotAvailable(RoomNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}


package com.soprano.francesco.rest.contollers;

import com.soprano.francesco.entities.Booking;
import com.soprano.francesco.entities.Room;
import com.soprano.francesco.exceptions.OwnerBookingException;
import com.soprano.francesco.exceptions.RoomNotAvailableException;
import com.soprano.francesco.rest.dtos.requests.BookingRequest;
import com.soprano.francesco.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getDetails();

        Optional<Booking> bookingOptional = bookingService.createBooking(bookingRequest, username);
        return bookingOptional.<ResponseEntity<Object>>map(booking -> ResponseEntity.status(HttpStatus.CREATED)
                .body(booking)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Room not found."));

    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(bookings);
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

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long bookingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getDetails();

        boolean isCancelled;
        try{
            isCancelled = bookingService.deleteBooking(bookingId, username);
        }catch (OwnerBookingException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

        if (!isCancelled) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


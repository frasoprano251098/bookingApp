package com.soprano.francesco.mappers;

import com.soprano.francesco.entities.Booking;
import com.soprano.francesco.rest.dtos.requests.BookingRequest;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public Booking toEntity(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setStartTime(bookingRequest.getStartTime());
        booking.setEndTime(bookingRequest.getEndTime());
        booking.setSeats(bookingRequest.getSeats());
        return booking;
    }
}


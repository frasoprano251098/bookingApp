package com.soprano.francesco.mapper;

import com.soprano.francesco.entity.Booking;
import com.soprano.francesco.rest.dtos.request.BookingRequest;
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


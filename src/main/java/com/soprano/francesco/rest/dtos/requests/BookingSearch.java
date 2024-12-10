package com.soprano.francesco.rest.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingSearch {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}


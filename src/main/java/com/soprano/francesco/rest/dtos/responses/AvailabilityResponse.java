package com.soprano.francesco.rest.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponse {
    private Long roomId;
    private String roomName;
    private int capacity;
}

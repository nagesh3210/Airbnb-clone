package com.airbnb.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PropertyDto
{
    private Long id;

    private String title;

    private String location;

    private Double pricePerNight;

    private Integer totalGuests;

    private Boolean available;

    private String ownerName;
}

package com.airbnb.propertyservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Propertydto
{
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;


    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Price is required")
    @Positive
    private Double pricePerNight;

    @NotNull(message = "Guest count required")
    @Positive
    private Integer totalGuests;

    private Boolean available;

    @NotBlank(message = "Owner name required")
    private String ownerName;
}

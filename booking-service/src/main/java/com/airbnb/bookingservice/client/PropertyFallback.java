package com.airbnb.bookingservice.client;

import com.airbnb.bookingservice.dto.PropertyDto;

public class PropertyFallback implements PropertyClient
{

    @Override
    public PropertyDto getPropertyById(Long id)
    {
        PropertyDto dto = new PropertyDto();

        dto.setAvailable(false);

        return dto;    }
}

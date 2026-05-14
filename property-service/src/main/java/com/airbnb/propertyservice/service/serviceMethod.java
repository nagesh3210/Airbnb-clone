package com.airbnb.propertyservice.service;

import com.airbnb.propertyservice.dto.Propertydto;
import com.airbnb.propertyservice.entity.Property;
import org.springframework.stereotype.Component;

@Component
public class serviceMethod
{
    public Propertydto mapToDto(Property property) {

        Propertydto dto = new Propertydto();

        dto.setId(property.getId());
        dto.setTitle(property.getTitle());
        dto.setLocation(property.getLocation());
        dto.setPricePerNight(property.getPricePerNight());
        dto.setTotalGuests(property.getTotalGuests());
        dto.setAvailable(property.getAvailable());
        dto.setOwnerName(property.getOwnerName());

        return dto;
    }

    // DTO → ENTITY
    public Property mapToEntity(Propertydto dto) {

        Property property = new Property();

        property.setId(dto.getId());
        property.setTitle(dto.getTitle());
        property.setLocation(dto.getLocation());
        property.setPricePerNight(dto.getPricePerNight());
        property.setTotalGuests(dto.getTotalGuests());
        property.setAvailable(dto.getAvailable());
        property.setOwnerName(dto.getOwnerName());

        return property;
    }
}

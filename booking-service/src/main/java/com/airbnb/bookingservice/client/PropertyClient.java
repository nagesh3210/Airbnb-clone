package com.airbnb.bookingservice.client;

import com.airbnb.bookingservice.dto.PropertyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(
        name = "PROPERTY-SERVICE",
        fallback = PropertyFallback.class
)
public interface PropertyClient
{
    @GetMapping("/property/{id}")
    PropertyDto getPropertyById(
            @PathVariable Long id
    );
}

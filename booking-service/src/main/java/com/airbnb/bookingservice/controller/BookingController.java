package com.airbnb.bookingservice.controller;


import com.airbnb.bookingservice.entity.Booking;
import com.airbnb.bookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/booking")
public class BookingController
{
    @Autowired
    private BookingService bookingService;

    @GetMapping("/test")
    public String test()
    {
        return "Booking Service Working";
    }

    @PostMapping("/create")
    public CompletableFuture<String> createBooking(@RequestBody Booking booking)
    {
        String booking1 = bookingService.createBooking(booking);
        return CompletableFuture.completedFuture(booking1);
    }


}

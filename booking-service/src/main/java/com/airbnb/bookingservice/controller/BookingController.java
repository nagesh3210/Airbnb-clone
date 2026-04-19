package com.airbnb.bookingservice.controller;


import com.airbnb.bookingservice.DTO.BookingEvent;
import com.airbnb.bookingservice.entity.Booking;
import com.airbnb.bookingservice.kafka.BookingProducer;
import com.airbnb.bookingservice.service.BookingConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/booking")
public class BookingController
{
    @Autowired
    private BookingConsumer bookingService;

    @Autowired
    private BookingProducer bookingProducer;

    @GetMapping("/test")
    public String test()
    {
        return "Booking Service Working";
    }


    @PostMapping("/create")
    public CompletableFuture<String> createBooking(@RequestBody BookingEvent event)
    {
        String bookingId = UUID.randomUUID().toString();

        event.setBookingId(bookingId);

        bookingProducer.send(event);
        return CompletableFuture.completedFuture("Request queued" + bookingId);
    }


}

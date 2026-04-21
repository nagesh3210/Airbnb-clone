package com.airbnb.bookingservice.controller;


import com.airbnb.bookingservice.DTO.BookingEvent;
import com.airbnb.bookingservice.entity.Booking;
import com.airbnb.bookingservice.kafka.BookingProducer;
import com.airbnb.bookingservice.repository.BookingRepository;
import com.airbnb.bookingservice.service.BookingConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @Autowired
    private BookingRepository bookingRepository;

//    @GetMapping("/test")
//    public String test()
//    {
//        return "Booking Service Working";
//    }

    @PostMapping("/create")
    public Map<String, String> createBooking(@RequestBody BookingEvent event) {
        String bookingId = UUID.randomUUID().toString();
        event.setBookingId(bookingId);
        bookingProducer.send(event);

        return Map.of(
                "bookingId", bookingId,
                "status", "PENDING"
        );
    }

    @GetMapping("/status/{bookingId}")
    public Map<String,String> getStatus(@PathVariable("bookingId") String bookingId)
    {

        System.out.println(bookingId);

        Booking booking = bookingRepository.findByBookingId(bookingId);

        if (booking == null)
        {
           return Map.of("status","PROCESSING");
        }

        return Map.of("status",booking.getStatus());
    }

    @GetMapping("all")
    public List<Booking> getAllBookigs()
    {
        return bookingRepository.findAll();
    }


}

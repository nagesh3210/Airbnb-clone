package com.airbnb.bookingservice.kafka;

import com.airbnb.bookingservice.DTO.BookingEvent;
import com.airbnb.bookingservice.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class BookingProducer
{
    @Autowired
    private KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public void send(BookingEvent event) {
        kafkaTemplate.send("booking-topic", event);
    }
}

package com.airbnb.bookingservice.kafka;

import com.airbnb.bookingservice.DTO.BookingEvent;
import com.airbnb.bookingservice.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingProducer
{
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public BookingProducer(KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(BookingEvent event) {
        System.out.println("Sending: " + event);
        kafkaTemplate.send("booking-topic",String.valueOf(event.getPropertyId()), event);
    }
}

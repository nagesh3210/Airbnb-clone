package com.airbnb.bookingservice.kafka;

import com.airbnb.common.events.BookingEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingProducer
{
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BookingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(BookingEvent event) {
        System.out.println("Sending: " + event);
        kafkaTemplate.send("booking-topic",String.valueOf(event.getPropertyId()), event);
    }
}

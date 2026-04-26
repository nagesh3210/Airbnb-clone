package com.airbnb.bookingservice.service;

import com.airbnb.bookingservice.entity.Booking;
import com.airbnb.bookingservice.repository.BookingRepository;
import com.airbnb.common.events.PaymentResultEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentResultConsumer
{
    private final BookingRepository repo;

    public PaymentResultConsumer(BookingRepository repo) {
        this.repo = repo;
    }

    @KafkaListener(topics = "payment-result-topic" , groupId = "booking-group" ,containerFactory = "paymentResultKafkaListenerFactory")
    public void handlePaymentResult(PaymentResultEvent event)
    {
        System.out.println("Received payment result for: " + event.getBookingId());

        Booking booking = repo.findByBookingId(event.getBookingId());

        if (booking == null) return;

        if ("SUCCESS".equals(event.getStatus())) {
            booking.setStatus("CONFIRMED");
        } else {
            booking.setStatus("FAILED");
        }

        repo.save(booking);
    }

}


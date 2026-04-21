package com.airbnb.bookingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendStatus(String bookingId, String status) {
        messagingTemplate.convertAndSend(
                "/topic/booking/" + bookingId,
                status
        );
    }
}
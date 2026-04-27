package com.airbnb.notificationservice.service;

import com.airbnb.common.events.PaymentResultEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer
{
    @KafkaListener(
            topics = "payment-result-topic",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void notifyUser(PaymentResultEvent event) {

        System.out.println(
                "NOTIFICATION → bookingId=" + event.getBookingId() +
                        " status=" + event.getStatus()
        );
    }
}

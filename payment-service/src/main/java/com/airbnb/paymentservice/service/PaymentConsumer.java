package com.airbnb.paymentservice.service;

import com.airbnb.common.events.PaymentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer
{
    @KafkaListener(
            topics = "payment-topic",
            groupId = "payment-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void processPayment(PaymentEvent event)
    {
        System.out.println("Received Payment: "+ event.getBookingId() );

        try
        {
            Thread.sleep(1000);
        }catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        if(Math.random() > 0.2)
        {
            event.setStatus("SUCCESS");
            System.out.println("Payment SUCCESS for " + event.getBookingId());
        }
        else {
            event.setStatus("FAILED");
            System.out.println("Payment FAILED for " + event.getBookingId());
        }


    }

}

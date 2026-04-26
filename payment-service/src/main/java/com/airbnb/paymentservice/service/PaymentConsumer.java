package com.airbnb.paymentservice.service;

import com.airbnb.common.events.PaymentEvent;
import com.airbnb.common.events.PaymentResultEvent;
import com.airbnb.paymentservice.kafka.PaymentResultProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer
{

  @Autowired
    private PaymentResultProducer resultProducer;

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

        PaymentResultEvent result = new PaymentResultEvent();
        result.setBookingId(event.getBookingId());
        if(Math.random() > 0.2)
        {
            result.setStatus("SUCCESS");

            System.out.println("Payment SUCCESS for " + event.getBookingId());
        }
        else {
            result.setStatus("FAILED");
            System.out.println("Payment FAILED for " + event.getBookingId());
        }
        resultProducer.send(result);

    }

}

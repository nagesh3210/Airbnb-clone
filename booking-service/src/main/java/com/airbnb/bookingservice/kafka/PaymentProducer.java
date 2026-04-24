package com.airbnb.bookingservice.kafka;


import com.airbnb.common.events.PaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer
{
    private final KafkaTemplate<String ,Object> kafkaTemplate;

    public PaymentProducer(KafkaTemplate<String, Object> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(PaymentEvent event)
    {
        kafkaTemplate.send("payment-topic",event.getBookingId(),event);
    }


}

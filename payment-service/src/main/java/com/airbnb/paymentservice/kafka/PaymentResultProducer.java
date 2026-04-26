package com.airbnb.paymentservice.kafka;

import com.airbnb.common.events.PaymentResultEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentResultProducer
{
  private final KafkaTemplate<String ,Object> kafkaTemplate;

    public PaymentResultProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(PaymentResultEvent event)
    {
        kafkaTemplate.send("payment-result-topic",event.getBookingId(),event);
    }
}

package com.airbnb.paymentservice.service;

import com.airbnb.common.events.PaymentEvent;
import com.airbnb.common.events.PaymentResultEvent;
import com.airbnb.paymentservice.kafka.PaymentResultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer
{


    private static final Logger log =
            LoggerFactory.getLogger(PaymentConsumer.class);

  @Autowired
    private PaymentResultProducer resultProducer;

    @KafkaListener(
            topics = "payment-topic",
            groupId = "payment-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void processPayment(PaymentEvent event)
    {
        try{
            MDC.put("correlationId", event.getCorrelationId());
            System.out.println("Received Payment: "+ event.getBookingId() );
            log.info("correlationId={} bookingId={} received",event.getCorrelationId(), event.getBookingId());

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
        catch (Exception e)
        {
            e.getSuppressed();
        }
        finally {
            MDC.clear();
        }
    }

}

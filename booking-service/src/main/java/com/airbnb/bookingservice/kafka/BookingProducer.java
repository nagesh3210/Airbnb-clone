package com.airbnb.bookingservice.kafka;

import com.airbnb.common.events.BookingEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingProducer
{
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BookingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Retry(name = "kafkaRetry", fallbackMethod = "fallback")
    @CircuitBreaker(name = "kafkaCB", fallbackMethod = "fallback")
    public void send(BookingEvent event)
    {
        try {
            System.out.println("Sending: " + event);
            MDC.put("correlationId", event.getCorrelationId());
            kafkaTemplate.send("booking-topic", String.valueOf(event.getPropertyId()), event);
        }
        catch (Exception e)
        {
            e.getSuppressed();
        }
        finally{
            MDC.clear();
            }
    }

    public String fallback(BookingEvent event, Exception ex) {
        System.out.println("Fallback triggered: " + ex.getMessage());
        return "REQUEST_QUEUED";
    }
}

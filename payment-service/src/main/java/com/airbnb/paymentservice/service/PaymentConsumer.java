package com.airbnb.paymentservice.service;

import com.airbnb.common.events.PaymentEvent;
import com.airbnb.common.events.PaymentResultEvent;
import com.airbnb.paymentservice.kafka.PaymentResultProducer;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentConsumer.class);

    @Autowired
    private PaymentResultProducer resultProducer;

    @KafkaListener(
            topics = "payment-topic",
            groupId = "payment-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleEvent(PaymentEvent event) {
        processPayment(event);
    }

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    @Retry(name = "paymentRetry")
    @Bulkhead(name = "paymentBulkhead", type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = "paymentService")
    public void processPayment(PaymentEvent event) {

        try {
            MDC.put("correlationId", event.getCorrelationId());

            log.info("bookingId={} received", event.getBookingId());

            Thread.sleep(1000);

            if (Math.random() < 0.7) {
                throw new RuntimeException("Payment gateway failure");
            }

            PaymentResultEvent result = new PaymentResultEvent();
            result.setBookingId(event.getBookingId());
            result.setStatus("SUCCESS");

            resultProducer.send(result);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            MDC.clear();
        }
    }

    public void paymentFallback(PaymentEvent event, Exception ex) {

        if (ex instanceof io.github.resilience4j.bulkhead.BulkheadFullException) {
            log.error("Bulkhead full → rejecting request bookingId={}", event.getBookingId());
        } else {
            log.error("Payment failed bookingId={}", event.getBookingId());
        }

        log.error("Fallback triggered for bookingId={}", event.getBookingId());

        PaymentResultEvent result = new PaymentResultEvent();
        result.setBookingId(event.getBookingId());
        result.setStatus("FAILED");

        resultProducer.send(result);
    }
}
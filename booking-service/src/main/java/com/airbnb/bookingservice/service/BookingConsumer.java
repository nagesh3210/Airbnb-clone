package com.airbnb.bookingservice.service;


import com.airbnb.bookingservice.entity.Booking;
import com.airbnb.bookingservice.kafka.PaymentProducer;
import com.airbnb.bookingservice.repository.BookingRepository;
import com.airbnb.common.events.BookingEvent;
import com.airbnb.common.events.PaymentEvent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookingConsumer {

    @Autowired
    private BookingRepository repo;

    @Autowired
    private BookingNotificationService notificationService;

    @Autowired
    private RedisLockService redisLockService;

    @Autowired
    private PaymentProducer paymentProducer;

    private static final Logger log =
            LoggerFactory.getLogger(BookingConsumer.class);

    //    @Async("bookingExecutor")
    @Transactional
    @KafkaListener(topics = "booking-topic", groupId = "booking-group", containerFactory = "bookingKafkaListenerFactory"
    )
    public void createBooking(com.airbnb.common.events.BookingEvent event) {
        System.out.println("Received: " + event);
        log.info("bookingId={} received", event.getBookingId());

        // ✅ 1. IDEMPOTENCY FIRST
        if (repo.existsByBookingId(event.getBookingId())) {
            System.out.println("Duplicate event ignored");
            return;
        }

        Booking booking = new Booking();
        booking.setBookingId(event.getBookingId());
        booking.setUserId(event.getUserId());
        booking.setPropertyId(event.getPropertyId());
        booking.setStartDate(event.getStartDate());
        booking.setEndDate(event.getEndDate());
        booking.setStatus("PENDING");

        repo.save(booking);

        String lockKey = "booking:" + event.getPropertyId();

        int retries = 3;

        while (retries-- > 0) {

            boolean locked = redisLockService.acquireLock(lockKey);

            if (locked) {
                try {
                    boolean exists = repo.existsConflictingBooking(
                            event.getPropertyId(),
                            event.getBookingId(),
                            event.getStartDate(),
                            event.getEndDate()
                    );

                    if (exists) {
                        booking.setStatus("FAILED");
                        repo.save(booking);
                        log.warn("bookingId={} status=FAILED (conflict)", event.getBookingId());
                        notificationService.sendStatus(event.getBookingId(), "FAILED");
                        return;
                    }
                    repo.save(booking);
                    log.info("bookingId={} status=CONFIRMED", event.getBookingId());
                    notificationService.sendStatus(event.getBookingId(), "CONFIRMED");
                    PaymentEvent paymentEvent = new PaymentEvent();
                    paymentEvent.setBookingId(event.getBookingId());
                    paymentEvent.setAmount(1000.0);      // TODO: compute real price
                    paymentEvent.setStatus("INITIATED"); // initial state
                    paymentProducer.send(paymentEvent);
                    return;

                } finally {
                    redisLockService.releaseLock(lockKey);
                }
            }

            // 🔁 retry wait (important)
            try {
                Thread.sleep(100*(4 - retries));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // ❗ DO NOT silently skip
        throw new RuntimeException("Could not acquire lock after retries");
    }
}


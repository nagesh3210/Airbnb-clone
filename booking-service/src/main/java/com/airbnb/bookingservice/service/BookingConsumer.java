package com.airbnb.bookingservice.service;

import com.airbnb.bookingservice.DTO.BookingEvent;
import com.airbnb.bookingservice.entity.Booking;
import com.airbnb.bookingservice.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookingConsumer {

    @Autowired
    private BookingRepository repo;

    @Autowired
    private RedisLockService redisLockService;

    //    @Async("bookingExecutor")
    @Transactional
    @KafkaListener(topics = "booking-topic", groupId = "booking-group")
    public void createBooking(BookingEvent event) {

        String lockKey = "booking:" + event.getPropertyId();

        boolean locked = redisLockService.acquireLock(lockKey);


        if (!locked) {
            System.out.println("Skip due to lock");
            return;
        }


        try {
            boolean exists = repo.existsConflictingBooking(
                    event.getPropertyId(),
                    event.getStartDate(),
                    event.getEndDate()
            );

            if (exists) {
                System.out.println("Already booked");
                return;
            }


            Booking booking = new Booking();
            booking.setUserId(event.getUserId());
            booking.setPropertyId(event.getPropertyId());
            booking.setStartDate(event.getStartDate());
            booking.setEndDate(event.getEndDate());
            booking.setStatus("CONFIRMED");

            repo.save(booking);

            System.out.println("Booking success");

        } finally {
            redisLockService.releaseLock(lockKey);
        }
    }
}


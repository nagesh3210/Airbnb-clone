package com.airbnb.bookingservice.service;

import com.airbnb.bookingservice.entity.Booking;
import com.airbnb.bookingservice.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BookingService {

    @Autowired
    private BookingRepository repo;

    @Autowired
    private RedisLockService redisLockService;

    //    @Async("bookingExecutor")
    @Transactional
    public String createBooking(Booking booking) {

        String lockKey = "booking:" + booking.getPropertyId();

        int retries = 3;

        while (retries-- > 0) {

            boolean locked = redisLockService.acquireLock(lockKey);

            if (locked) {
                try {
                    boolean exists = repo.existsConflictingBooking(
                            booking.getPropertyId(),
                            booking.getStartDate(),
                            booking.getEndDate()
                    );

                    if (exists) {
                        return "Room already booked!";
                    }

                    repo.save(booking);
                    return "Booking Confirmed";

                } finally {
                    redisLockService.releaseLock(lockKey);
                }
            }

            // wait before retry
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return "System busy, try again!";
    }
}
package com.airbnb.bookingservice.repository;

import com.airbnb.bookingservice.entity.Booking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking,Long>
{
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.propertyId = :propertyId " +
            "AND b.bookingId <> :bookingId " +
            "AND b.startDate <= :endDate " +
            "AND b.endDate >= :startDate")
    boolean existsConflictingBooking(
            @Param("propertyId") Long propertyId,
            @Param("bookingId") String bookingId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    boolean existsByBookingId(String bookingId);

    Booking findByBookingId(String bookingId);
}
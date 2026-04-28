package com.airbnb.common.events;

import java.io.Serializable;
import java.time.LocalDate;


public class BookingEvent implements Serializable
{
    private Long userId;
    private Long propertyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String correlationId;
    private String bookingId;

    public BookingEvent() {
    }

    public BookingEvent(Long userId, Long propertyId, LocalDate startDate, LocalDate endDate,String correlationId) {
        this.userId = userId;
        this.propertyId = propertyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.correlationId = correlationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}

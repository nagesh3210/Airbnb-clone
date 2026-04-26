package com.airbnb.common.events;

import java.io.Serializable;

public class PaymentResultEvent implements Serializable
{
    private String bookingId;

    private String status;

    public PaymentResultEvent() {
    }

    public PaymentResultEvent(String bookingId, String status) {
        this.bookingId = bookingId;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

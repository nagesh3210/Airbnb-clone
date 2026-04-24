package com.airbnb.common.events;


import java.io.Serializable;

public class PaymentEvent implements Serializable {

    private String bookingId;
    private Double amount;
    private String status;

    public PaymentEvent() {}

    public PaymentEvent(String bookingId, Double amount, String status) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
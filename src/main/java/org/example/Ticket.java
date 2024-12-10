package org.example;

import java.math.BigDecimal;

/**
 * Represents a ticket for an event.
 */
public class Ticket {
    private int ticketId;
    private String eventName;
    private BigDecimal ticketPrice;

    /**
     * Constructs a new Ticket object with the specified ID, event name, and price.
     *
     * @param ticketId   The unique identifier for the ticket.
     * @param eventName  The name of the event.
     * @param ticketPrice The price of the ticket.
     */
    public Ticket(int ticketId, String eventName, BigDecimal ticketPrice) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.ticketPrice = ticketPrice;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", eventName='" + eventName + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}

package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class Vendor implements Runnable{
    private TicketPool ticketPool;
    private int releaseRate;
    private int ticketsAdded = 0;
    private String vendorID;

    private static final Logger logger = LogManager.getLogger(Vendor.class);

    public Vendor(String vendorID, TicketPool ticketPool, int releaseRate){
        this.vendorID = vendorID;
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
    }

    private static final AtomicInteger count = new AtomicInteger(0);

    @Override
    public void run() {
        while (true) {
            int ticketID = count.incrementAndGet();
            if (ticketID > ticketPool.getTotalTickets()) {
                break; // Stop adding tickets if the total limit is reached
            }

            Ticket ticket = new Ticket(ticketID, "Simple Event", new BigDecimal("1000"));
            ticketPool.addTickets(ticket);
            logger.info(Thread.currentThread().getName() + " released a ticket. Total released by this vendor: " + (++ticketsAdded) + ".");

            try {
                Thread.sleep(releaseRate * 1000); // Convert seconds to milliseconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Vendor thread interrupted: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}

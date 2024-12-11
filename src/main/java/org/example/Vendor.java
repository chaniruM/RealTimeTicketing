package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Vendor class represents a ticket vendor in the simulation.
 * Vendors release tickets into the ticket pool at a specified rate.
 * Runnable interface is implemented for multithreading purposes
 */
public class Vendor implements Runnable{
    private TicketPool ticketPool;
    private int releaseRate;
    private int ticketsAdded = 0;
    private String vendorID;

    private static final Logger logger = LogManager.getLogger(Vendor.class); //instance of the logger

    /**
     * Creates a new vendor with the given ID, ticket pool, and release rate.
     *
     * @param vendorID   Unique identifier for the vendor.
     * @param ticketPool The shared ticket pool to which the vendor adds tickets.
     * @param releaseRate The rate at which the vendor releases tickets (in milliseconds).
     */
    public Vendor(String vendorID, TicketPool ticketPool, int releaseRate){
        this.vendorID = vendorID;
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
    }

    /**
     * Atomic integer to track the total number of tickets added across all vendors.
     * ensures that the entire operation of reading, incrementing, and storing the new value
     * happens as a single, indivisible unit of execution, prevents any other thread from
     * interfering in the middle of the update process
     */
    private static final AtomicInteger count = new AtomicInteger(0);

    @Override
    public void run() {
        while (true) {
            int ticketID = count.incrementAndGet(); //increases number of tickets added by 1 each time a ticket is added
            if (ticketID > ticketPool.getTotalTickets()) { //ensures that the total number of tickets does not exceed the total tickets set originally
                logger.info(Thread.currentThread().getName()+" is stopping as all tickets are released.");
                break; // Stop adding tickets if the total limit is reached
            }

            Ticket ticket = new Ticket(ticketID, "Simple Event", new BigDecimal("1000")); //creating a ticket
            ticketPool.addTickets(ticket);
            logger.info(Thread.currentThread().getName() + " released a ticket. Total released by this vendor: " + (++ticketsAdded) + ".");

            try {
                Thread.sleep(releaseRate * 1000); // Delays the thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error(Thread.currentThread().getName()+" thread interrupted: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}

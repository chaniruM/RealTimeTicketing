package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Customer class represents a customer in the simulation.
 * Customers retrieve tickets from the ticket pool at a specified rate.
 * Runnable interface is implemented for multithreading purposes
 */
public class Customer implements Runnable{
    private TicketPool ticketPool;
    private String customerID;
    private int retrievalRate;
    private static final Logger logger = LogManager.getLogger(Customer.class);

    /**
     * Creates a new customer with the given ID, ticket pool, and retrieval rate.
     *
     * @param customerID   Unique identifier for the customer.
     * @param ticketPool   The shared ticket pool from which customers purchase tickets.
     * @param retrievalRate The rate at which the customer attempts to purchase tickets.
     */
    public Customer(String customerID, TicketPool ticketPool, int retrievalRate){
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.customerID = customerID;
    }

    /**
     * Atomic integer to track the total number of tickets sold across all customers.
     * ensures that the entire operation of reading, incrementing, and storing the new value
     * happens as a single, indivisible unit of execution, prevents any other thread from
     * interfering in the middle of the update process
     */
    private static final AtomicInteger count = new AtomicInteger(0);

    @Override
    public void run() {
        while (true) {
            // Increment the total tickets sold count
            int sold = count.incrementAndGet();

            //ensures that tickets sold does not exceed total tickets limit
            if (sold > ticketPool.getTotalTickets()) {
                logger.info(Thread.currentThread().getName() + " stopping as tickets are sold out.");
                break;
            }

            logger.info(Thread.currentThread().getName() + " trying to purchase a ticket");
            Ticket ticket = ticketPool.removeTickets();
            logger.info("Ticket-"+ticket.getTicketId() + " sold.");
            try {
                Thread.sleep(retrievalRate * 1000); // Delays the thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error(Thread.currentThread().getName()+" thread interrupted: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}

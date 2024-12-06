package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Runnable{
    private TicketPool ticketPool;
    private String customerID;
    private int retrievalRate;
    private int totalTickets;
    private static final Logger logger = LogManager.getLogger(Customer.class);

    public Customer(String customerID, TicketPool ticketPool, int retrievalRate){
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.customerID = customerID;
        this.totalTickets = ticketPool.getTotalTickets();
    }

    private static final AtomicInteger count = new AtomicInteger(0);

//    @Override
//    public void run() {
//        for (int i=0; i<numTickets; i++){
//            System.out.println(Thread.currentThread().getName() + " trying to purchase a ticket");
//            logger.info(Thread.currentThread().getName() + " trying to purchase a ticket");
//            Ticket ticket = ticketPool.removeTickets();
//            System.out.println("Ticket - " + ticket.getTicketId() + " bought by - " + Thread.currentThread().getName());
//            try {
//                Thread.sleep(retrievalRate * 1000); // Retieving delay
//            } catch (InterruptedException e) {
//                logger.error(customerID + " was interrupted.");
//                System.out.println(Thread.currentThread().getName() + " was interrupted.");
//                throw new RuntimeException(e);
//            }
//        }
//
//    }

    @Override
    public void run() {
        while (true) {
            int sold = count.incrementAndGet();
            if (sold > ticketPool.getTotalTickets()) {
                break; // Stop buying tickets if the total limit is reached
            }

            System.out.println(Thread.currentThread().getName() + " trying to purchase a ticket");
            logger.info(Thread.currentThread().getName() + " trying to purchase a ticket");
            Ticket ticket = ticketPool.removeTickets();
            System.out.println("Ticket - " + ticket.getTicketId() + " bought by - " + Thread.currentThread().getName());
            try {
                Thread.sleep(retrievalRate * 1000); // Retieving delay
            } catch (InterruptedException e) {
                logger.error(customerID + " was interrupted.");
                System.out.println(Thread.currentThread().getName() + " was interrupted.");
                throw new RuntimeException(e);
            }
        }
    }
}
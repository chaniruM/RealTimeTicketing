package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TicketPool manages the pool of tickets shared between Vendors and Customers.
 * Implements thread-safe operations for adding and retrieving tickets.
 */
public class TicketPool {
    private List<Ticket> tickets = Collections.synchronizedList(new ArrayList<>()); // Thread safe structure to store tickets

    private final int maxTicketCapacity; // Maximum ticket capacity in the pool

    private int totalTickets; //maximum number of tickets to be sold by all vendors combined

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition notEmpty = lock.newCondition(); //holds the customer threads until tickets are added to the pool
    private final Condition notFull = lock.newCondition(); //holds the vendor threads until there is more space the pool

    private static int ticketsSold = 0; //overall tickets sold

    private static int count = 0; //overall tickets added

    private static final Logger logger = LogManager.getLogger(TicketPool.class);

    public int getTicketsSize() {
        return tickets.size();
    }

    public static int getTicketsSold() {
        return ticketsSold;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public TicketPool(int maxTicketCapacity, int totalTickets) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
    }

    /**
     * Adds a new ticket to the pool. This method is synchronized using a ReentrantLock
     * to ensure thread safety when multiple vendors try to add tickets concurrently.
     *
     * @param ticket The ticket object to be added to the pool.
     * @throws InterruptedException If the thread is interrupted while waiting for space in the pool.
     */
    public void addTickets(Ticket ticket) {
        lock.lock();
        try {
            while (tickets.size() >= maxTicketCapacity) {
                logger.info("Ticket pool full. "+Thread.currentThread().getName()+" waiting for tickets to be sold...");
                notFull.await(); // Wait until space is available
            }
            tickets.add(ticket);
            logger.info("Ticket-"+ticket.getTicketId() + " added to ticket pool by "+Thread.currentThread().getName()+". Current pool has - " + tickets.size() + " tickets. Total tickets added by all vendors: " + (++count));
            notEmpty.signalAll(); // Notify waiting customers
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while adding tickets: " + e.getMessage());
            throw new RuntimeException("Thread interrupted while adding tickets: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes and returns a ticket from the pool. This method is synchronized using a ReentrantLock
     * to ensure thread safety when multiple customers try to remove tickets concurrently.
     *
     * @return The removed ticket object.
     * @throws InterruptedException If the thread is interrupted while waiting for tickets to be added to the pool.
     */
    public Ticket removeTickets() {
        lock.lock();
        try {
            while (tickets.isEmpty()) {
                logger.info(Thread.currentThread().getName() + " waiting for more tickets...");
                notEmpty.await(); // Wait for tickets to be added
            }
            Ticket ticket = tickets.remove(0);
            ticketsSold++;
            logger.info(Thread.currentThread().getName() + " purchased Ticket-" + ticket.getTicketId() + ". Total tickets sold: " + ticketsSold);
            notFull.signalAll(); // Notify waiting vendors
            return ticket;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while removing tickets: " + e.getMessage());
            throw new RuntimeException("Thread interrupted while removing tickets: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }


}

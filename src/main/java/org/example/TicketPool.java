package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private List<Ticket> tickets = Collections.synchronizedList(new ArrayList<>());
    private final int maxTicketCapacity;
    private final ReentrantLock lock = new ReentrantLock();

    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    private static int ticketsSold = 0;

    private int totalTickets = 0;
    private static int count = 0;

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

    public int getTotalTickets() {return totalTickets;}

    public TicketPool(int maxTicketCapacity, int totalTickets) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
    }

    public void addTickets(Ticket ticket) {
        lock.lock();
        try {
            while (tickets.size() >= maxTicketCapacity) {
                logger.info("Ticket pool full");
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

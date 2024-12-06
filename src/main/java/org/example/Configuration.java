package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Configuration {
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int totalTickets;
    public Configuration(int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity, int totalTickets) {
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrievalRate=" + customerRetrievalRate +
                ", maxTicketCapacity=" + maxTicketCapacity +
                ", totalTickets=" + totalTickets +
                '}';
    }

    public static Configuration getUserConfiguration(){
        Scanner scanner = new Scanner(System.in);

        int totalTickets;
        while (true) {
            try {
                System.out.print("Enter total number of tickets: ");
                totalTickets = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer!");
                scanner.next();
            }
        }

        int maxTicketCapacity;
        while (true) {
            try {
                System.out.print("Enter maximum ticket capacity of the ticket pool: ");
                maxTicketCapacity = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer!");
                scanner.next();
            }
        }

        int ticketReleaseRate;
        while (true) {
            try {
                System.out.print("Enter the ticket release rate: ");
                ticketReleaseRate = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer!");
                scanner.next();
            }
        }

        int customerRetrievalRate;
        while (true) {
            try {
                System.out.print("Enter the customer retrieval rate: ");
                customerRetrievalRate = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer!");
                scanner.next();
            }
        }

        return new Configuration(ticketReleaseRate, customerRetrievalRate, maxTicketCapacity, totalTickets);
    }
}
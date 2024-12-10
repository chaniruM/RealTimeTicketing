package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Configuration class to hold the ticketing system configuration settings.
 * Supports user input for creating configurations.
 */
public class Configuration {
    private int ticketReleaseRate; //the interval between each thread call
    private int customerRetrievalRate; //the interval between each thread call
    private int maxTicketCapacity; //the maximum number of tickets that can be in the ticket pool at any given time
    private int totalTickets; //the total number of tickets the vendors intend to sell combined

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
                "totalTickets = " + totalTickets +
                ", maxTicketCapacity = " + maxTicketCapacity +
                ", ticketReleaseRate = " + ticketReleaseRate +
                ", customerRetrievalRate = " + customerRetrievalRate +
                '}';
    }

    /**
     * Prompts the user to create a new configuration by providing input for each parameter.
     *
     * @return New Configuration instance with user input.
     */
    public static Configuration getUserConfiguration() {
        Scanner scanner = new Scanner(System.in);

        int totalTickets = getValidatedInput(scanner, "Enter total number of tickets: ");
        int maxTicketCapacity = getValidatedInput(scanner, "Enter maximum ticket capacity of the ticket pool: ");
        int ticketReleaseRate = getValidatedInput(scanner, "Enter the ticket release rate: ");
        int customerRetrievalRate = getValidatedInput(scanner, "Enter the customer retrieval rate: ");

        return new Configuration(ticketReleaseRate, customerRetrievalRate, maxTicketCapacity, totalTickets);
    }

    /**
     * Validates positive integer input from the user.
     *
     * @param scanner Input scanner.
     * @param prompt  Prompt message for the user.
     * @return Validated positive integer input.
     */
    private static int getValidatedInput(Scanner scanner, String prompt) {
        int temp; //temporarily holds the input value for validation
        while (true) {
            try {
                System.out.print(prompt);
                temp = scanner.nextInt();
                if (temp > 0) {
                    return temp;
                }
                System.out.println("Please enter a positive integer!");
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer!");
                scanner.next();
            }
        }
    }
}
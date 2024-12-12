package org.example;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main class to manage the ticketing system simulation.
 * Includes configuration setup, thread creation for vendors and customers,
 * and simulation control.
 */
public class Main {

    private final static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        System.out.println("Welcome to the RealTimeTicketing_CLI!");
        System.out.println();
        System.out.println("Let's set a new simulation configuration...");
        System.out.println();

        //Gson instance for reading and writing the configuration details in the JSON file
        Gson gson = new Gson();
        Configuration config = null;

        Scanner scanner = new Scanner(System.in);

        //Configuration option selection
        boolean validOption = false;
        while (!validOption) {
            System.out.print("Enter \"a\" for new configuration or \"b\" to use previous configuration: ");
            String opt = scanner.next();
            switch (opt) {
                case "a":
                    config = Configuration.getUserConfiguration();

                    try (FileWriter writer = new FileWriter("src/main/resources/configuration.json")) {
                        gson.toJson(config, writer); //saving the configuration in a JSON file
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    validOption = true;
                    break;
                case "b":
                    try (FileReader reader = new FileReader("src/main/resources/configuration.json")) {
                        config = gson.fromJson(reader, Configuration.class); //retrieving the configuration from a JSON file
                    } catch (FileNotFoundException e) {
                        logger.error(e.getMessage());
                        logger.error("Configuration file not found. Set absolute path to the JSON file.");
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                        throw new RuntimeException(e);
                    } catch (Exception e){
                        logger.error(e.getMessage());
                    }

                    if (config == null) {
                        logger.error("Configuration could not be loaded. Exiting...");
                        System.out.println("Failed to load configuration. Please check the file and try again.");
                        System.exit(1);
                    }
                    validOption = true;
                    break;
                default:
                    System.out.println("Invalid option selected.");
            }
        }
        logger.info("Configuration successful: " + config);

        // Initialize the TicketPool with configuration parameters
        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());

        // Validate and get the number of vendor and customer threads
        int vendors = validateThreads("vendor");
        int customers = validateThreads("customer");

        // Options to start the simulation or quit
        validOption = false;
        while (!validOption) {
            System.out.print("Enter \"a\" to start the simulation or \"b\" to quit: ");
            String opt = scanner.next();
            switch (opt) {
                case "a":
                    logger.info("Starting simulation...");
                    // Create and start vendor threads
                    for (int i = 0; i < vendors; i++) {
                        String vendorID = ("V00"+(i+1));
                        Vendor vendor = new Vendor(vendorID, ticketPool, config.getTicketReleaseRate());
                        Thread vendorThread = new Thread(vendor, "Vendor-" + (i + 1));

                        vendorThread.start();
                    }
                    // Create and start customer threads
                    for (int i = 0; i < customers; i++) {
                        String customerID = "C00" + (i + 1);
                        Customer customer = new Customer(customerID, ticketPool, config.getCustomerRetrievalRate());
                        Thread customerThread = new Thread(customer, "Customer-" + (i + 1));

                        customerThread.start();
                    }
                    validOption = true;
                    break;
                case "b":
                    logger.info("User quitting...");
                    validOption = true;
                    break;
                default:
                    System.out.println("Invalid option selected.");
            }
        }
    }

    /**
     * Prompots for and validates user input for the number of threads.
     *
     * @param threadType The type of thread ("vendor" or "customer")
     * @return The validated number of threads
     */
    public static int validateThreads(String threadType){
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;
        int temp=0; //temporarily holds the value for the input to check for positive integers

        while (!validInput) {
            try {
                System.out.print("Enter number of "+threadType+" threads: ");
                temp = scanner.nextInt();
                if (temp <= 0) {
                    System.out.println("The number of threads must be a positive number.");
                } else {
                    validInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer for the number of "+threadType+"s.");
                scanner.next();
            }
        }

        return temp;
    }

}
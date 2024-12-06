package org.example;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Gson gson = new Gson();
        Configuration config = null;

        Scanner scanner = new Scanner(System.in);

        boolean validOption = false;
        while (!validOption) {
            System.out.print("Enter \"a\" for new configuration or \"b\" to use previous configuration: ");
            String opt = scanner.next();
            switch (opt) {
                case "a":
                    config = Configuration.getUserConfiguration();
                    System.out.println("Configuration successful: " + config);

                    // Save configuration to file (unchanged)
                    try (FileWriter writer = new FileWriter("/Users/chanirumannapperuma/Downloads/RealTimeTicketing/src/main/resources/configuration.json")) {
                        gson.toJson(config, writer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    validOption = true;
                    break;
                case "b":
                    try (FileReader reader = new FileReader("/Users/chanirumannapperuma/Downloads/RealTimeTicketing/src/main/resources/configuration.json")) {
                        config = gson.fromJson(reader, Configuration.class);
                    } catch (FileNotFoundException e) {
                        // Handle file not found case (optional)
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Configuration successful: " + config);
                    validOption = true;
                    break;
                default:
                    System.out.println("Invalid option selected.");
            }
        }

        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());


        int vendors = 0;
        int customers = 0;


        boolean validVendorsInput = false;
        boolean validCustomersInput = false;

// Loop for validating vendor input
        while (!validVendorsInput) {
            try {
                System.out.print("Enter number of Vendors: ");
                vendors = scanner.nextInt();
                if (vendors <= 0) {
                    System.out.println("Vendors must be a positive number.");
                } else {
                    validVendorsInput = true; // Exit the loop when the input is valid
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer for the number of Vendors.");
                scanner.next(); // Clear the invalid input from the buffer
            }
        }

// Loop for validating customer input
        while (!validCustomersInput) {
            try {
                System.out.print("Enter number of Customers: ");
                customers = scanner.nextInt();
                if (customers <= 0) {
                    System.out.println("Customers must be a positive number.");
                } else {
                    validCustomersInput = true; // Exit the loop when the input is valid
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer for the number of Customers.");
                scanner.next(); // Clear the invalid input from the buffer
            }
        }

        for (int i = 0; i < vendors; i++) {
            String vendorID = ("V00"+(i+1));
            Vendor vendor = new Vendor(vendorID, ticketPool, config.getTicketReleaseRate());
            Thread vendorThread = new Thread(vendor, "Vendor-" + (i + 1));

            vendorThread.start();
        }


        for (int i = 0; i < customers; i++) {
            String customerID = "C00" + (i + 1);
            Customer customer = new Customer(customerID, ticketPool, config.getCustomerRetrievalRate());
            Thread customerThread = new Thread(customer, "Customer-" + (i + 1));
            customerThread.start();
        }
    }

}

# Real-Time Ticketing System - Java CLI

This project simulates a real-time ticketing system using multithreading and concurrency in Java. It features vendors releasing tickets into a shared pool and customers purchasing tickets concurrently. The simulation employs thread-safe data structures and synchronization mechanisms to ensure consistent behavior.

## Features
- **Multithreading:**
    - Vendors and customers operate concurrently.
    - Thread-safe interactions between multiple threads.
- **Concurrency Management:**
    - Reentrant locks and conditions ensure proper synchronization.
- **Logging:**
    - Uses Log4j2 for detailed logging of events.
- **Dynamic Configuration:**
    - Supports setting parameters like ticket pool capacity, release rate, and retrieval rate.


## File Structure

| File/Directory         | Description                                                                                      |
|------------------------|--------------------------------------------------------------------------------------------------|
| **Main.java**          | Entry point for the application. Manages configuration, thread creation, and simulation control. |
| **Configuration.java** | Handles configuration details (e.g., ticket release rate, pool capacity) and user input.         |
| **Vendor.java**        | Simulates ticket vendors, releasing tickets into the pool. Implements Runnable.                  |
| **Customer.java**      | Simulates customers purchasing tickets from the pool. Implements Runnable.                       |
| **TicketPool.java**    | Manages the shared pool of tickets with thread-safe operations.                                  |
| **Ticket.java**        | Represents individual tickets with properties like ID, event name, and price.                    |
| **log4j2.xml**         | Log4j2 configuration file for logging setup.                                                     |
| **pom.xml**            | Maven configuration file with dependencies for Log4j2 and Gson.                                  |

## Classes
1. **Main.java**
   **Purpose:** Entry point for managing the ticketing system simulation, configuration setup, and thread creation for vendors and customers.

**Methods:**
- main(String[] args):
    - Starts the simulation by prompting the user for configuration and thread setup.
    - Initializes the ticket pool, vendor, and customer threads.
    - Handles user choices for starting or quitting the simulation.
- validateThreads(String threadType):
    - Prompts the user for the number of vendor or customer threads.
    - Validates the input ensuring it’s a positive integer.

2. **Configuration.java**

**Purpose:** Manages the configuration settings for the simulation, including ticket release rate, customer retrieval rate, max ticket capacity, and total tickets.

**Properties:**
- **ticketReleaseRate:** Rate at which tickets are released by vendors (in seconds).
- **customerRetrievalRate:** Rate at which customers attempt to retrieve tickets (in seconds).
- **maxTicketCapacity:** The maximum number of tickets the pool can hold.
- **totalTickets:** The total number of tickets available for sale.

**Methods:**
- **Configuration(int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity, int totalTickets):** Constructor for initializing a configuration instance with the specified values.
- **getTicketReleaseRate():** Returns the ticket release rate.
- **getCustomerRetrievalRate():** Returns the customer retrieval rate.
- **getMaxTicketCapacity():** Returns the maximum ticket pool capacity.
- **getTotalTickets():** Returns the total number of tickets.
- **setTicketReleaseRate(int ticketReleaseRate):** Sets the ticket release rate.
- **setCustomerRetrievalRate(int customerRetrievalRate):** Sets the customer retrieval rate.
- **setMaxTicketCapacity(int maxTicketCapacity):** Sets the maximum ticket pool capacity.
- **setTotalTickets(int totalTickets):** Sets the total number of tickets available.
- **toString():** Returns a string representation of the configuration.
- **getUserConfiguration():** Prompts the user to input configuration values and returns a new Configuration object.
- **getValidatedInput(Scanner scanner, String prompt):** Prompts for and validates positive integer inputs from the user.

3. **Customer.java**

**Purpose:** Represents a customer that attempts to retrieve tickets from the ticket pool at a specified retrieval rate. Implements the Runnable interface for multithreading.

**Properties:**
- **ticketPool:** The shared ticket pool from which customers retrieve tickets.
- **customerID:** Unique ID for the customer.
- **retrievalRate:** Rate at which the customer attempts to retrieve tickets (in seconds).

**Methods:**
- **Customer(String customerID, TicketPool ticketPool, int retrievalRate):** Constructor for initializing a customer with the specified ID, ticket pool, and retrieval rate.
- **run():** Runs in a separate thread and attempts to purchase tickets from the pool at the specified retrieval rate.
  Increments the global ticket sold count using AtomicInteger.

4. **Vendor.java**

**Purpose:** Represents a vendor that adds tickets to the ticket pool at a specified release rate. Implements the Runnable interface for multithreading.

**Properties:**
- **ticketPool:** The shared ticket pool to which the vendor adds tickets.
- **vendorID:** Unique ID for the vendor.
- **releaseRate:** Rate at which the vendor releases tickets (in seconds).
- **ticketsAdded:** Tracks the number of tickets added by this vendor.

**Methods:**
- **Vendor(String vendorID, TicketPool ticketPool, int releaseRate):** Constructor for initializing the vendor with a unique ID, ticket pool, and release rate.
- **run():** Runs in a separate thread and adds tickets to the pool at the specified release rate.
  Stops when the total number of tickets reaches the configured limit.

5. **Ticket.java**

**Purpose:** Represents an individual ticket with properties like ID, event name, and price.

**Properties:**
- **ticketId:** Unique ID for the ticket.
- **eventName:** Name of the event for which the ticket is valid.
- **ticketPrice:** Price of the ticket.

**Methods:**
- **Ticket(int ticketId, String eventName, BigDecimal ticketPrice):** Constructor for creating a new ticket with a unique ID, event name, and price.
- **getTicketId():** Returns the ticket ID.
- **getEventName():** Returns the event name.
- **getTicketPrice():** Returns the ticket price.
- **toString():** Returns a string representation of the ticket.

5. **TicketPool.java**

**Purpose:** Manages the shared pool of tickets. Provides thread-safe operations for adding and retrieving tickets using ReentrantLock and Condition for synchronization.

**Properties:**
- **tickets:** List that holds the tickets in the pool.
- **maxTicketCapacity:** Maximum number of tickets that the pool can hold.
- **totalTickets:** The total number of tickets to be sold.
- **lock:** Reentrant lock to ensure thread-safety when accessing the ticket pool.
- **notEmpty:** Condition to signal when tickets are available for customers.
- **notFull:** Condition to signal when space is available for vendors to add tickets.
- **ticketsSold:* Tracks the total number of tickets sold.
- **count:** Tracks the total number of tickets added.

**Methods**
- **TicketPool(int maxTicketCapacity, int totalTickets):** Constructor that initializes the ticket pool with maximum capacity and total tickets.
- **addTickets(Ticket ticket):** Adds a ticket to the pool in a thread-safe manner. Notifies customers when tickets are available.
- **removeTickets():** Removes a ticket from the pool and sells it to a customer. Notifies vendors when space is available.
- **getTicketsSize():** Returns the current size of the ticket pool.
- **getMaxTicketCapacity():** Returns the maximum ticket capacity of the pool.
- **getTotalTickets():** Returns the total number of tickets to be sold.
- **getTicketsSold():** Returns the total number of tickets sold.
- **getCount():** Returns the total number of tickets added to the pool.
- **setTicketsSold(int ticketsSold):** Sets the total number of tickets sold.
- **setCount(int count):** Sets the total number of tickets added.

## Prerequisites

    1.	Java JDK 21 or later.
	2.	Maven for dependency management.

## Setup Instructions
### Using IntelliJ IDEA
1.	**Import the Project:**
- Open IntelliJ IDEA.
- Click File > Open and select the project folder.
2.	**Setup Maven:**
- IntelliJ should automatically recognize the pom.xml file and download dependencies.
3.	**Run the Application:**
- Open Main.java in the editor.
- Click the green Run icon or right-click and select Run ‘Main.main()’.

## Usage
1.	Configure the Simulation:
- Choose to set a new configuration or use a previous one saved in configuration.json.
- Define the total number of tickets, ticket pool capacity, release rate, and retrieval rate.
2.	Specify Thread Counts:
- Input the number of vendor and customer threads.
3. Run or Exit:
- Choose to start the simulation or exit the program.
4. Observe Logs:
- Logs are saved in Logs/ticketing.log and displayed in the console.


## Troubleshooting
- **Logs Not Generated:** Ensure the Logs directory exists and is writable.
- **Thread Synchronization Issues:** Confirm all threads are managed correctly by monitoring log outputs.
- **Maven Dependency Issues:** Run mvn clean install to resolve dependency conflicts.
- **JSON File Errors:** Ensure configuration.json exists and is properly formatted when using a saved configuration.
import java.util.Scanner;

/**
 * Main - entry point of the Car Rental Management System (Assignment 2).
 *
 * It creates the fleet (Car, ElectricCar, SportsCar), a Customer and the
 * RentalAgency, then runs a menu-driven loop. The menu uses while/for loops and
 * if / else-if selection to call methods on all the classes.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);

    /** Reads one line of input. When input is redirected from a file (no console),
     *  the typed text is echoed so a saved test run still shows the user's answers. */
    private static String readLine() {
        String line = scanner.nextLine();
        if (System.console() == null) {
            System.out.println(line);
        }
        return line;
    }

    /** Reads a whole number safely; keeps asking until the user types a valid one. */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(readLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a whole number.");
            }
        }
    }

    /** Reads a decimal number safely. */
    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(readLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a number.");
            }
        }
    }

    /** Prints the menu. */
    private static void showMenu() {
        System.out.println("\n----- MENU -----");
        System.out.println("1. View fleet");
        System.out.println("2. Rent a car");
        System.out.println("3. Drive a rented car");
        System.out.println("4. Refuel / charge a rented car");
        System.out.println("5. Return a car");
        System.out.println("6. Polymorphism demo (all car types)");
        System.out.println("7. View bookings and revenue");
        System.out.println("0. Exit");
    }

    public static void main(String[] args) {

        // ----- 1. Create the agency and the fleet (objects of base + derived classes) -----
        RentalAgency agency = new RentalAgency("MSU Car Rental");
        agency.addCar(new Car("Toyota", "Vios", 2023, "Silver", 15000, 60, false, 120));
        agency.addCar(new Car("Honda", "City", 2024, "Blue", 8000, 45, false, 130));
        agency.addCar(new ElectricCar("Tesla", "Model3", 2024, "White", 5000, 80, 60, 250));
        agency.addCar(new SportsCar("Porsche", "911", 2023, "Red", 3000, 70, 308, 600));

        // ----- 2. Create the customer -----
        System.out.println("Welcome to " + agency.getAgencyName() + "!");
        System.out.print("Enter your name: ");
        String name = readLine().trim();
        System.out.print("Enter your licence number: ");
        String licence = readLine().trim();
        Customer customer = new Customer(name, "C001", licence);
        customer.displayInfo();

        // ----- 3. Menu loop -----
        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Enter choice: ");

            if (choice == 1) {
                agency.listFleet();

            } else if (choice == 2) {
                agency.listFleet();
                int carNo = readInt("Car number to rent: ");
                int days = readInt("Number of days: ");
                agency.rentCar(customer, carNo, days);

            } else if (choice == 3) {
                int id = readInt("Booking number: ");
                RentalBooking b = agency.findActiveBooking(id);
                if (b == null) {
                    System.out.println("  No active booking with that number.");
                } else {
                    Car car = b.getCar();
                    car.startEngine();
                    // instanceof lets us use features that exist only in SportsCar
                    if (car instanceof SportsCar) {
                        int turbo = readInt("Activate turbo? 1 = Yes, 0 = No: ");
                        if (turbo == 1) {
                            ((SportsCar) car).activateTurbo();
                        }
                    }
                    // for loop: drive three trips
                    for (int trip = 1; trip <= 3; trip++) {
                        double km = readDouble("Trip " + trip + " distance (km): ");
                        car.drive(km);          // polymorphic: version depends on car type
                    }
                    car.displayInfo();          // polymorphic
                }

            } else if (choice == 4) {
                int id = readInt("Booking number: ");
                RentalBooking b = agency.findActiveBooking(id);
                if (b == null) {
                    System.out.println("  No active booking with that number.");
                } else {
                    double amount = readDouble("Amount to add (%): ");
                    b.getCar().refuel(amount);  // polymorphic: fuel or battery
                }

            } else if (choice == 5) {
                int id = readInt("Booking number to return: ");
                agency.returnCar(id);

            } else if (choice == 6) {
                // ----- Polymorphism demo: one loop, one reference type, four behaviours -----
                System.out.println("\n=== POLYMORPHISM DEMO (3-day rental, 50 km drive) ===");
                for (int i = 0; i < agency.getFleetSize(); i++) {
                    Car c = agency.getCar(i);
                    System.out.println("\n" + (i + 1) + ". " + c.getCarType() + " car");
                    c.displayInfo();
                    System.out.printf("  3-day rental cost: RM%.2f%n", c.calculateRentalCost(3));
                    if (!c.isEngineOn()) {
                        c.startEngine();
                    }
                    c.drive(50);
                    c.stopEngine();
                }

            } else if (choice == 7) {
                agency.listBookings();
                customer.displayInfo();

            } else if (choice == 0) {
                running = false;
                System.out.println("\nThank you for using " + agency.getAgencyName() + ". Goodbye!");

            } else {
                System.out.println("  Invalid choice. Please select 0 - 7.");
            }
        }
        scanner.close();
    }
}

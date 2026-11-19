import java.util.ArrayList;

/**
 * RentalAgency - ADDITIONAL CLASS 3.
 *
 * Purpose: the manager of the whole system. It stores the fleet (a list of Car
 * objects - which may be Car, ElectricCar or SportsCar), the customers and the
 * bookings, and provides the operations the menu needs (list, rent, return, revenue).
 */
public class RentalAgency {

    private String agencyName;
    private ArrayList<Car> fleet = new ArrayList<>();
    private ArrayList<RentalBooking> bookings = new ArrayList<>();
    private int nextBookingId = 1;
    private double revenue = 0;

    public RentalAgency(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyName() { return agencyName; }
    public double getRevenue()    { return revenue; }
    public int getFleetSize()     { return fleet.size(); }
    public Car getCar(int index)  { return fleet.get(index); }

    /** Polymorphism: a Car reference can hold a Car, ElectricCar or SportsCar. */
    public void addCar(Car car) {
        fleet.add(car);
    }

    /** Loops through the fleet and prints every car with its number. */
    public void listFleet() {
        System.out.println("\n=== " + agencyName + " - FLEET ===");
        for (int i = 0; i < fleet.size(); i++) {
            Car c = fleet.get(i);
            String status = c.isAvailable() ? "Available" : "Rented";
            System.out.printf("  %d. %-7s %-8s %-8s RM%6.2f/day  %s%n", (i + 1), c.getCarType(),
                    c.getBrand(), c.getModel(), c.getDailyRate(), status);
        }
    }

    /** Creates a booking if the car number is valid and the car is available. */
    public RentalBooking rentCar(Customer customer, int carNumber, int days) {
        if (carNumber < 1 || carNumber > fleet.size()) {
            System.out.println("  Invalid car number.");
            return null;
        }
        if (days < 1) {
            System.out.println("  Rental period must be at least 1 day.");
            return null;
        }
        Car car = fleet.get(carNumber - 1);
        if (!car.isAvailable()) {
            System.out.println("  Sorry, that car is already rented.");
            return null;
        }
        RentalBooking booking = new RentalBooking(nextBookingId++, customer, car, days);
        bookings.add(booking);
        System.out.println("  Booking confirmed!");
        booking.displaySummary();
        return booking;
    }

    /** Searches the booking list for an active booking with the given id. */
    public RentalBooking findActiveBooking(int bookingId) {
        for (RentalBooking b : bookings) {
            if (b.getBookingId() == bookingId && b.isActive()) {
                return b;
            }
        }
        return null;
    }

    /** Returns a car: stops the engine, closes the booking and adds to revenue. */
    public void returnCar(int bookingId) {
        RentalBooking booking = findActiveBooking(bookingId);
        if (booking == null) {
            System.out.println("  No active booking with that number.");
        } else {
            booking.getCar().stopEngine();
            booking.completeBooking();
            revenue += booking.getTotalCost();
            System.out.printf("  Car returned. Amount paid: RM%.2f%n", booking.getTotalCost());
        }
    }

    public void listBookings() {
        System.out.println("\n=== BOOKINGS ===");
        if (bookings.isEmpty()) {
            System.out.println("  No bookings yet.");
        }
        for (RentalBooking b : bookings) {
            b.displaySummary();
        }
        System.out.printf("  Total revenue collected: RM%.2f%n", revenue);
    }
}

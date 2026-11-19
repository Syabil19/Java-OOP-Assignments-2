/**
 * RentalBooking - ADDITIONAL CLASS 2.
 *
 * Purpose: links ONE Customer to ONE Car for a number of days (composition of
 * objects). The price is obtained by calling car.calculateRentalCost(days), so the
 * correct pricing rule is chosen at run time (polymorphism).
 */
public class RentalBooking {

    private int bookingId;
    private Customer customer;
    private Car car;
    private int days;
    private double totalCost;
    private boolean active;        // true = car still with customer

    public RentalBooking(int bookingId, Customer customer, Car car, int days) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.car = car;
        this.days = days;
        this.active = true;

        double cost = car.calculateRentalCost(days);   // polymorphic call
        if (customer.isLoyal()) {
            cost = cost * 0.95;                        // 5% loyalty discount
        }
        this.totalCost = cost;
        car.setAvailable(false);                       // car is now rented out
    }

    public int getBookingId()     { return bookingId; }
    public Customer getCustomer() { return customer; }
    public Car getCar()           { return car; }
    public int getDays()          { return days; }
    public double getTotalCost()  { return totalCost; }
    public boolean isActive()     { return active; }

    /** Closes the booking, frees the car and updates the customer's history. */
    public void completeBooking() {
        active = false;
        car.setAvailable(true);
        customer.addCompletedRental();
    }

    public void displaySummary() {
        System.out.println("  Booking #" + bookingId + " | " + customer.getName() + " | "
                + car.getBrand() + " " + car.getModel() + " | " + days + " day(s) | RM"
                + String.format("%.2f", totalCost) + " | " + (active ? "ACTIVE" : "RETURNED"));
    }
}

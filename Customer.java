/**
 * Customer - ADDITIONAL CLASS 1.
 *
 * Purpose: represents a person who rents cars from the agency. It keeps the
 * customer's personal data private and counts how many rentals the customer made.
 */
public class Customer {

    private String name;            // full name
    private String customerId;      // unique id, e.g. C001
    private String licenseNumber;   // driving licence number (private data)
    private int totalRentals;       // number of rentals completed

    public Customer(String name, String customerId, String licenseNumber) {
        this.name = name;
        this.customerId = customerId;
        this.licenseNumber = licenseNumber;
        this.totalRentals = 0;
    }

    public String getName()          { return name; }
    public String getCustomerId()    { return customerId; }
    public String getLicenseNumber() { return licenseNumber; }
    public int getTotalRentals()     { return totalRentals; }

    public void setName(String name) { this.name = name; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    /** Called by the agency whenever the customer finishes a rental. */
    public void addCompletedRental() {
        totalRentals++;
    }

    /** True for customers with 2 or more completed rentals (gets loyalty discount). */
    public boolean isLoyal() {
        return totalRentals >= 2;
    }

    public void displayInfo() {
        System.out.println("  Customer " + customerId + ": " + name
                + " | Licence: " + licenseNumber + " | Rentals: " + totalRentals);
    }
}

/**
 * RentalPricing - common interface for every rentable vehicle.
 *
 * Purpose: defines ONE method signature that all car types must provide, but each
 * class is free to implement it differently. This is how the application achieves
 * polymorphism through an interface: code that holds a RentalPricing/Car reference
 * can call calculateRentalCost() without knowing the exact type of car.
 */
public interface RentalPricing {

    /** Returns the total rental price (in RM) for the given number of days. */
    double calculateRentalCost(int days);

    /** Returns a short label describing the type of car (e.g. "Standard"). */
    String getCarType();
}

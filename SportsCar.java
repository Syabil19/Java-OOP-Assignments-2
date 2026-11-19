/**
 * SportsCar - DERIVED CLASS (child) of Car.
 *
 * Inherits everything from Car and adds performance features:
 *   - new attributes : topSpeed, turboOn
 *   - new methods    : activateTurbo(), deactivateTurbo()
 *   - overridden     : getConsumptionPerKm(), drive(), displayInfo(),
 *                      calculateRentalCost(), getCarType()
 */
public class SportsCar extends Car {

    private int topSpeed;        // maximum speed in km/h
    private boolean turboOn;     // true = turbo boost active

    /** Sports cars burn more fuel per km than a standard car. */
    private static final double SPORTS_FUEL_PER_KM = 0.15;

    public SportsCar(String brand, String model, int year, String color,
                     double mileage, double fuelLevel, int topSpeed, double dailyRate) {
        super(brand, model, year, color, mileage, fuelLevel, false, dailyRate);
        this.topSpeed = topSpeed;
        this.turboOn = false;
    }

    // ----- getters and setters for the NEW attributes -----
    public int getTopSpeed()       { return topSpeed; }
    public boolean isTurboOn()     { return turboOn; }

    public void setTopSpeed(int topSpeed) {
        if (topSpeed > 0) {
            this.topSpeed = topSpeed;
        }
    }

    // ----- new behaviour -----
    public void activateTurbo() {
        if (!isEngineOn()) {
            System.out.println("  Start the engine before using turbo.");
        } else {
            turboOn = true;
            System.out.println("  TURBO activated!");
        }
    }

    public void deactivateTurbo() {
        turboOn = false;
        System.out.println("  Turbo deactivated.");
    }

    // ----- OVERRIDDEN methods -----

    /** With turbo on, the car uses 50% more fuel for every km. */
    @Override
    public double getConsumptionPerKm() {
        double rate = SPORTS_FUEL_PER_KM;
        if (turboOn) {
            rate = rate * 1.5;
        }
        return rate;
    }

    /** Reuses the base drive() logic, then prints a sports-specific message. */
    @Override
    public void drive(double distance) {
        super.drive(distance);
        if (turboOn && distance > 0 && isEngineOn()) {
            System.out.println("  (Turbo boost used - top speed " + topSpeed + " km/h)");
        }
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("  Top speed: " + topSpeed + " km/h | Turbo on: " + turboOn);
    }

    /** Premium pricing: 30% performance surcharge plus RM50 per day insurance. */
    @Override
    public double calculateRentalCost(int days) {
        return (getDailyRate() * days * 1.30) + (50.0 * days);
    }

    @Override
    public String getCarType() {
        return "Sports";
    }
}

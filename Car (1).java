/**
 * Car - BASE CLASS (parent) of the whole car hierarchy.
 *
 * Continues the Car class from Assignment 1. It stores the common state of every
 * car (brand, model, year, colour, mileage, fuel level, engine status) and, for the
 * rental system, a daily rental rate and an availability flag.
 *
 * Encapsulation: every attribute is PRIVATE. Other classes (including the derived
 * classes) must use the public getters and setters, which validate the data.
 */
public class Car implements RentalPricing {

    // ===== Attributes (all private = encapsulation) =====
    private String brand;        // manufacturer, e.g. Toyota
    private String model;        // model name, e.g. Vios
    private int year;            // year of manufacture
    private String color;        // body colour
    private double mileage;      // total distance travelled in km
    private double fuelLevel;    // fuel level in percent (0 - 100)
    private boolean engineOn;    // true = engine running, false = engine off
    private double dailyRate;    // rental price per day in RM
    private boolean available;   // true = free to rent, false = currently rented

    /** Fuel (in percent) used for every kilometre driven by a standard car. */
    private static final double FUEL_PER_KM = 0.08;

    // ===== Constructors =====
    /** Default constructor: gives every attribute a safe starting value. */
    public Car() {
        this("Unknown", "Unknown", 0, "Unknown", 0, 0, false, 0);
    }

    /** Parameterized constructor: sets all values when the object is created.
     *  Setters are used so the validation rules also apply at construction time. */
    public Car(String brand, String model, int year, String color,
               double mileage, double fuelLevel, boolean engineOn, double dailyRate) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        setMileage(mileage);
        setFuelLevel(fuelLevel);
        this.engineOn = engineOn;
        setDailyRate(dailyRate);
        this.available = true;      // every new car starts as available
    }

    // ===== Getters =====
    public String getBrand()      { return brand; }
    public String getModel()      { return model; }
    public int getYear()          { return year; }
    public String getColor()      { return color; }
    public double getMileage()    { return mileage; }
    public double getFuelLevel()  { return fuelLevel; }
    public boolean isEngineOn()   { return engineOn; }
    public double getDailyRate()  { return dailyRate; }
    public boolean isAvailable()  { return available; }

    // ===== Setters =====
    public void setBrand(String brand)   { this.brand = brand; }
    public void setModel(String model)   { this.model = model; }
    public void setYear(int year)        { this.year = year; }
    public void setColor(String color)   { this.color = color; }
    public void setEngineOn(boolean engineOn)   { this.engineOn = engineOn; }
    public void setAvailable(boolean available) { this.available = available; }

    /** Validation: mileage can never be negative. */
    public void setMileage(double mileage) {
        if (mileage >= 0) {
            this.mileage = mileage;
        }
    }

    /** Validation: fuel level is always kept between 0 and 100. */
    public void setFuelLevel(double fuelLevel) {
        if (fuelLevel < 0) {
            this.fuelLevel = 0;
        } else if (fuelLevel > 100) {
            this.fuelLevel = 100;
        } else {
            this.fuelLevel = fuelLevel;
        }
    }

    /** Validation: a rental rate cannot be negative. */
    public void setDailyRate(double dailyRate) {
        if (dailyRate >= 0) {
            this.dailyRate = dailyRate;
        }
    }

    // ===== Action methods (can be OVERRIDDEN by derived classes) =====

    /** Starts the engine only if it is not already running. */
    public void startEngine() {
        if (!engineOn) {
            engineOn = true;
            System.out.println("  The engine has started.");
        } else {
            System.out.println("  The engine is already running.");
        }
    }

    /** Stops the engine only if it is currently running. */
    public void stopEngine() {
        if (engineOn) {
            engineOn = false;
            System.out.println("  The engine has stopped.");
        } else {
            System.out.println("  The engine is already off.");
        }
    }

    /** Fuel used per km. Derived classes may override it to change consumption. */
    public double getConsumptionPerKm() {
        return FUEL_PER_KM;
    }

    /** Drives a distance: validates input, then increases mileage and reduces fuel. */
    public void drive(double distance) {
        if (distance <= 0) {
            System.out.println("  Distance must be more than 0.");
        } else if (!engineOn) {
            System.out.println("  Start the engine first.");
        } else if (fuelLevel <= 0) {
            System.out.println("  No fuel left. Please refuel.");
        } else {
            double fuelNeeded = distance * getConsumptionPerKm();
            if (fuelNeeded > fuelLevel) {
                // not enough fuel: travel only as far as the remaining fuel allows
                double possible = fuelLevel / getConsumptionPerKm();
                setMileage(mileage + possible);
                setFuelLevel(0);
                System.out.printf("  The car ran out of fuel after %.1f km.%n", possible);
            } else {
                setMileage(mileage + distance);
                setFuelLevel(fuelLevel - fuelNeeded);
                System.out.printf("  The car drove %.1f km.%n", distance);
            }
        }
    }

    /** Adds fuel to the tank (the level can never go above 100%). */
    public void refuel(double amount) {
        if (amount <= 0) {
            System.out.println("  Fuel amount must be more than 0.");
        } else {
            setFuelLevel(fuelLevel + amount);   // setter caps the value at 100
            System.out.printf("  Fuel level: %.1f%%%n", fuelLevel);
        }
    }

    /** Prints the state of the car. Derived classes override it and add their own lines. */
    public void displayInfo() {
        System.out.println("  [" + getCarType() + "] " + brand + " " + model + " (" + year + ")");
        System.out.println("  Colour: " + color + " | Mileage: " + String.format("%.1f", mileage) + " km");
        System.out.println("  " + getEnergyInfo() + " | Engine on: " + engineOn
                + " | Available: " + available);
        System.out.println("  Rate: RM" + String.format("%.2f", dailyRate) + " per day");
    }

    /** Describes the energy level. ElectricCar overrides this to describe its battery. */
    public String getEnergyInfo() {
        return "Fuel: " + String.format("%.1f", fuelLevel) + "%";
    }

    // ===== RentalPricing interface methods (overridden by derived classes) =====

    /** Standard pricing: rate x days, with a 10% discount for rentals of 7 days or more. */
    @Override
    public double calculateRentalCost(int days) {
        double total = dailyRate * days;
        if (days >= 7) {
            total = total * 0.90;      // weekly discount
        }
        return total;
    }

    @Override
    public String getCarType() {
        return "Standard";
    }
}

/**
 * ElectricCar - DERIVED CLASS (child) of Car.
 *
 * Inherits all Car attributes and methods and EXTENDS them with battery features:
 *   - new attributes : batteryCapacityKWh, batteryLevel
 *   - new methods    : chargeBattery(), getEstimatedRangeKm()
 *   - overridden     : drive(), refuel(), getEnergyInfo(), displayInfo(),
 *                      calculateRentalCost(), getCarType()
 */
public class ElectricCar extends Car {

    private double batteryCapacityKWh;   // size of the battery in kWh
    private double batteryLevel;         // battery charge in percent (0 - 100)

    /** Battery percent used per km (electric cars are more efficient than petrol). */
    private static final double BATTERY_PER_KM = 0.20;

    public ElectricCar(String brand, String model, int year, String color,
                       double mileage, double batteryLevel, double batteryCapacityKWh,
                       double dailyRate) {
        // super(...) calls the Car constructor; an electric car has no fuel (0)
        super(brand, model, year, color, mileage, 0, false, dailyRate);
        this.batteryCapacityKWh = batteryCapacityKWh;
        setBatteryLevel(batteryLevel);
    }

    // ----- getters and setters for the NEW attributes -----
    public double getBatteryCapacityKWh() { return batteryCapacityKWh; }
    public double getBatteryLevel()       { return batteryLevel; }

    public void setBatteryCapacityKWh(double kWh) {
        if (kWh > 0) {
            this.batteryCapacityKWh = kWh;
        }
    }

    /** Validation: battery level is kept between 0 and 100. */
    public void setBatteryLevel(double level) {
        if (level < 0) {
            this.batteryLevel = 0;
        } else if (level > 100) {
            this.batteryLevel = 100;
        } else {
            this.batteryLevel = level;
        }
    }

    /** New behaviour (not in Car): remaining range in km from the current charge. */
    public double getEstimatedRangeKm() {
        return batteryLevel / BATTERY_PER_KM;
    }

    /** New behaviour (not in Car): charges the battery by the given percentage. */
    public void chargeBattery(double percent) {
        if (percent <= 0) {
            System.out.println("  Charge amount must be more than 0.");
        } else {
            setBatteryLevel(batteryLevel + percent);
            System.out.printf("  Battery charged to %.1f%%%n", batteryLevel);
        }
    }

    // ----- OVERRIDDEN methods: same signature, different behaviour -----

    /** Electric driving uses the battery instead of fuel. */
    @Override
    public void drive(double distance) {
        if (distance <= 0) {
            System.out.println("  Distance must be more than 0.");
        } else if (!isEngineOn()) {
            System.out.println("  Switch on the motor first.");
        } else if (batteryLevel <= 0) {
            System.out.println("  Battery empty. Please charge.");
        } else {
            double needed = distance * BATTERY_PER_KM;
            if (needed > batteryLevel) {
                double possible = batteryLevel / BATTERY_PER_KM;
                setMileage(getMileage() + possible);
                setBatteryLevel(0);
                System.out.printf("  The battery ran out after %.1f km.%n", possible);
            } else {
                setMileage(getMileage() + distance);
                setBatteryLevel(batteryLevel - needed);
                System.out.printf("  The electric car drove %.1f km silently.%n", distance);
            }
        }
    }

    /** "Refuelling" an electric car means charging its battery. */
    @Override
    public void refuel(double amount) {
        chargeBattery(amount);
    }

    /** Overrides the energy description: an electric car reports battery, not fuel. */
    @Override
    public String getEnergyInfo() {
        return String.format("Battery: %.1f%% of %.0f kWh", batteryLevel, batteryCapacityKWh);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();    // reuse the base-class output, then add the extra line
        System.out.printf("  Estimated range: %.0f km (electric motor, no petrol used)%n",
                getEstimatedRangeKm());
    }

    /** Eco pricing: 10% green discount for rentals of 3 days or more. */
    @Override
    public double calculateRentalCost(int days) {
        double total = getDailyRate() * days;
        if (days >= 3) {
            total = total * 0.90;
        }
        return total;
    }

    @Override
    public String getCarType() {
        return "Electric";
    }
}

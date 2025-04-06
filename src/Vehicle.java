import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Vehicle implements Serializable {
  private String licensePlate;
  private String make;
  private String model;
  private int year;
  private VehicleStatus status;

  public enum VehicleStatus {
    AVAILABLE,
    RESERVED,
    RENTED,
    MAINTENANCE,
    OUTOFSERVICE
  }

  public Vehicle(String make, String model, int year) {
    this.make = this.capitalize(make);
    this.model = this.capitalize(model);

    this.year = year;
    this.status = VehicleStatus.AVAILABLE;
    this.licensePlate = null;
  }

  public Vehicle() {
    this(null, null, 0);
  }

  // Internal helpers
  private String capitalize(String str) {
    if (str == null || str.isEmpty()) return null;
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }

  // External api

  public void setLicensePlate(String plate) {
    if (!isValidPlate(plate)) {
      throw new IllegalArgumentException("Invalid license plate: " + plate);
    }
    this.licensePlate = plate.toUpperCase();
  }

  public void setStatus(VehicleStatus status) {
    this.status = status;
  }

  public String getLicensePlate() {
    return licensePlate;
  }

  public String getMake() {
    return make;
  }

  public String getModel() {
    return model;
  }

  public int getYear() {
    return year;
  }

  public VehicleStatus getStatus() {
    return status;
  }

  public String getInfo() {
    return "| "
        + licensePlate
        + " | "
        + make
        + " | "
        + model
        + " | "
        + year
        + " | "
        + status
        + " |";
  }

  private boolean isValidPlate(String plate) {
    // Empty or too long
    if (plate == null) return false;
    if (plate.isEmpty()) return false;
    // Check format: 3 letters, 3 digits
    Pattern pattern = Pattern.compile("^[A-Za-z]{3}[0-9]{3}$");
    Matcher matcher = pattern.matcher(plate);
    if (!matcher.matches()) return false;
    // It's valid
    return true;
  }
}

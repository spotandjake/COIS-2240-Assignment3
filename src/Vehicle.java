import java.io.Serializable;

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
    this.licensePlate = plate == null ? null : plate.toUpperCase();
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
}

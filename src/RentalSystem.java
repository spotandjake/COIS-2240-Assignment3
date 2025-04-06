import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalSystem {
  private static RentalSystem instance = null;

  private RentalSystem() {}

  public static RentalSystem getInstance() {
    return instance == null ? new RentalSystem() : instance;
  }

  private List<Vehicle> vehicles = new ArrayList<>();
  private List<Customer> customers = new ArrayList<>();
  private RentalHistory rentalHistory = new RentalHistory();

  public void addVehicle(Vehicle vehicle) {
    vehicles.add(vehicle);
    this.saveVehicle(vehicle);
  }

  public void addCustomer(Customer customer) {
    customers.add(customer);
    this.saveCustomer(customer);
  }

  public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
    if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
      vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
      // Note: It would be better to have an addRecord handlet this
      RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
      rentalHistory.addRecord(record);
      this.saveRecord(record);
      System.out.println("Vehicle rented to " + customer.getCustomerName());
    } else {
      System.out.println("Vehicle is not available for renting.");
    }
  }

  public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
    if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
      vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
      RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
      // Note: It would be better to have an addRecord handlet this
      rentalHistory.addRecord(record);
      this.saveRecord(record);
      System.out.println("Vehicle returned by " + customer.getCustomerName());
    } else {
      System.out.println("Vehicle is not rented.");
    }
  }

  public void displayAvailableVehicles() {
    System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    System.out.println(
        "---------------------------------------------------------------------------------");

    for (Vehicle v : vehicles) {
      if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
        System.out.println(
            "|     "
                + (v instanceof Car ? "Car          " : "Motorcycle   ")
                + "|\t"
                + v.getLicensePlate()
                + "\t|\t"
                + v.getMake()
                + "\t|\t"
                + v.getModel()
                + "\t|\t"
                + v.getYear()
                + "\t|\t");
      }
    }
    System.out.println();
  }

  public void displayAllVehicles() {
    for (Vehicle v : vehicles) {
      System.out.println("  " + v.getInfo());
    }
  }

  public void displayAllCustomers() {
    for (Customer c : customers) {
      System.out.println("  " + c.toString());
    }
  }

  public void displayRentalHistory() {
    for (RentalRecord record : rentalHistory.getRentalHistory()) {
      System.out.println(record.toString());
    }
  }

  public Vehicle findVehicleByPlate(String plate) {
    for (Vehicle v : vehicles) {
      if (v.getLicensePlate().equalsIgnoreCase(plate)) {
        return v;
      }
    }
    return null;
  }

  public Customer findCustomerById(int id) {
    for (Customer c : customers) if (c.getCustomerId() == id) return c;
    return null;
  }

  public Customer findCustomerByName(String name) {
    for (Customer c : customers) if (c.getCustomerName().equalsIgnoreCase(name)) return c;
    return null;
  }

  // File I/O methods to save and load vehicles and customers
  private void saveSerializable(String fileName, Serializable obj) {
    try {
      FileOutputStream file = new FileOutputStream(fileName, true); // Open in append mode
      ObjectOutputStream out = new ObjectOutputStream(file);
      // Serialize and write
      out.writeObject(obj);
      // Close stream
      out.close();
      file.close();
    } catch (Exception e) {
      System.out.println("Error writing to file: " + e.getMessage());
    }
  }

  private void saveVehicle(Vehicle obj) {
    this.saveSerializable("vehicles.txt", obj);
  }

  private void saveCustomer(Customer obj) {
    this.saveSerializable("customers.txt", obj);
  }

  private void saveRecord(RentalRecord obj) {
    this.saveSerializable("rental_records.txt", obj);
  }
}

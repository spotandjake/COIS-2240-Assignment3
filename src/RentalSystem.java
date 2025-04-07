import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalSystem {
  private static RentalSystem instance = null;

  private RentalSystem() {}

  public static RentalSystem getInstance() {
    if (instance == null) {
      instance = new RentalSystem();
      // Load data from files - Note important we do it here rather than in the constructor
      instance.loadData();
    }
    return instance;
  }

  private List<Vehicle> vehicles = new ArrayList<>();
  private List<Customer> customers = new ArrayList<>();
  private RentalHistory rentalHistory = new RentalHistory();

  public boolean addVehicle(Vehicle vehicle) {
    if (this.findVehicleByPlate(vehicle.getLicensePlate()) != null) {
      return false;
    }
    vehicles.add(vehicle);
    this.saveVehicle(vehicle);
    return true;
  }

  public boolean addCustomer(Customer customer) {
    if (this.findCustomerById(customer.getCustomerId()) != null) {
      return false;
    }
    customers.add(customer);
    this.saveCustomer(customer);
    return true;
  }

  public boolean rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
    if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
      vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
      // Note: It would be better to have an addRecord handlet this
      RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
      rentalHistory.addRecord(record);
      this.saveRecord(record);
      System.out.println("Vehicle rented to " + customer.getCustomerName());
      return true;
    } else {
      System.out.println("Vehicle is not available for renting.");
      return false;
    }
  }

  public boolean returnVehicle(
      Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
    if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
      vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
      RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
      // Note: It would be better to have an addRecord handlet this
      rentalHistory.addRecord(record);
      this.saveRecord(record);
      System.out.println("Vehicle returned by " + customer.getCustomerName());
      return true;
    } else {
      System.out.println("Vehicle is not rented.");
      return false;
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
      File file = new File(fileName);
      boolean fileExists = file.exists();
      FileOutputStream fileStream = new FileOutputStream(fileName, true); // Open in append mode
      ObjectOutputStream out =
          fileExists
              ? new AppendingObjectOutputStream(fileStream)
              : new ObjectOutputStream(fileStream);
      // Serialize and write
      out.writeObject(obj);
      // Close stream
      out.close();
      fileStream.close();
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

  private void loadVehicleData(String filePath) {
    FileInputStream fileStream = null;
    ObjectInputStream objStream = null;
    // Load Vehicles
    try {
      File file = new File(filePath);
      if (!file.exists()) return;
      fileStream = new FileInputStream(filePath);
      objStream = new ObjectInputStream(fileStream);
      // Deserialize and read
      while (true) {
        Object obj = objStream.readObject();
        if (!(obj instanceof Vehicle)) {
          System.out.println("Invalid object type in vehicles file.");
          continue; // Skip if not a Vehicle
        }
        // Add to vehicles list
        this.addVehicle((Vehicle) obj);
      }
    } catch (EOFException e) {
      // End of file reached, ignore this exception
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error reading from vehicle file: " + e.getMessage());
    } finally {
      try {
        if (objStream != null) objStream.close();
        if (fileStream != null) fileStream.close();
      } catch (Exception e) {
        System.out.println("Error closing file: " + e.getMessage());
      }
    }
  }

  private void loadCustomerData(String filePath) {
    FileInputStream fileStream = null;
    ObjectInputStream objStream = null;
    // Load Customers
    try {
      File file = new File(filePath);
      if (!file.exists()) return;
      fileStream = new FileInputStream(filePath);
      objStream = new ObjectInputStream(fileStream);
      // Deserialize and read
      while (true) {
        Object obj = objStream.readObject();
        if (!(obj instanceof Customer)) {
          System.out.println("Invalid object type in customers file.");
          continue; // Skip if not a Vehicle
        }
        // Add to vehicles list
        this.addCustomer((Customer) obj);
      }
    } catch (EOFException e) {
      // End of file reached, ignore this exception
    } catch (Exception e) {
      System.out.println("Error reading from customer file: " + e.getMessage());
    } finally {
      try {
        if (objStream != null) objStream.close();
        if (fileStream != null) fileStream.close();
      } catch (Exception e) {
        System.out.println("Error closing file: " + e.getMessage());
      }
    }
  }

  private void loadRentalData(String filePath) {
    FileInputStream fileStream = null;
    ObjectInputStream objStream = null;
    // Load Rental Records
    try {
      File file = new File(filePath);
      if (!file.exists()) return;
      fileStream = new FileInputStream(filePath);
      objStream = new ObjectInputStream(fileStream);
      // Deserialize and read
      while (true) {
        Object obj = objStream.readObject();
        if (!(obj instanceof RentalRecord)) {
          System.out.println("Invalid object type in rental records file.");
          continue; // Skip if not a Vehicle
        }
        // Add to vehicles list
        this.rentalHistory.addRecord((RentalRecord) obj);
      }
    } catch (EOFException e) {
      // End of file reached, ignore this exception
    } catch (Exception e) {
      System.out.println("Error reading from record file: " + e.getMessage());
    } finally {
      try {
        if (objStream != null) objStream.close();
        if (fileStream != null) fileStream.close();
      } catch (Exception e) {
        System.out.println("Error closing file: " + e.getMessage());
      }
    }
  }

  private void loadData() {
    // NOTE: We never save the updated vehicle data with the new status
    // NOTE: This was not in the instructions and doesn't fit into the design given
    // Load Vehicles
    this.loadVehicleData("vehicles.txt");
    // Load Customers
    this.loadCustomerData("customers.txt");
    // Load Rental Records
    this.loadRentalData("rental_records.txt");
  }
}

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;

public class RentalRecord implements Serializable {
  private transient Vehicle vehicle;
  private transient Customer customer;
  private LocalDate recordDate;
  private double totalAmount;
  private String recordType; // "RENT" or "RETURN"

  public RentalRecord(
      Vehicle vehicle,
      Customer customer,
      LocalDate recordDate,
      double totalAmount,
      String recordType) {
    this.vehicle = vehicle;
    this.customer = customer;
    this.recordDate = recordDate;
    this.totalAmount = totalAmount;
    this.recordType = recordType;
  }

  public Customer getCustomer() {
    return customer;
  }

  public Vehicle getVehicle() {
    return vehicle;
  }

  @Override
  public String toString() {
    return recordType
        + " | Plate: "
        + vehicle.getLicensePlate()
        + " | Customer: "
        + customer.getCustomerName()
        + " | Date: "
        + recordDate
        + " | Amount: $"
        + totalAmount;
  }

  // Serde
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject(); // Write the object - transient fields are skipped
    out.writeUTF(vehicle.getLicensePlate()); // Write the License Plate
    out.writeInt(customer.getCustomerId()); // Write the Customer ID
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject(); // Read the default object - transient fields are skipped
    String licensePlate = in.readUTF(); // Read the license plate
    int customerID = in.readInt(); // Read the customer ID
    // Lookup for
    RentalSystem rentalSystem = RentalSystem.getInstance();
    this.vehicle =
        rentalSystem.findVehicleByPlate(licensePlate); // Lookup vehicle using license plate
    this.customer = rentalSystem.findCustomerById(customerID); // Lookup customer using ID
  }
}

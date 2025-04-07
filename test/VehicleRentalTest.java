import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VehicleRentalTest {
  @BeforeEach
  public void setUp() {
    // Delete The save files
    File customers = new File("customers.txt");
    if (!customers.delete() && customers.exists()) {
      System.out.println("Failed to delete the file customers.");
    }
    // Delete the vehicles
    File vehicles = new File("vehicles.txt");
    if (!vehicles.delete() && vehicles.exists()) {
      System.out.println("Failed to delete the file vehicles.");
    }
    // Delete the rental history
    File rentalHistory = new File("rental_records.txt");
    if (!rentalHistory.delete() && rentalHistory.exists()) {
      System.out.println("Failed to delete the file rental records.");
    }
    // Create initial rentalSystem
    RentalSystem.getInstance();
  }

  @Test
  public void testLicensePlateValidation() {
    Vehicle vehicle = new Car("toyota", "corolla", 2020, 4);
    // Null Checks
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate(null));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate(""));
    // Length Checks
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("1"));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("12345"));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("12345678"));
    // Invalid Format
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("AAA1000"));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("ZZZ99"));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("1B1C1D"));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("12ABCD"));
    // Valid [A-Za-z]{3}[0-9]{3}
    assertAll(() -> vehicle.setLicensePlate("AAA100"));
    assertAll(() -> vehicle.setLicensePlate("ABC567"));
    assertAll(() -> vehicle.setLicensePlate("ZZZ999"));
  }

  @Test
  public void testRentAndReturnVehicle() {
    String plate = "ABC123";
    String customerName = "George";
    LocalDate date = LocalDate.of(2022, Month.JANUARY, 1); // Fixed date for testing
    double amt = 100.0;
    // Get Rental System
    RentalSystem rentalSystem = RentalSystem.getInstance();
    // Add Vehicle
    Vehicle vehicle = new Car("toyota", "corolla", 2020, 4);
    vehicle.setLicensePlate(plate);
    assertNull(rentalSystem.findVehicleByPlate(plate));
    rentalSystem.addVehicle(vehicle);
    assertNotNull(rentalSystem.findVehicleByPlate(plate));
    // Add Customer
    Customer customer = new Customer(1, customerName);
    assertNull(rentalSystem.findCustomerByName(customerName));
    rentalSystem.addCustomer(customer);
    assertNotNull(rentalSystem.findCustomerByName(customerName));
    // Ensure Renting works
    assertEquals(Vehicle.VehicleStatus.AVAILABLE, vehicle.getStatus());
    assertTrue(rentalSystem.rentVehicle(vehicle, customer, date, amt));
    assertEquals(Vehicle.VehicleStatus.RENTED, vehicle.getStatus());
    // Ensure Renting fails if already rented
    assertFalse(rentalSystem.rentVehicle(vehicle, customer, date, amt));
    // Ensure returning works
    assertTrue(rentalSystem.returnVehicle(vehicle, customer, date, amt));
    assertEquals(Vehicle.VehicleStatus.AVAILABLE, vehicle.getStatus());
    // Ensure returning fails if not rented
    assertFalse(rentalSystem.returnVehicle(vehicle, customer, date, amt));
  }
}

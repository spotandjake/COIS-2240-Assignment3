import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class VehicleRentalTest {
  @Test
  public void TestVehicle() {
    Vehicle vehicle = new Car("toyota", "corolla", 2020, 4);
    // Null Checks
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate(null));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate(""));
    // Length Checks
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("1"));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("12345"));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("12345678"));
    // Invalid Format
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("A1B1C1"));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("123ABC"));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("1B1C1D"));
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("12ABCD"));
    // Valid [A-Za-z]{3}[0-9]{3}
    assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("abc123"));
    assertAll(() -> vehicle.setLicensePlate("abc123"));
    assertAll(() -> vehicle.setLicensePlate("ABC123"));
    assertAll(() -> vehicle.setLicensePlate("xyz456"));
  }
}

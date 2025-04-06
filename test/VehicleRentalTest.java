import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class VehicleRentalTest {
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
}

package net.darmo_creations.naissancee;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {
  @Test
  void testTrueModuloPositiveLeftOpPositiveRightOpInsideRange() {
    assertEquals(5, Utils.trueModulo(5, 16));
  }

  @Test
  void testTrueModuloPositiveLeftOpPositiveRightOpOutsideRange() {
    assertEquals(0, Utils.trueModulo(16, 16));
    assertEquals(1, Utils.trueModulo(17, 16));
  }

  @Test
  void testTrueModuloPositiveLeftOpPositiveRightOpOutsideRangeMultipleTimes() {
    assertEquals(0, Utils.trueModulo(64, 16));
    assertEquals(1, Utils.trueModulo(65, 16));
  }

  @Test
  void testTrueModuloNegativeLeftPositiveRightOpOp() {
    assertEquals(15, Utils.trueModulo(-1, 16));
  }

  @Test
  void testTrueModuloNegativeLeftOpPositiveRightOpMultipleTimes() {
    assertEquals(15, Utils.trueModulo(-65, 16));
  }

  @Test
  void testTrueModuloPositiveLeftOpNegativeRightOpInsideRange() {
    assertEquals(-11, Utils.trueModulo(5, -16));
  }

  @Test
  void testTrueModuloPositiveLeftOpNegativeRightOpOutsideRange() {
    assertEquals(0, Utils.trueModulo(16, -16));
    assertEquals(-15, Utils.trueModulo(17, -16));
  }

  @Test
  void testTrueModuloPositiveLeftOpNegativeRightOpOutsideRangeMultipleTimes() {
    assertEquals(0, Utils.trueModulo(64, -16));
    assertEquals(-15, Utils.trueModulo(65, -16));
  }

  @Test
  void testTrueModuloNegativeLeftNegativeRightOpOp() {
    assertEquals(-1, Utils.trueModulo(-1, -16));
  }

  @Test
  void testTrueModuloNegativeLeftOpNegativeRightOpMultipleTimes() {
    assertEquals(-1, Utils.trueModulo(-65, -16));
  }
}

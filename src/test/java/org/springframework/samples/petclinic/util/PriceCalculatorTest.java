package org.springframework.samples.petclinic.util;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;

import static org.junit.Assert.*;



public class PriceCalculatorTest {
  
  
  @Test
  public void testGetBasePrice() {
    PriceCalculator uut = new PriceCalculator();
    
    DateTime birthDate = new DateTime(2005, 3, 31, 0, 0, 0, 0);
    Pet pet = new Pet();
    pet.setBirthDate(birthDate);
    assertEquals("One year old", new BigDecimal("400.00"), uut.calculate(birthDate.plusYears(1), pet));
    assertEquals("Two years old", new BigDecimal("400.00"), uut.calculate(birthDate.plusYears(2), pet));
    assertEquals("Three years old", new BigDecimal("400.00"), uut.calculate(birthDate.plusYears(3), pet));
    assertEquals("Four years old", new BigDecimal("480.00"), uut.calculate(birthDate.plusYears(4), pet));
    assertEquals("Five years old", new BigDecimal("480.00"), uut.calculate(birthDate.plusYears(5), pet));
    
  }
  

}

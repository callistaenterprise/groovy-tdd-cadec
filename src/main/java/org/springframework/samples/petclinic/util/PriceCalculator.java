package org.springframework.samples.petclinic.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.samples.petclinic.model.Pet;

/*
 * This class is used to calculate the cost for a visit at the PetClinic
 */
public class PriceCalculator {
  
  
  public  BigDecimal calculate(DateTime date, Pet pet) {
        
    
    BigDecimal price = new BigDecimal("400.00");
    Years age = Years.yearsBetween(pet.getBirthDate(), date);
    // Pets older than 3 years pay an additional 20%
    if (age.isGreaterThan(Years.THREE)) {
        price = price.multiply(new BigDecimal("1.20"));
    }
    // Starting with 3rd visit, you get a 20% rebate
    if (pet.getVisits().size() > 2) {
        price = price.multiply(new BigDecimal("0.80"));
    }

    return price.setScale(2, RoundingMode.HALF_UP);   
    
  }

  
  
  
}

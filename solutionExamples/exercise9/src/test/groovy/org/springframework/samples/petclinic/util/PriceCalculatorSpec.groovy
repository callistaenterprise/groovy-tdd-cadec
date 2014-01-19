package org.springframework.samples.petclinic.util

import org.joda.time.DateTime
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit

import spock.lang.Shared
import spock.lang.Specification

class PriceCalculatorSpec extends Specification {

	@Shared def calculator = new PriceCalculator()
	@Shared def birthDate = new DateTime(2005, 3, 31, 0, 0, 0, 0)
	def pet = new Pet(birthDate: birthDate)
	def visits = []
	
	def setup() {
		10.times {
			visits << new Visit(pet:pet, date: birthDate.plusWeeks(it * 5))
		}
		pet.visits = visits[0..1]
	}

	def "base price for up to 3 year old pet is 400.00"() {
		expect:
		calculator.calculate(birthDate.plusYears(3), pet) == 400.00
	}

	def "third visit gives 20% rebate"() {
		when:
		pet.visits = visits[0..2]
		then:
		calculator.calculate(birthDate.plusYears(3), pet) == 320.00
	}

	def "pets older than 3 year pay an 20% extra"() {
		expect:
		calculator.calculate(birthDate.plusYears(4), pet) == 480.00
	}
	
	def "third visit still gives 20% rebate for older pets"() {
		when:
		pet.visits = visits[0..2]
		then:
		calculator.calculate(birthDate.plusYears(4), pet) == 384.00
	}

	def "more than 3 visits gives no further rebate"(){
		when:
		pet.visits = visits
		then:
		calculator.calculate(birthDate.plusYears(4), pet) == 384.00
	}

}

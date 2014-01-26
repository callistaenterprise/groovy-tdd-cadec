package org.springframework.samples.petclinic.slim

import javax.persistence.Column;
import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotEmpty;

class InsertOwners extends SqlFixture {
	
	int id
	String firstName
	String lastName
	String address
	String city
	String telephone
	String email
	
	void reset() {
		firstName = null
		lastName = null
		address = null
		city = null
		telephone = null
		email = null
	}
	
	void execute() {
		def columns = new StringBuffer('id')
		def params = new StringBuffer('?')
		def values = [id]
		// Build an insert statement containing only those properties that was specified
		[firstName:'first_name', lastName:'last_name', address:'address',
		 city:'city', telephone:'telephone', email:'email'].each {property, column ->
			if (this."${property}") {
				columns << ", ${column}"
				params << ', ?'
				values << this."${property}"
			}
		}
		sql.execute("INSERT INTO owners (${columns}) values (${params})", values) 
	}
}

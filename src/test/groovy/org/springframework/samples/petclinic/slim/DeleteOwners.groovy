package org.springframework.samples.petclinic.slim

import javax.persistence.Column;
import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotEmpty;

class DeleteOwners extends SqlFixture {
	
	int id
	
	void execute() {
		sql.execute("""DELETE FROM owners WHERE id = ?""", id) 
	}
}

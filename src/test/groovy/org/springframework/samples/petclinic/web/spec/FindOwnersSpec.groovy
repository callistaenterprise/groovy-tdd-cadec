package org.springframework.samples.petclinic.web.spec

import geb.spock.GebSpec
import groovy.sql.Sql

import org.springframework.samples.petclinic.web.pages.*

import spock.lang.Shared

class FindOwnersSpec extends GebSpec {

	@Shared
	Sql sql = Sql.newInstance("jdbc:h2:tcp://localhost:9092/mem:h2DataSource", "sa", "", "org.h2.Driver")
	
	def "search by lastName"() {
		given: "some owners"
		createOwner(-1, 'Magnus', 'Larsson', 'ml@callistaenterprise.se')
		createOwner(-2, 'Peter', 'Larsson', 'peter.larsson@callistaenterprise.se')
		createOwner(-3, 'Örjan', 'Lundberg', 'orjan.lundberg@callistaenterprise.se')
		
		when:
		to FindOwnersPage
		findByLastName("Larsson")
 
		then:
		at OwnersPage
		owners.size() == 2
		owners[0].name == "Magnus Larsson"
		owners[1].name == "Peter Larsson"
		
		cleanup:
		deleteOwner(-1)
		deleteOwner(-2)
		deleteOwner(-3)
	}
	
	def createOwner(def id, def firstName, def lastName, def email) {
		sql.execute("INSERT INTO owners (id, first_name, last_name, email) values (?, ?, ?, ?)", [id, firstName, lastName, email])
	}

	def deleteOwner(def id) {
		sql.execute("DELETE FROM owners WHERE id = ?", id)
	}

}

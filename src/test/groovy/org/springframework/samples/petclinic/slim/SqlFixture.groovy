package org.springframework.samples.petclinic.slim

import groovy.sql.Sql

class SqlFixture {

	Sql sql
	
	void beginTable() {
		sql = Sql.newInstance("jdbc:h2:tcp://localhost:9092/mem:h2DataSource", "sa", "", "org.h2.Driver")
	}
	
	void endTable() {
		sql.close()
		sql = null
	}
}

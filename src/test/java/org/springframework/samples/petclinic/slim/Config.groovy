package org.springframework.samples.petclinic.slim

class Config {

	String property
	String value
	
	void execute() {
		if (value && !value.contains("undefined variable:")) System.setProperty(property, value)
	}

}

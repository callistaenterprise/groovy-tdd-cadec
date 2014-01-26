package org.springframework.samples.petclinic.web.pages

import geb.Module

class OwnersPage extends PetClinicPage {

	static url = "owners.html"

	static at = { $("h2").text() == "Owners" }
	
	static content = {
	    owners {
	        $("#owners tbody tr").collect {
	            module OwnerRow, it
	        }
	    }
	}
}

class OwnerRow extends Module {
	static content = {
		cell { i -> $("td", i) }
		name { cell(0).text() }
		address { cell(1).text() }
		city { cell(2).text() }
		telephone { cell(3).text() }
		pets { cell(4).text() }
	}
}
package org.springframework.samples.petclinic.web.pages

import geb.Page

class FindOwnersPage extends PetClinicPage {

	static url = "owners/find.html"

	static at = { $("h2").text() == "Find Owners" }

	static content = {
		lastName { $("input", name: "lastName") }
		search { $("submit") }
	}
	
	def findByLastName(name) {
		lastName.value(name)
		search.click()
	}
}

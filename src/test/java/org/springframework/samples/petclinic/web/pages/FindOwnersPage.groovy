package org.springframework.samples.petclinic.web.pages

import geb.Page

class FindOwnersPage extends PetClinicPage {

	static url = "owners/find.html"

	static at = { $("h2").text() == "Find Owners" }

}

package org.springframework.samples.petclinic.web.pages

import geb.Page

class OwnersPage extends PetClinicPage {

	static url = "owners.html"

	static at = { $("h2").text() == "Owners" }

}

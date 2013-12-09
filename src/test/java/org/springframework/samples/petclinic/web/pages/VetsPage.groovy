package org.springframework.samples.petclinic.web.pages

import geb.Page

class VetsPage extends PetClinicPage {

	static url = "vets.html"

	static at = { $("h2").text() == "Veterinarians" }

}

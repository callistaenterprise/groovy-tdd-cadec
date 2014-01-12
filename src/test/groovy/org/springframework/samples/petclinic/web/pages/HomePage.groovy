package org.springframework.samples.petclinic.web.pages

import geb.Page

class HomePage extends PetClinicPage {

	static url = "home.html"

	static at = { $("h2").text() == "Welcome" }

}

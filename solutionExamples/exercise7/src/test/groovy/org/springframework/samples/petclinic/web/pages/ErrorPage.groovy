package org.springframework.samples.petclinic.web.pages

import geb.Page

class ErrorPage extends PetClinicPage {

	static url = "oups.html"

	static at = { $("h2").text() == "Something happened..." }

}

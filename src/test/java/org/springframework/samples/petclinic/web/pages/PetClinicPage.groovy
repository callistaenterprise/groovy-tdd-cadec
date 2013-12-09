package org.springframework.samples.petclinic.web.pages

import geb.Page

class PetClinicPage extends Page {

	static content = {
		navigationBar { $("ul.nav") }
		navigationLink { href -> navigationBar.find("a", href: contains(href)) }
		homeLink(to: HomePage) { navigationLink(HomePage.url) }
		findOwnersLink(to: FindOwnersPage) { navigationLink(FindOwnersPage.url) }
		vetsLink(to: VetsPage) { navigationLink(VetsPage.url) }
		errorLink { navigationLink("oups") }
		footer { $("table.footer") }
	}

	def navigateToHomePage() {
		homeLink.click()
	}

	def navigateToFindOwnersPage() {
		findOwnersLink.click()
	}

	def navigateToVetsPage() {
		vetsLink.click()
	}
}

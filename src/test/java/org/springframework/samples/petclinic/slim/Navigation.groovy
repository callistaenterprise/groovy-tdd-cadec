package org.springframework.samples.petclinic.slim

import org.springframework.samples.petclinic.web.pages.*

class Navigation {

	boolean firstPageIsHomePage() {
		Browser.drive {
			go ""
            waitFor {
                at HomePage
            }
		}
		true
	}
	
	void navigateToFindOwnersPage() {
		Browser.drive {
			page.navigateToFindOwnersPage()
		}
	}

	boolean atFindOwnersPage() {
		Browser.drive {
            waitFor {
                at FindOwnersPage
            }
		}
		true
	}

	void navigateToVetsPage() {
		Browser.drive {
			page.navigateToVetsPage()
		}
	}

	boolean atVetsPage() {
		Browser.drive {
            waitFor {
                at VetsPage
            }
		}
		true
	}
	
	void navigateToHomePage() {
		Browser.drive {
			page.navigateToHomePage()
		}
	}

	boolean atHomePage() {
		Browser.drive {
            waitFor {
                at HomePage
            }
		}
		true
	}

}

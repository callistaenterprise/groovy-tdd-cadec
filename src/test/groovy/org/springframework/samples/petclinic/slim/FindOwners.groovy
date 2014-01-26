package org.springframework.samples.petclinic.slim

import org.springframework.samples.petclinic.web.pages.*

class FindOwners {

	void navigateToFindOwnersPage() {
		Browser.drive {
			to FindOwnersPage
            waitFor {
                at FindOwnersPage
            }
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
	
	void findByLastName(def lastName) {
		Browser.drive {
			page.findByLastName(lastName)
		}
	}

	boolean atOwnersPage() {
		Browser.drive {
            waitFor {
                at OwnersPage
            }
		}
		true
	}
	
	int numberOfOwnersFound() {
		int result
		Browser.drive {
			result = page.owners.size()
		}
		result
	}

	String nameOfOwner(int count) {
		String result
		Browser.drive {
            result = page.owners[count-1].name
		}
		result
	}

}

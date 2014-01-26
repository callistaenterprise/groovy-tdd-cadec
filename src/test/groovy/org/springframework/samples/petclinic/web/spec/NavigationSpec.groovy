package org.springframework.samples.petclinic.web.spec

import geb.spock.GebSpec
import org.springframework.samples.petclinic.web.pages.*

class NavigationSpec extends GebSpec {

	def "first page is HomePage"() {
		when:
		go ""
 
		then:
		at HomePage
	}

	def "from Home page you can navigate to Vets page"() {
		given:
		to HomePage
		
		when:
		navigateToVetsPage()
 
		then:
		at VetsPage
	}

	def "from Home page you can navigate to Find Owners page"() {
		given:
		to HomePage
		
		when:
		navigateToFindOwnersPage()
 
		then:
		at FindOwnersPage
	}

	def "from FindOwners page you can navigate back to Home page"() {
		given:
		to FindOwnersPage
		
		when:
		navigateToHomePage()
 
		then:
		at HomePage
	}
	
	def "from Home page you can navigate to Error page"() {
	}

	def "from Error page you can navigate back to Home page"() {
	}

}

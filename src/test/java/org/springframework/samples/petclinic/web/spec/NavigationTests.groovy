package org.springframework.samples.petclinic.web.spec

import geb.spock.GebSpec
import org.springframework.samples.petclinic.web.pages.*

class NavigationTests extends GebSpec {

	def "first page is HomePage"() {
		when:
		go ""
 
		then:
		at HomePage
	}

	def "navigate to Find Owners page"() {
		given:
		to HomePage
		
		when:
		navigateToFindOwnersPage()
 
		then:
		at FindOwnersPage
	}

	def "navigate to Vets page"() {
		given:
		to HomePage
		
		when:
		navigateToVetsPage()
 
		then:
		at VetsPage
	}

	def "navigate back to Home page"() {
		given:
		to FindOwnersPage
		
		when:
		navigateToHomePage()
 
		then:
		at HomePage
	}

}

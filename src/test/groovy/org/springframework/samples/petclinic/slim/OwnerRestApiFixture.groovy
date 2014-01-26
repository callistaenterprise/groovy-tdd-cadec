package org.springframework.samples.petclinic.slim

import groovyx.net.http.RESTClient;

class OwnerRestApiFixture {

	private def owner
	private RESTClient restClient
	
	String id

	void beginTable() {
		restClient = new RESTClient("http://localhost:9966/petclinic/api/")
	}

	void execute() {
		def resp = restClient.get( path: "owner/${id}.json")
		owner = resp.data
	}
	
	String firstName() { owner.firstName }
	String lastName() { owner.lastName }
	String address() { owner.address }
	String city() { owner.city }
	String telephone() { owner.telephone }
	String email() { owner.email }
}

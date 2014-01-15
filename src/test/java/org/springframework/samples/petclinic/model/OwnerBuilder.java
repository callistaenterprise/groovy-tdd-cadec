package org.springframework.samples.petclinic.model;

import java.util.Set;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Magnus Ekstrand
 */
public class OwnerBuilder {

	    private Owner model;

	    public OwnerBuilder() {
	        model = new Owner();
	    }

	    public OwnerBuilder id(Integer id) {
	        ReflectionTestUtils.setField(model, "id", id);
	        return this;
	    }

	    public OwnerBuilder address(String address) {
	        model.setAddress(address);
	        return this;
	    }

	    public OwnerBuilder city(String city) {
	        model.setCity(city);
	        return this;
	    }

	    public OwnerBuilder firstName(String firstName) {
	        model.setFirstName(firstName);
	        return this;
	    }
	    
	    public OwnerBuilder lastName(String lastName) {
	        model.setLastName(lastName);
	        return this;
	    }

	    public OwnerBuilder telephone(String telephone) {
	        model.setTelephone(telephone);
	        return this;
	    }

	    public OwnerBuilder pets(Set<Pet> pets) {
	        model.getPets().addAll(pets);
	        return this;
	    }

	    public Owner build() {
	        return model;
	    }
}
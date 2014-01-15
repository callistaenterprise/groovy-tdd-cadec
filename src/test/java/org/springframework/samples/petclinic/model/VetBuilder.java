package org.springframework.samples.petclinic.model;

import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Magnus Ekstrand
 */
public class VetBuilder {

    private Vet model;

    public VetBuilder() {
        model = new Vet();
    }

    public VetBuilder id(Integer id) {
        ReflectionTestUtils.setField(model, "id", id);
        return this;
    }
    
    public VetBuilder firstName(String firstName) {
        model.setFirstName(firstName);
        return this;
    }
   
    public VetBuilder lastName(String lastName) {
        model.setLastName(lastName);
        return this;
    }

    public VetBuilder specialties(List<Specialty> specialties) {
    	for (Specialty specialty : specialties) {
    		model.addSpecialty(specialty);
    	}
        return this;
    }
    
    public Vet build() {
        return model;
    }
}
package org.springframework.samples.petclinic.model;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Magnus Ekstrand
 */
public class PetBuilder {

    private Pet model;

    public PetBuilder() {
        model = new Pet();
    }

    public PetBuilder id(Integer id) {
        ReflectionTestUtils.setField(model, "id", id);
        return this;
    }
    
    public PetBuilder birthDate(DateTime birthDate) {
        model.setBirthDate(birthDate);
        return this;
    }

    public PetBuilder name(String name) {
        model.setName(name);
        return this;
    }

    public PetBuilder owner(Owner owner) {
        model.setOwner(owner);
        return this;
    }

    public PetBuilder petType(PetType petType) {
        model.setType(petType);
        return this;
    }

    public PetBuilder visits(List<Visit> visits) {
    	for (Visit visit : visits)
        model.addVisit(visit);
        return this;
    }
    
    public Pet build() {
        return model;
    }
}
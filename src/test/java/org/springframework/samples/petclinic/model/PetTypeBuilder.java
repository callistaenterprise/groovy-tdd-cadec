package org.springframework.samples.petclinic.model;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Magnus Ekstrand
 */
public class PetTypeBuilder {

    private PetType model;

    public PetTypeBuilder() {
        model = new PetType();
    }

    public PetTypeBuilder id(Integer id) {
        ReflectionTestUtils.setField(model, "id", id);
        return this;
    }
    
    public PetTypeBuilder name(String name) {
        model.setName(name);
        return this;
    }
   
    public PetType build() {
        return model;
    }
}
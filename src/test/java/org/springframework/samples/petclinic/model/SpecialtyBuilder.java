package org.springframework.samples.petclinic.model;

import org.springframework.test.util.ReflectionTestUtils;


/**
 * @author Magnus Ekstrand
 */
public class SpecialtyBuilder {

    private Specialty model;

    public SpecialtyBuilder() {
        model = new Specialty();
    }
    
    public SpecialtyBuilder id(Integer id) {
        ReflectionTestUtils.setField(model, "id", id);
        return this;
    }
    
    public SpecialtyBuilder name(String name) {
        model.setName(name);
        return this;
    }
    
    public Specialty build() {
        return model;
    }
}
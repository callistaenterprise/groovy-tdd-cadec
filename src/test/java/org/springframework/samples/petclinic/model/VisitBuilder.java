package org.springframework.samples.petclinic.model;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Magnus Ekstrand
 */
public class VisitBuilder {

    private Visit model;

    public VisitBuilder() {
        model = new Visit();
    }

    public VisitBuilder id(Integer id) {
        ReflectionTestUtils.setField(model, "id", id);
        return this;
    }
    
    public VisitBuilder date(DateTime dateTime) {
        model.setDate(dateTime);
        return this;
    }

    public VisitBuilder description(String description) {
        model.setDescription(description);
        return this;
    }

    public VisitBuilder pet(Pet pet) {
        model.setPet(pet);
        return this;
    }

    public VisitBuilder price(BigDecimal price) {
        model.setPrice(price);
        return this;
    }
    
    public Visit build() {
        return model;
    }
}
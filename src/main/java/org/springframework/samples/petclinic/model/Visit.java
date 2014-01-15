/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Magnus Ekstrand
 */
@Entity
@Table(name = "visits")
@XmlRootElement(name = "visit")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Visit extends BaseEntity {

    /**
     * Holds value of property date.
     */
	@JsonSerialize(using = DateTimeSerializer.class)	
    @Column(name = "visit_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private DateTime date;

    /**
     * Holds value of property description.
     */
    @NotEmpty
    @Column(name = "description")
    private String description;

    /**
     * Holds value of property pet.
     */
//    @JsonBackReference("pet-visits")
    @XmlTransient
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    /**
     * Holds value of property price.
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * Creates a new instance of Visit for the current date
     */
    public Visit() {
        this.date = new DateTime();
    }

    /**
     * Getter for property date.
     *
     * @return Value of property date.
     */
    public DateTime getDate() {
        return this.date;
    }

    /**
     * Setter for property date.
     *
     * @param date New value of property date.
     */
    public void setDate(DateTime date) {
        this.date = date;
    }

    /**
     * Getter for property description.
     *
     * @return Value of property description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for property description.
     *
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for property pet.
     *
     * @return Value of property pet.
     */
    public Pet getPet() {
        return this.pet;
    }

    /**
     * Setter for property pet.
     *
     * @param pet New value of property pet.
     */
    public void setPet(Pet pet) {
        this.pet = pet;
    }
    /**
     * Getter for property price.
     *
     * @return Value of property price.
     */
    public BigDecimal getPrice() {
        return this.price;
    }

    /**
     * Setter for property price.
     *
     * @param price New value of property price.
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}

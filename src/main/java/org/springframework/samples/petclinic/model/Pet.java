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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;

/**
 * Simple business object representing a pet.
 *
 * @author Magnus Ekstrand
 */
@Entity
@Table(name = "pets")
@XmlRootElement(name = "pet")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Pet extends NamedEntity {

	@JsonSerialize(using = DateTimeSerializer.class)
    @Column(name = "birth_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private DateTime birthDate;

    //@JsonBackReference("pet-petTypes")    
    @XmlTransient
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "type_id")
    private PetType petType;

    //@JsonBackReference("owner-pets")
    @XmlTransient
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    //@JsonManagedReference("pet-visits")
    @XmlElementWrapper(name = "visits")
    @XmlElement(name = "visit")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", targetEntity = Visit.class, fetch = FetchType.EAGER)
    private Set<Visit> visits;

    // ---- getters and setters
    
    public DateTime getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(DateTime birthDate) {
        this.birthDate = birthDate;
    }

    public PetType getType() {
        return this.petType;
    }

    public void setType(PetType type) {
        this.petType = type;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Visit> getVisits() {
        List<Visit> sortedVisits = new ArrayList<Visit>(getVisitsInternal());
        PropertyComparator.sort(sortedVisits, new MutableSortDefinition("date", false, false));
        //return Collections.unmodifiableList(sortedVisits);
        return sortedVisits;
    }

    public void setVisits(List<Visit> visits) {
    	getVisitsInternal().clear();
        for (Visit visit : visits) {
        	addVisit(visit);
        }
    }

    // ---- protected methods

    protected void addVisit(Visit visit) {
        getVisitsInternal().add(visit);
        visit.setPet(this);
    }
    
    // ---- protected methods

    protected Set<Visit> getVisitsInternal() {
        if (this.visits == null) {
            this.visits = new HashSet<Visit>();
        }
        return this.visits;
    }

    protected void setVisitsInternal(Set<Visit> visits) {
        this.visits = visits;
    }
    
}

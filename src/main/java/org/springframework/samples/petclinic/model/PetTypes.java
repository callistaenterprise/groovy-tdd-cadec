/*
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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple domain object representing a list of pet types.
 *
 * @author Magnus Ekstrand
 */
@XmlRootElement(name = "petTypes")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PetTypes {

    @XmlElement(name = "petType")
    private List<PetType> petTypes;

    public List<PetType> getPetTypes() {
        if (petTypes == null) {
        	petTypes = new ArrayList<PetType>();
        }
        return petTypes;
    }

}

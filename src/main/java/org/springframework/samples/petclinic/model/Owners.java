package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple domain object representing a list of owners. 
 * Mostly here to be used for the 'owners' {@link
 * org.springframework.web.servlet.view.xml.MarshallingView}.
 *
 * @author Magnus Ekstrand
 */
@XmlRootElement(name = "owners")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Owners {

    @XmlElement(name = "owner")
    private List<Owner> owners;

    public List<Owner> getOwners() {
        if (owners == null) {
            owners = new ArrayList<Owner>();
        }
        return owners;
    }

}

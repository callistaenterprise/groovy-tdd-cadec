package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple domain object representing a list of owners. 
 * Mostly here to be used for the 'owners' {@link
 * org.springframework.web.servlet.view.xml.MarshallingView}.
 *
 * @author Magnus Ekstrand
 */
@XmlRootElement
public class Owners {

    private List<Owner> owners;

    @XmlElement
    public List<Owner> getOwnerList() {
        if (owners == null) {
            owners = new ArrayList<Owner>();
        }
        return owners;
    }

}

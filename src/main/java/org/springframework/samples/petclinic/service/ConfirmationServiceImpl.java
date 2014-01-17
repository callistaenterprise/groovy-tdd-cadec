package org.springframework.samples.petclinic.service;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

	@Autowired
	private MessageSender messageSender;

	private XStream xstream;
	
	public ConfirmationServiceImpl() {
		xstream = new XStream();
		xstream.alias("owner", Owner.class);
		xstream.alias("pet", Pet.class);
		xstream.alias("visit", Visit.class);
		xstream.omitField(Pet.class, "visits");
		xstream.useAttributeFor(Pet.class, "petType");
		xstream.registerConverter(new DateTimeConverter());
		xstream.registerConverter(new PetTypeConverter());
		xstream.registerConverter(new OwnerConverter());
	}
	
	@Override
	public void sendConfirmationMessage(Visit visit) {
		String destination = visit.getPet().getOwner().getEmail();
		String message = serialize(visit);
		messageSender.sendMessage(destination, message);
 	}

	protected String serialize(Visit visit) {
		return xstream.toXML(visit);
	}

	public static class DateTimeConverter implements SingleValueConverter {
        public String toString(Object obj) {
            return ((DateTime) obj).toString();
        }
        public Object fromString(String name) {
            return null;
        }
        public boolean canConvert(Class type) {
            return type.equals(DateTime.class);
        }
	}
	public static class PetTypeConverter implements SingleValueConverter {
        public String toString(Object obj) {
            return ((PetType) obj).getName();
        }
        public Object fromString(String name) {
        	return null;
        }
        public boolean canConvert(Class type) {
            return type.equals(PetType.class);
        }
	}
	public static class OwnerConverter implements SingleValueConverter {
        public String toString(Object obj) {
        	Owner owner = (Owner) obj;
            return owner.getFirstName() + " " + owner.getLastName();
        }
        public Object fromString(String name) {
        	return null;
        }
        public boolean canConvert(Class type) {
            return type.equals(Owner.class);
        }
	}
}

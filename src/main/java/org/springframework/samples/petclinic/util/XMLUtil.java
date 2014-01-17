package org.springframework.samples.petclinic.util;

import java.sql.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XMLUtil {

	private static XStream xstream;
	
	static {
		xstream = new XStream();
		xstream.alias("owner", Owner.class);
		xstream.alias("pet", Pet.class);
		xstream.alias("petType", PetType.class);
		xstream.alias("visit", Visit.class);
		xstream.omitField(Pet.class, "visits");
		xstream.useAttributeFor(Pet.class, "petType");
		xstream.registerConverter(new DateTimeConverter());
		//xstream.registerConverter(new PetTypeConverter());
		//xstream.registerConverter(new OwnerConverter());
	}
	
	public static String serialize(Object object) {
		if (!validate(object)) {
			throw new RuntimeException("XMLUtil.serialize(Object) can only serialize instances of Owner, Pet, PetType and Visit.");
		}
		return xstream.toXML(object);
	}
	
	@SuppressWarnings("unchecked")   
	public static Object deserialize(String xml, Object root) {
	   return xstream.fromXML(xml, root);
	}

	static class DateTimeConverter implements SingleValueConverter {
        public String toString(Object obj) {
            DateTime data = (DateTime) obj;
            return DateUtil.getDateTimeFormatter(DateUtil.PRINT_FORMAT).print(data);
        }
        public Object fromString(String date) {
        	DateTime dt = DateUtil.getDateTimeFormatter(DateUtil.PARSE_FORMAT).parseDateTime(date);
            return dt;
        }
        public boolean canConvert(Class type) {
        	return type.equals(DateTime.class);
        }
	}

	static class PetTypeConverter implements SingleValueConverter {
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

	static class OwnerConverter implements SingleValueConverter {
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

	static private boolean validate(Object o) { 
		if (o instanceof Owner || 
			o instanceof Pet ||
			o instanceof PetType ||
			o instanceof Visit) {
			return true;
		}
		
		return false;
	}	
}

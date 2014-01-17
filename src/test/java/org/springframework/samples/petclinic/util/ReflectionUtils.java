package org.springframework.samples.petclinic.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

	public static void setStaticFinalAttribute(Class clazz, String attribute, Object newValue) throws Exception {
		Field field = clazz.getDeclaredField(attribute);
	    field.setAccessible(true);
	    // remove final modifier from field
	    Field modifiersField = Field.class.getDeclaredField("modifiers");
	    modifiersField.setAccessible(true);
	    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	    // set value
	    field.set(null, newValue);
	}

	public static void setStaticAttribute(Class clazz, String attribute, Object newValue) throws Exception {
		Field field = clazz.getDeclaredField(attribute);
	    field.setAccessible(true);
	    // set value
	    field.set(null, newValue);
	}

}

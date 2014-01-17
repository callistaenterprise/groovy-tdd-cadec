package org.springframework.samples.petclinic.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Magnus Ekstrand
 */
public class TestUtil {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    public static final MediaType APPLICATION_XML_UTF8 = new MediaType(MediaType.APPLICATION_XML.getType(), MediaType.APPLICATION_XML.getSubtype(), Charset.forName("utf8"));

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    
    public static DateTimeFormatter getDateTimeFormatter(String format) {
		return DateTimeFormat.forPattern(format);
	}

    /**
     * Gets a datetime object given a date as a string
     * 
     * @param dateTime a string on the format yyyy/MM/dd
     * @return a DateTime object
     */
    // Format yyyy/MM/dd
    public static DateTime getDateTime(String dateTime) throws ParseException {
		return getDateTimeFormatter("yyyy/MM/dd").parseDateTime(dateTime);
	}
    
}
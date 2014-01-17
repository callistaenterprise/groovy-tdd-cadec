package org.springframework.samples.petclinic.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {

	public static final String PRINT_FORMAT = "yyyy/MM/dd";
    public static final String PARSE_FORMAT = "yyyy/MM/dd";
	
	public static DateTimeFormatter getDateTimeFormatter(String format) {
		return DateTimeFormat.forPattern(format);
	}
	
}

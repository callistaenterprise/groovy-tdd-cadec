package org.springframework.samples.petclinic.util;

import java.nio.charset.Charset;

import org.springframework.http.MediaType;

public class MediaTypeUtil {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    public static final MediaType APPLICATION_XML_UTF8 = new MediaType(MediaType.APPLICATION_XML.getType(), MediaType.APPLICATION_XML.getSubtype(), Charset.forName("utf8"));

}

package org.springframework.samples.petclinic.service;

public interface MessageSender {

	/**
	 * Sends an xml-formatted message to a destination
	 * @param destination the destination
	 * @param xmlMessage an XML-formatted message
	 */
	boolean sendMessage(String destination, String xmlMessage);

}

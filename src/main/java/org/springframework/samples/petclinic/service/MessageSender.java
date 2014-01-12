package org.springframework.samples.petclinic.service;

public interface MessageSender {

	boolean sendMessage(String destination, String message);

}

package org.springframework.samples.petclinic.service;

import org.springframework.stereotype.Service;

@Service
public class MessageSenderImpl implements MessageSender {

	@Override
	public boolean sendMessage(String destination, String message) {
		// TODO: Implement
		System.out.println(message + "=>" + destination);
		return true;
	}

}

package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.Visit;

public interface ConfirmationService {

	public void sendConfirmationMessage(Visit visit);

}

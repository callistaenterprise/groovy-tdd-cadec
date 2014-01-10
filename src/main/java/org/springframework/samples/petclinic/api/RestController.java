package org.springframework.samples.petclinic.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Owners;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetTypes;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/api")
public class RestController {

	@Autowired
	private ClinicService clinicService;
	
	// ***** OWNER *****

//  public Collection<Owner> findOwnerByLastName(String lastName) throws DataAccessException;
//  public Owner findOwnerById(int id) throws DataAccessException;
//  public void saveOwner(Owner owner) throws DataAccessException;
	
	@RequestMapping(method=RequestMethod.GET, value="/owner/{id}", produces={"application/xml", "application/json"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Owner getOwnerById(@PathVariable("id") Integer id) {
		return clinicService.findOwnerById(id);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/owners/{lastName}", produces={"application/xml", "application/json"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Owners getOwnerByLastName(@PathVariable("lastName") String lastName) {
		Owners owners = new Owners();
		owners.getOwners().addAll(clinicService.findOwnerByLastName(lastName));
		return owners;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/owners", produces={"application/xml", "application/json"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Owners getOwners() {
		Owners owners = new Owners();
		owners.getOwners().addAll(clinicService.findOwners());
		return owners;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/owner")
	public @ResponseBody String createOwner(@RequestBody Owner owner) {
		String retMsg = "SUCCESS";
				
		try {
			clinicService.saveOwner(owner);
		} catch (Exception e) {
			retMsg = "FAILED: " + e.getMessage(); 
		}
			
		return retMsg;
	}

	@RequestMapping(method=RequestMethod.PUT, value="/owner/{id}")
	public @ResponseBody String updateOwner(@RequestBody Owner owner, @PathVariable("id") Integer id) {
		String retMsg = "SUCCESS";

		try {
			Owner current = clinicService.findOwnerById(id);
			if (current == null) {
				retMsg = "FAILED: Owner doesn't exist i repository. Make a POST to /api/owner to create an owner."; 
			}
			// Update owner
			clinicService.saveOwner(owner);
		} catch (Exception e) {
			retMsg = "FAILED: " + e.getMessage(); 
		}
		
		return retMsg;
	}
	
	// ***** PET *****

//  public Collection<PetType> findPetTypes() throws DataAccessException;
//  public Pet findPetById(int id) throws DataAccessException;
//  public void savePet(Pet pet) throws DataAccessException;
	
	@RequestMapping(method=RequestMethod.GET, value="/pet/{id}", produces={"application/xml", "application/json"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Pet getPet(@PathVariable("id") Integer id) {
		return clinicService.findPetById(id);
	}

	@RequestMapping(method=RequestMethod.GET, value="/pet/types", produces={"application/xml", "application/json"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody PetTypes getPetTypes() {
		PetTypes petTypes = new PetTypes();
		petTypes.getPetTypes().addAll(clinicService.findPetTypes());
		return petTypes;
	}

	// ***** VETERINARIAN *****
	
	@RequestMapping(method=RequestMethod.GET, value="/vets", produces={"application/xml", "application/json"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Vets getVets() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet objects 
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
    	vets.getVetList().addAll(clinicService.findVets());
		return vets;
	}
	
	// ***** VISIT *****
	
//  public void saveVisit(Visit visit) throws DataAccessException;
	
}

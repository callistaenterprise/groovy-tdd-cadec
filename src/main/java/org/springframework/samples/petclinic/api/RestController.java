package org.springframework.samples.petclinic.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Owners;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetTypes;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.util.XMLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/api")
public class RestController {

	@Autowired
	private ClinicService clinicService;
	
	// ***** OWNER *****

	@RequestMapping(method=RequestMethod.POST, value="/owners", consumes="application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.CREATED)	
	public @ResponseBody String createOwner(@RequestBody String json) {
		String retMsg = "Created";
	    
		try {
			ObjectMapper mapper = new ObjectMapper();
			Owner owner = mapper.readValue(json, Owner.class);	    
			clinicService.saveOwner(owner);
		} catch (Exception e) {
			retMsg = "FAILED: " + (e.getMessage() == null ? e.getCause().getMessage() : e.getMessage()); 
		}
			
		return retMsg;
	}

	@RequestMapping(method=RequestMethod.DELETE, value="/owners/{id}", produces={"application/json;charset=UTF-8"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody String deleteOwnerById(@PathVariable("id") Integer id) {
		String retMsg = "Deleted";
	    
		try {
			clinicService.deleteOwnerById(id);
		} catch (Exception e) {
			retMsg = "FAILED: " + (e.getMessage() == null ? e.getCause().getMessage() : e.getMessage()); 
		}
			
		return retMsg;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/owner/{id}", produces={"application/xml;charset=UTF-8", "application/json;charset=UTF-8"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Owner getOwnerById(@PathVariable("id") Integer id) {
		return clinicService.findOwnerById(id);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/owners/{lastName}", produces={"application/xml;charset=UTF-8", "application/json;charset=UTF-8"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Owners getOwnerByLastName(@PathVariable("lastName") String lastName) {
		Owners owners = new Owners();
		owners.getOwners().addAll(clinicService.findOwnerByLastName(lastName));
		return owners;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/owners", produces={"application/xml;charset=UTF-8", "application/json;charset=UTF-8"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Owners getOwners() {
		Owners owners = new Owners();
		owners.getOwners().addAll(clinicService.findOwners());
		return owners;
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/owners/{id}", consumes="application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody String updateOwner(@RequestBody String json, @PathVariable("id") Integer id) {
		String retMsg = "Updated";

		try {
			Owner found = clinicService.findOwnerById(id);
			if (found == null) {
				retMsg = "FAILED: Owner doesn't exist i repository. Make a POST to /api/owners to create an owner."; 
			}

			ObjectMapper mapper = new ObjectMapper();
			Owner owner = mapper.readValue(json, Owner.class);
			
			// Map values
			found.setAddress(owner.getAddress());
			found.setCity(owner.getCity());
			found.setFirstName(owner.getFirstName());
			found.setLastName(owner.getLastName());
			found.setTelephone(owner.getTelephone());
			if (owner.getPets() != null && owner.getPets().size() > 0) {
				for (Pet pet : owner.getPets()) {
					found.addPet(pet);
				}
			}

			// Update owner
			clinicService.saveOwner(found);
		} catch (Exception e) {
			retMsg = "FAILED: " + (e.getMessage() == null ? e.getCause().getMessage() : e.getMessage()); 
		}
		
		return retMsg;
	}
	
	// ***** PET *****

	@RequestMapping(method=RequestMethod.POST, value="/pets", consumes="application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.CREATED)	
	public @ResponseBody String createPet(@RequestBody String json) {
		String retMsg = "Created";
	    
		try {
			ObjectMapper mapper = new ObjectMapper();
			Pet pet = mapper.readValue(json, Pet.class);	    
			clinicService.savePet(pet);
		} catch (Exception e) {
			retMsg = "FAILED: " + (e.getMessage() == null ? e.getCause().getMessage() : e.getMessage()); 
		}
			
		return retMsg;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/pets/{id}", produces={"application/xml;charset=UTF-8", "application/json;charset=UTF-8"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Pet getPetById(@PathVariable("id") Integer id) {
		return clinicService.findPetById(id);
	}

	@RequestMapping(method=RequestMethod.GET, value="/pets/types", produces={"application/xml;charset=UTF-8", "application/json;charset=UTF-8"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody PetTypes getPetTypes() {
		PetTypes petTypes = new PetTypes();
		petTypes.getPetTypes().addAll(clinicService.findPetTypes());
		return petTypes;
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/pets/{id}", consumes="application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody String updatePet(@RequestBody String json, @PathVariable("id") Integer id) {
		String retMsg = "Updated";
		
		try {
			Pet oldPet = clinicService.findPetById(id);
			if (oldPet == null) {
				retMsg = "FAILED: Pet doesn't exist i repository. Make a POST to /api/pets to create a pet."; 
			}

			ObjectMapper mapper = new ObjectMapper();
			Pet newPet = mapper.readValue(json, Pet.class);
			
			// Map values
			oldPet.setBirthDate(newPet.getBirthDate());
			oldPet.setName(newPet.getName());
			if (newPet.getOwner() != null) {
				oldPet.setOwner(newPet.getOwner());
			}
			if (newPet.getType() != null) {
				oldPet.setType(newPet.getType());
			}
			if (newPet.getVisits() != null) {
				List<Visit> oldVisits = oldPet.getVisits(); 
				for (Visit visit : newPet.getVisits()) {
					oldVisits.add(visit);
				}
				oldPet.setVisits(oldVisits);
			}
			
			// Update owner
			clinicService.savePet(oldPet);
		} catch (Exception e) {
			retMsg = "FAILED: " + (e.getMessage() == null ? e.getCause().getMessage() : e.getMessage()); 
		}
		
		return retMsg;
	}
	

	// ***** VETERINARIAN *****
	
	@RequestMapping(method=RequestMethod.GET, value="/vets", produces={"application/xml;charset=UTF-8", "application/json;charset=UTF-8"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Vets getVets() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet objects 
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
    	vets.getVetList().addAll(clinicService.findVets());
		return vets;
	}
	
	// ***** VISIT *****
	
	@RequestMapping(method=RequestMethod.POST, value="/visits", consumes="application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.CREATED)	
	public @ResponseBody String createVisitJSON(@RequestBody String body) {
		String retMsg = "Created";
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			Visit visit = mapper.readValue(body, Visit.class);
			clinicService.saveVisit(visit);
		} catch (Exception e) {
			retMsg = "FAILED: " + (e.getMessage() == null ? e.getCause().getMessage() : e.getMessage()); 
		}
			
		return retMsg;
	}

	@RequestMapping(method=RequestMethod.POST, value="/visits", consumes="application/xml;charset=UTF-8")	
	public @ResponseBody ResponseEntity<String> createVisitXML(@RequestBody String body) {
		String retMsg = "Created";
		
		try {
			Visit visit = (Visit) XMLUtil.deserialize(body, new Visit());
			clinicService.saveVisit(visit);
		} catch (Exception e) {
			retMsg = "FAILED: " + (e.getMessage() == null ? e.getCause().getMessage() : e.getMessage()); 
		}

		return new ResponseEntity<String>(retMsg, new HttpHeaders(), HttpStatus.CREATED);		
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/visits/{id}", consumes="application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody String updateVisit(@RequestBody String body, @PathVariable("id") Integer id) {
		String retMsg = "Updated";
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			Visit newVisit = mapper.readValue(body, Visit.class);
			
			if (newVisit.getId() == null) {
				throw new RuntimeException("Cannot update visit entity in datasource. New visit object's id is null");
			}
			if (newVisit.getPet() == null) {
				throw new RuntimeException("Cannot update visit entity in datasource. New visit object's pet reference is missing");
			}
			
			Visit oldVisit = clinicService.findVisitById(newVisit.getId());
			if (oldVisit == null) {
				retMsg = "FAILED: Visit doesn't exist i repository. Make a POST to /api/visits to create a visit"; 
			}
			
			// Map values
			oldVisit.setDate(newVisit.getDate());
			oldVisit.setDescription(newVisit.getDescription());
			oldVisit.setPet(newVisit.getPet());
			oldVisit.setPrice(newVisit.getPrice());
			
			// Update visit
			clinicService.saveVisit(oldVisit);
		} catch (Exception e) {
			retMsg = "FAILED: " + (e.getMessage() == null ? e.getCause().getMessage() : e.getMessage()); 
		}
		
		return retMsg;
	}
	
}

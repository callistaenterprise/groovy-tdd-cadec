package org.springframework.samples.petclinic.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
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
	public @ResponseBody Owner getOwner(@PathVariable("id") Integer id) {
		Owner e = clinicService.findOwnerById(id);
		return e;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/owner/{lastName}", produces={"application/xml", "application/json"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody List<Owner> getOwner(@PathVariable("lastName") String lastName) {
		List<Owner> owners = new ArrayList<Owner>();
		owners.addAll(clinicService.findOwnerByLastName(lastName));
		return owners;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/owners", produces={"application/xml", "application/json"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody List<Owner> getOwners() {
		List<Owner> owners = new ArrayList<Owner>();
		owners.addAll(clinicService.findOwnerByLastName(""));
		return owners;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/owner")
	public @ResponseBody void createOwner(@RequestBody Owner owner) {
		clinicService.saveOwner(owner);
		//TODO: return something to indicate if it went ok or not 
	}

	@RequestMapping(method=RequestMethod.PUT, value="/owner/{id}")
	public @ResponseBody void updateOwner(@RequestBody Owner owner, @PathVariable("id") Integer id) {
		Owner o = clinicService.findOwnerById(id);
		if (o == null) {
			//TODO: return something to indicate if it went ok or not 
		}
		
		clinicService.saveOwner(owner);
		//TODO: return something to indicate if it went ok or not 
	}
	
	// ***** PET *****

//  public Collection<PetType> findPetTypes() throws DataAccessException;
//  public Pet findPetById(int id) throws DataAccessException;
//  public void savePet(Pet pet) throws DataAccessException;
	
	// ***** VET *****
	
//  public Collection<Vet> findVets() throws DataAccessException;
	
	@RequestMapping(method=RequestMethod.GET, value="/vets", produces={"application/xml", "application/json"})
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody List<Vet> getVets() {
		List<Vet> vets = new ArrayList<Vet>();
		vets.addAll(clinicService.findVets());
		return vets;
	}
	
	// ***** VISIT *****
	
//  public void saveVisit(Visit visit) throws DataAccessException;
	
}

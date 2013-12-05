package org.springframework.samples.petclinic.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestController {

	@Autowired
	private ClinicService clinicService;
	
	// ***** OWNER *****

//  public Collection<Owner> findOwnerByLastName(String lastName) throws DataAccessException;
//  public Owner findOwnerById(int id) throws DataAccessException;
//  public void saveOwner(Owner owner) throws DataAccessException;
	
	@RequestMapping(method=RequestMethod.GET, value="/owner/{id}", headers="Accept=application/xml, application/json")
	@ResponseBody
	public Owner get(@PathVariable("id") Integer id) {
		Owner e = clinicService.findOwnerById(id);
		return e;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/owner/{lastName}", headers="Accept=application/xml, application/json")
	@ResponseBody
	public List<Owner> get(@PathVariable("lastName") String lastName) {
		List<Owner> owners = new ArrayList<Owner>();
		owners.addAll(clinicService.findOwnerByLastName(lastName));
		return owners;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/owner")
	@ResponseBody
	public void create(@RequestBody Owner owner) {
		clinicService.saveOwner(owner);
		//TODO: return something to indicate if it went ok or not 
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/owner/{id}")
	@ResponseBody
	public void update(@RequestBody Owner owner, @PathVariable("id") Integer id) {
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

	// ***** VISIT *****
	
//  public void saveVisit(Visit visit) throws DataAccessException;
	
	
}

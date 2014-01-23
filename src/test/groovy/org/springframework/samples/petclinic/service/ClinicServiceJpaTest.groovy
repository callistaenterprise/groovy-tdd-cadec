package org.springframework.samples.petclinic.service;

import static org.junit.Assert.*
import groovy.sql.Sql

import java.sql.Connection

import javax.sql.DataSource

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Vet
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.util.EntityUtils
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.BeforeTransaction
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional

@ContextConfiguration(locations = ["classpath:spring/business-config.xml"])
@RunWith(SpringJUnit4ClassRunner.class)
class ClinicServiceJpaTest {

    @Autowired
    protected ClinicService clinicService;

    @Autowired
    protected DataSource dataSource;

	static boolean testDataSetup = false
    @BeforeTransaction
    public void setupTestData() {
		if (!testDataSetup) {
			testDataSetup = true
	    	Sql sql = new Sql(dataSource)
			this.class.getResource('/db/hsqldb/populateDb.sql').eachLine {line ->
				if (!line.empty) sql.execute(line)
			}
		}
	}

    @Test
    @Transactional
    public void findOwners() {
        def owners = this.clinicService.findOwnerByLastName("Davis")
        assert owners.size() == 2
        owners = this.clinicService.findOwnerByLastName("Daviss")
        assert owners.size() == 0
    }

    @Test
    public void findSingleOwner() {
        def owner1 = this.clinicService.findOwnerById(1)
        assert owner1.lastName.startsWith("Franklin")
        def owner9 = this.clinicService.findOwnerById(9)
        assert owner9.firstName == "David"

        assert owner9.getPets().size() == 1
    }

    @Test
    @Transactional
    public void insertOwner() {
        def owners = this.clinicService.findOwnerByLastName("Schultz")
        int found = owners.size()
        def owner = new Owner(firstName:"Sam", lastName: "Schultz", address: "4, Evans Street", city: "Wollongong", 
								telephone: "4444444444", email: "sam@foobar.com")
        this.clinicService.saveOwner(owner)
        assert owner.getId().longValue() != 0, "Owner Id should have been generated"
        owners = this.clinicService.findOwnerByLastName("Schultz")
        assert owners.size() == found+1, "Verifying number of owners after inserting a new one."
    }

    @Test
    @Transactional
    public void updateOwner() throws Exception {
        def o1 = this.clinicService.findOwnerById(1)
        def old = o1.lastName
        o1.lastName = old + "X"
        this.clinicService.saveOwner(o1)
        o1 = this.clinicService.findOwnerById(1)
		assert o1.lastName == "${old}X"
    }

	@Test
	public void findPet() {
	    def types = this.clinicService.findPetTypes()
	    def pet7 = this.clinicService.findPetById(7)
	    assert pet7.name.startsWith("Samantha")
	    assert pet7.owner != null
	    Pet pet6 = this.clinicService.findPetById(6);
	    assert pet6.name == "George"
	    assert pet7.owner instanceof Owner
	}

	@Test
	public void getPetTypes() {
	    def petTypes = this.clinicService.findPetTypes();
	
	    PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1)
	    assert petType1.name == "cat"
	    PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4)
	    assert petType4.name == "snake"
	}

	@Test
	@Transactional
	public void insertPet() {
	    Owner owner6 = this.clinicService.findOwnerById(6)
	    int found = owner6.pets.size();
	    Pet pet = new Pet(name: "bowser", birthDate: new DateTime())
	    def types = this.clinicService.findPetTypes()
	    pet.setType(EntityUtils.getById(types, PetType.class, 2))
	    owner6.addPet(pet)
	    assert owner6.getPets().size() == found + 1
	    // both storePet and storeOwner are necessary to cover all ORM tools
	    this.clinicService.savePet(pet)
	    this.clinicService.saveOwner(owner6)
	    owner6 = this.clinicService.findOwnerById(6)
	    assert owner6.getPets().size() == found + 1
	    assert pet.id != null, "Pet Id should have been generated"
	}

	@Test
	@Transactional
	public void updatePet() throws Exception {
	    Pet pet7 = this.clinicService.findPetById(7)
	    String old = pet7.name
	    pet7.name = old + "X"
	    this.clinicService.savePet(pet7)
	    pet7 = this.clinicService.findPetById(7)
	    assert pet7.name == "${old}X"
	}

	@Test
	public void findVets() {
	    def vets = this.clinicService.findVets()
	
	    Vet v1 = EntityUtils.getById(vets, Vet.class, 2)
	    assert v1.lastName == "Leary"
		assert v1.nrOfSpecialties == 1
		assert v1.specialties[0].name ==  "radiology"
	    Vet v2 = EntityUtils.getById(vets, Vet.class, 3);
	    assert v2.lastName == "Douglas"
		assert v2.nrOfSpecialties == 2
		assert v2.specialties[0].name ==  "dentistry"
		assert v2.specialties[1].name ==  "surgery"
	}

	@Test
	@Transactional
	public void insertVisit() {
	    Pet pet7 = this.clinicService.findPetById(7)
	    int found = pet7.getVisits().size()
	    Visit visit = new Visit(description: "test")
	    pet7.addVisit(visit)
	    // both storeVisit and storePet are necessary to cover all ORM tools
	    this.clinicService.saveVisit(visit)
	    this.clinicService.savePet(pet7)
	    pet7 = this.clinicService.findPetById(7)
	    assert pet7.getVisits().size() == found + 1
	    assert visit.id != null, "Visit Id should have been generated"
	}

}

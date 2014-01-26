package org.springframework.samples.petclinic.api

import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertThat
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.verifyNoMoreInteractions
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import groovy.json.JsonSlurper

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.model.BaseEntity
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.service.ClinicService
import org.springframework.samples.petclinic.util.DateUtil
import org.springframework.samples.petclinic.util.JSONUtil
import org.springframework.samples.petclinic.util.MediaTypeUtil
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@WebAppConfiguration
@ContextConfiguration(locations = ["classpath:testContext.xml", "classpath:spring/mvc-core-config.xml"])
@RunWith(SpringJUnit4ClassRunner)
class RestControllerPetTest {

    @Autowired
    private ClinicService clinicServiceMock

    @Autowired
    private WebApplicationContext webApplicationContext

    MockMvc mockMvc
    def birthDate = new DateTime(2009, 5, 19, 0, 0)
	def pet
	def builder = new ObjectGraphBuilder()
	
	def RestControllerPetTest() {
		builder.identifierResolver = "ref_id"
		builder.classNameResolver = "org.springframework.samples.petclinic.model"
		BaseEntity.metaClass.toJson = { JSONUtil.convertObjectToJsonBytes(delegate) }
	}
	
    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(clinicServiceMock)

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()        
		pet = builder.pet(birthDate: birthDate, name: "Fido",
						  {petType(id: 1, name: "Hund")},
						  {owner(id: 1, address: "Kungsgatan 1", city: "Göteborg", firstName: "Hannes",
								 lastName: "Johansson", telephone: "031-111213", email: "foo@bar.com")})
    }        

	@Test
	public void create_NewPet_ShouldCreatePetAndReturnString() throws Exception {

        mockMvc.perform(post("/api/pets.json")
				.contentType(MediaTypeUtil.APPLICATION_JSON_UTF8)
				.content(pet.toJson())
			)
	        .andExpect(status().isCreated())
	        .andExpect(content().contentType("application/json;charset=UTF-8"))
	        .andExpect(content().string("Created"))
        
		ArgumentCaptor<Pet> argument = ArgumentCaptor.forClass(Pet)
		verify(clinicServiceMock, times(1)).savePet(argument.capture())
		
		Pet value = argument.getValue()
		assert value.id == null
		assert value.birthDate == pet.birthDate
		assert value.name == pet.name
		assert value.new
	}
	
	@Test
	public void getPetById_PetFound_ShouldReturnSinglePetAsJson() throws Exception {
		
		when(clinicServiceMock.findPetById(1)).thenReturn(pet)

	    String jsonString = mockMvc.perform(get("/api/pets/{id}.json", 1))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(MediaTypeUtil.APPLICATION_JSON_UTF8))
			.andReturn().getResponse().getContentAsString()
		def json = new JsonSlurper().parseText(jsonString)
		assert json.id == pet.id
		assert json.birthDate == pet.birthDate.millis
		assert json.name == pet.name
	}

	@Test
	public void getPetTypes_PetTypesFound_ShouldReturnAllPetTypesAsJson() throws Exception {
		PetType first = new PetType(id: 1, name: "Hund")
		PetType second = new PetType(id: 2, name: "Katt")
		PetType third = new PetType(id: 3, name: "Ödla")
		def petTypes = [first, second, third]
		when(clinicServiceMock.findPetTypes()).thenReturn(petTypes)
		
		String jsonString = mockMvc.perform(get("/api/pets/types.json"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaTypeUtil.APPLICATION_JSON_UTF8))
			.andReturn().getResponse().getContentAsString()
		def json = new JsonSlurper().parseText(jsonString)
		assert json.petTypes.size() == petTypes.size()
		assert json.petTypes[0].id == first.id
		assert json.petTypes[0].name == first.name
		assert json.petTypes[1].id == second.id
		assert json.petTypes[1].name == second.name
		assert json.petTypes[2].id == third.id
		assert json.petTypes[2].name == third.name
	}

	@Test
	public void update_PetFound_ShouldUpdatePetAndReturnString() throws Exception {
		pet.id = 1
		when(clinicServiceMock.findPetById(1)).thenReturn(pet)
		
		mockMvc.perform(put("/api/pets/{id}.json", 1)
				.contentType(MediaTypeUtil.APPLICATION_JSON_UTF8)
				.content(pet.toJson())
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(content().string("Updated"))
		
		ArgumentCaptor<Pet> argument = ArgumentCaptor.forClass(Pet)
		verify(clinicServiceMock, times(1)).savePet(argument.capture())
		
		Pet value = argument.getValue()
		assert value.id == 1
		assert value.name == pet.name
		assert value.new == false
	}

}

package org.springframework.samples.petclinic.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.TestUtil;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.OwnerBuilder;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetBuilder;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.PetTypeBuilder;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.VisitBuilder;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml", "classpath:spring/mvc-core-config.xml"})
@WebAppConfiguration
public class RestControllerPetTest {

    private MockMvc mockMvc;

    @Autowired
    private ClinicService clinicServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(clinicServiceMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }        

	@Test
	public void create_NewPet_ShouldCreatePetAndReturnString() throws Exception {
		DateTime birthDate = TestUtil.getDateTime("2010/03/23");

		Owner owner = new OwnerBuilder()
    	.id(1)
    	.address("Kungsgatan 1")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.telephone("031-111213")
    	.build();

        PetType petType = new PetTypeBuilder()
    	.id(1)
    	.name("Hund")
    	.build();
        
        Pet first = new PetBuilder()
    	.birthDate(birthDate)
    	.name("Fido")
    	.owner(owner)
    	.petType(petType)
    	.build();        

		// This conversion will not render Owner in resulting JSON-string
        // owner in Pet.class is set to JsonIgnore to avoid a cyclic dependency
        // between Owner and Pet.
        // TODO: Manually craft a JSON-string having all dependencies
        byte[] json = TestUtil.convertObjectToJsonBytes(first);
        System.err.println(new String(json));
    
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                return "Created";
            }}).when(clinicServiceMock).savePet(any(Pet.class));

        MvcResult result = mockMvc.perform(post("/api/pets.json")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(json)
				)
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Created"))
        .andReturn();

		String content = result.getResponse().getContentAsString();
		System.err.println(content);
        
		ArgumentCaptor<Pet> argument = ArgumentCaptor.forClass(Pet.class);
		verify(clinicServiceMock, times(1)).savePet(argument.capture());
		verifyNoMoreInteractions(clinicServiceMock);
		
		Pet value = argument.getValue();
		assertNull(value.getId());
		assertThat(value.getBirthDate().getMillis(), is(1269298800000L));
		assertThat(value.getName(), is("Fido"));
		assertThat(value.getType().getId(), is(1));
		assertThat(value.getType().getName(), is("Hund"));
		assertThat(value.getVisits(), hasSize(0));
		assertThat(value.isNew(), is(true));
	}
	
	@Test
	public void getPetById_PetFound_ShouldReturnSinglePetAsJson() throws Exception {
		DateTime birthDate = TestUtil.getDateTime("2010/03/23");
		
        Owner owner = new OwnerBuilder()
    	.id(1)
    	.address("Kungsgatan 1")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.build();

        PetType petType = new PetTypeBuilder()
    	.id(1)
    	.name("Hund")
    	.build();
        
        Pet first = new PetBuilder()
    	.id(1)
    	.birthDate(birthDate)
    	.name("Fido")
    	.owner(owner)
    	.petType(petType)
    	.build();
    
		when(clinicServiceMock.findPetById(1)).thenReturn(first);

	    MvcResult result = mockMvc.perform(get("/api/pets/{id}.json", 1))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.id", is(1)))
		    .andExpect(jsonPath("$.birthDate", is(1269298800000L)))
		    .andExpect(jsonPath("$.name", is("Fido")))
		    .andExpect(jsonPath("$.type.id", is(1)))
		    .andExpect(jsonPath("$.type.name", is("Hund")))
		    .andExpect(jsonPath("$.visits", hasSize(0)))
        	.andReturn();

//		String content = result.getResponse().getContentAsString();
//		System.err.println(content);
		
		verify(clinicServiceMock, times(1)).findPetById(1);
		verifyNoMoreInteractions(clinicServiceMock);		
	}

	@Test
	public void getPetTypes_PetTypesFound_ShouldReturnAllPetTypesAsJson() throws Exception {
        PetType first = new PetTypeBuilder()
    	.id(1)
    	.name("Hund")
    	.build();

        PetType second = new PetTypeBuilder()
    	.id(2)
    	.name("Katt")
    	.build();

        PetType third = new PetTypeBuilder()
    	.id(3)
    	.name("Ödla")
    	.build();

		when(clinicServiceMock.findPetTypes()).thenReturn(Arrays.asList(first, second, third));
		
		MvcResult result = mockMvc.perform(get("/api/pets/types.json"))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.petTypes", hasSize(3)))
		    .andExpect(jsonPath("$.petTypes[0].id", is(1)))
		    .andExpect(jsonPath("$.petTypes[0].name", is("Hund")))
		    .andExpect(jsonPath("$.petTypes[1].id", is(2)))
		    .andExpect(jsonPath("$.petTypes[1].name", is("Katt")))
		    .andExpect(jsonPath("$.petTypes[2].id", is(3)))
		    .andExpect(jsonPath("$.petTypes[2].name", is("Ödla")))
        	.andReturn();

//		String content = result.getResponse().getContentAsString();
//		System.err.println(content);
		
		verify(clinicServiceMock, times(1)).findPetTypes();
		verifyNoMoreInteractions(clinicServiceMock);		
	}

	@Test
	public void update_PetFound_ShouldUpdatePetAndReturnString() throws Exception {
		DateTime birthDate = TestUtil.getDateTime("2009/05/19");
		DateTime visitDate = TestUtil.getDateTime("2014/01/14");

        Owner owner = new OwnerBuilder()
    	.id(1)
    	.address("Kungsgatan 1")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.build();

        PetType petType = new PetTypeBuilder()
    	.id(1)
    	.name("Katt")
    	.build();
        
        Pet pet = new PetBuilder()
        .id(1)
    	.birthDate(birthDate)
    	.name("Spöket III")
    	.owner(owner)
    	.petType(petType)
    	.build();
        
        // create json
		byte[] json = TestUtil.convertObjectToJsonBytes(pet);
		System.err.println(new String(json));
    
		when(clinicServiceMock.findPetById(1)).thenReturn(pet);
        
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                return "Updated";
            }}).when(clinicServiceMock).savePet(pet);

        MvcResult result = mockMvc.perform(put("/api/pets/{id}.json", 1)
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(json)
				)
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Updated"))
        .andReturn();
        
		String content = result.getResponse().getContentAsString();
		System.err.println(content);
		
		ArgumentCaptor<Pet> argument = ArgumentCaptor.forClass(Pet.class);
		verify(clinicServiceMock, times(1)).findPetById(1);
		verify(clinicServiceMock, times(1)).savePet(argument.capture());
		verifyNoMoreInteractions(clinicServiceMock);
		
		Pet value = argument.getValue();
		assertThat(value.getId(), is(1));
		assertThat(value.getName(), is("Spöket III"));
		assertThat(value.getOwner().getId(), is(1));
		assertThat(value.getType().getId(), is(1));
		assertThat(value.isNew(), is(false));
	}
	
}

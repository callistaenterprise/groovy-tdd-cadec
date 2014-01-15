package org.springframework.samples.petclinic.api;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

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
public class RestControllerVisitTest {

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
	public void create_NewVisit_ShouldCreateVisitAndReturnString() throws Exception {
		DateTime birthDate = TestUtil.getDateTime("2009/05/19");
		DateTime visitDate = TestUtil.getDateTime("2014/01/14");

		Owner owner = new OwnerBuilder()
    	.id(1)
    	.address("Kungsgatan 1")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.telephone("031-111213")
    	.build();
		
        PetType petType = new PetTypeBuilder()
    	.id(2)
    	.name("Katt")
    	.build();
        
        Pet pet = new PetBuilder()
        .id(3)
    	.birthDate(birthDate)
    	.name("Spöket")
    	.owner(owner)
    	.petType(petType)
    	.build();
        
        Visit visit = new VisitBuilder()
        .date(visitDate)
        .description("bruten svans")
        .pet(pet)
        .price(new BigDecimal(750.00))
    	.build();        

        byte[] json = TestUtil.convertObjectToJsonBytes(visit);
        System.err.println(new String(json));
    
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                return "Created";
            }}).when(clinicServiceMock).saveVisit(visit);

        MvcResult result = mockMvc.perform(post("/api/visits.json")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(json)
				)
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Created"))
        .andReturn();

		String content = result.getResponse().getContentAsString();
		System.err.println(content);
        
		ArgumentCaptor<Visit> argument = ArgumentCaptor.forClass(Visit.class);
		verify(clinicServiceMock, times(1)).saveVisit(argument.capture());
		verifyNoMoreInteractions(clinicServiceMock);
		
		Visit value = argument.getValue();
		assertNull(value.getId());
		assertThat(value.getDate().getMillis(), is(1389654000000L));
		assertThat(value.getDescription(), is("bruten svans"));
		assertThat(value.getPet().getId(), is(3));
		assertThat(value.getPet().getOwner().getId(), is(1));
		assertThat(value.getPet().getType().getId(), is(2));
		assertThat(value.getPrice().doubleValue(), is(750.00));
		assertThat(value.isNew(), is(true));
	}

	@Test
	public void update_VisitFound_ShouldUpdateVisitAndReturnString() throws Exception {
		DateTime birthDate = TestUtil.getDateTime("2009/05/19");
		DateTime visitDate = TestUtil.getDateTime("2014/01/14");
        
		Owner owner = new OwnerBuilder()
    	.id(1)
    	.address("Kungsgatan 1")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.telephone("031-111213")
    	.build();
		
        PetType petType = new PetTypeBuilder()
    	.id(2)
    	.name("Katt")
    	.build();
        
        Pet pet = new PetBuilder()
        .id(3)
    	.birthDate(birthDate)
    	.name("Spöket")
    	.owner(owner)
    	.petType(petType)
    	.build();
        
        Visit visit = new VisitBuilder()
        .id(4)
        .date(visitDate)
        .description("bruten svans")
        .pet(pet)
        .price(new BigDecimal(1150.00))
    	.build();        

        // create json
		byte[] json = TestUtil.convertObjectToJsonBytes(visit);
		System.err.println(new String(json));

		when(clinicServiceMock.findVisitById(4)).thenReturn(visit);
		
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                return "Updated";
            }}).when(clinicServiceMock).saveVisit(visit);

        MvcResult result = mockMvc.perform(put("/api/visits/{id}.json", 1)
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(json)
				)
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Updated"))
        .andReturn();
        
//		String content = result.getResponse().getContentAsString();
//		System.err.println(content);
		
		ArgumentCaptor<Visit> argument = ArgumentCaptor.forClass(Visit.class);
		verify(clinicServiceMock, times(1)).findVisitById(4);
		verify(clinicServiceMock, times(1)).saveVisit(argument.capture());
		verifyNoMoreInteractions(clinicServiceMock);
		
		Visit value = argument.getValue();
		assertThat(value.getId(), is(4));
		assertThat(value.getDate().getMillis(), is(1389654000000L));
		assertThat(value.getDescription(), is("bruten svans"));
		assertThat(value.getPet().getId(), is(3));
		assertThat(value.getPrice().doubleValue(), is(1150.00));
		assertThat(value.isNew(), is(false));
	}	
}

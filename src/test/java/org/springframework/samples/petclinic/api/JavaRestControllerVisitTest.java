package org.springframework.samples.petclinic.api;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
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
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.OwnerBuilder;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetBuilder;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.PetTypeBuilder;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.VisitBuilder;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.util.DateUtil;
import org.springframework.samples.petclinic.util.JSONUtil;
import org.springframework.samples.petclinic.util.MediaTypeUtil;
import org.springframework.samples.petclinic.util.XMLUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:testContext.xml", "classpath:spring/mvc-core-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class JavaRestControllerVisitTest {

    @Autowired
    private ClinicService clinicServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private MockMvc mockMvc;
    private DateTime birthDate;
	private DateTime visitDate;
	private Visit visit;

    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(clinicServiceMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Dates
 		DateTimeFormatter formatter = DateUtil.getDateTimeFormatter(DateUtil.PARSE_FORMAT);
 		birthDate = DateUtil.getDateTime("2009/05/19", formatter);
 		visitDate = DateUtil.getDateTime("2014/01/14", formatter);

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
        
        visit = new VisitBuilder()
        .date(visitDate)
        .description("bruten svans")
        .pet(pet)
        .price(new BigDecimal(750.00))
    	.build();        
     }
    
	@Test
	public void createVisit_ContentAsJson_ShouldCreateNewVisitAndReturnString() throws Exception {

        // Request body
        byte[] json = JSONUtil.convertObjectToJsonBytes(visit);

        mockMvc.perform(post("/api/visits.json")
				.contentType(MediaTypeUtil.APPLICATION_JSON_UTF8)
				.content(json)
				)
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Created"));
       
		ArgumentCaptor<Visit> argument = ArgumentCaptor.forClass(Visit.class);
		verify(clinicServiceMock, times(1)).saveVisit(argument.capture());
		
		Visit value = argument.getValue();
		assertNull(value.getId());
		assertThat(value.getDate(), is(visit.getDate()));
		assertThat(value.getDescription(), is(visit.getDescription()));
		assertThat(value.getPet().getId(), is(visit.getPet().getId()));
		assertThat(value.getPet().getOwner().getId(), is(visit.getPet().getOwner().getId()));
		assertThat(value.getPet().getType().getId(), is(visit.getPet().getType().getId()));
		assertThat(value.getPrice(), is(visit.getPrice()));
		assertThat(value.isNew(), is(true));
	}

	@Test
	public void createVisit_ContentAsXml_ShouldCreateNewVisitAndReturnString() throws Exception {

        // Request body
        String xml = XMLUtil.serialize(visit);
        
        mockMvc.perform(post("/api/visits.xml")
				.contentType(MediaTypeUtil.APPLICATION_XML_UTF8)
				.content(xml)
				)
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().string("Created"));

		ArgumentCaptor<Visit> argument = ArgumentCaptor.forClass(Visit.class);
		verify(clinicServiceMock, times(1)).saveVisit(argument.capture());
		verifyNoMoreInteractions(clinicServiceMock);
		
		Visit value = argument.getValue();
		assertNull(value.getId());
		assertThat(value.getDate(), is(visit.getDate()));
		assertThat(value.getDescription(), is(visit.getDescription()));
		assertThat(value.getPet().getId(), is(visit.getPet().getId()));
		assertThat(value.getPet().getOwner().getId(), is(visit.getPet().getOwner().getId()));
		assertThat(value.getPet().getType().getId(), is(visit.getPet().getType().getId()));
		assertThat(value.getPrice(), is(visit.getPrice()));
		assertThat(value.isNew(), is(true));
	}

	@Test
	public void updateVisit_ContentAsJson_ShouldUpdateVisitAndReturnString() throws Exception {

		visit.setId(4);
		
        // Request body
		byte[] json = JSONUtil.convertObjectToJsonBytes(visit);

		when(clinicServiceMock.findVisitById(4)).thenReturn(visit);

		mockMvc.perform(put("/api/visits/{id}.json", 1)
				.contentType(MediaTypeUtil.APPLICATION_JSON_UTF8)
				.content(json)
				)
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Updated"));
        
		ArgumentCaptor<Visit> argument = ArgumentCaptor.forClass(Visit.class);
		verify(clinicServiceMock, times(1)).saveVisit(argument.capture());
		
		Visit value = argument.getValue();
		assertThat(value.getId(), is(visit.getId()));
		assertThat(value.getDate(), is(visit.getDate()));
		assertThat(value.getDescription(), is(visit.getDescription()));
		assertThat(value.getPet().getId(), is(visit.getPet().getId()));
		assertThat(value.getPrice(), is(visit.getPrice()));
		assertThat(value.isNew(), is(false));
	}
	
		
	
}

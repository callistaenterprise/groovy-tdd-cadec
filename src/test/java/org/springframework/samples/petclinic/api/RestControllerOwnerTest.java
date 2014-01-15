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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

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
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml", "classpath:spring/mvc-core-config.xml"})
@WebAppConfiguration
public class RestControllerOwnerTest {

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
	public void create_NewOwner_ShouldCreateOwnerAndReturnString() throws Exception {
		Owner first = new OwnerBuilder()
    	.address("Kungsgatan 1")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.build();
        
        byte[] json = TestUtil.convertObjectToJsonBytes(first);
    
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                return "Created";
            }}).when(clinicServiceMock).saveOwner(any(Owner.class));

		mockMvc.perform(post("/api/owners.json")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(json)
				)
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Created"))
        .andReturn();
		
		ArgumentCaptor<Owner> argument = ArgumentCaptor.forClass(Owner.class);
		verify(clinicServiceMock, times(1)).saveOwner(argument.capture());
		verifyNoMoreInteractions(clinicServiceMock);
		
		Owner value = argument.getValue();
		assertNull(value.getId());
		assertNull(value.getTelephone());
		assertThat(value.getAddress(), is("Kungsgatan 1"));
		assertThat(value.getCity(), is("Göteborg"));
		assertThat(value.getFirstName(), is("Hannes"));
		assertThat(value.getLastName(), is("Johansson"));
		assertThat(value.getPets(), hasSize(0));
		assertThat(value.isNew(), is(true));
	}
	
	@Test
	public void deleteById_OwnerFound_ShouldDeleteOwnerAndReturnString() throws Exception {
        
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                return "Deleted";
            }}).when(clinicServiceMock).deleteOwnerById(1);

		mockMvc.perform(delete("/api/owners/{id}.json", 1)
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				)
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Deleted"));
		
		verify(clinicServiceMock, times(1)).deleteOwnerById(1);
		verifyNoMoreInteractions(clinicServiceMock);		
	}
    
	@Test
	public void getOwnerById_OwnerFound_ShouldReturnSingleOwnerAsJson() throws Exception {
        Owner first = new OwnerBuilder()
    	.id(1)
    	.address("Kungsgatan 1")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.build();
    
		when(clinicServiceMock.findOwnerById(1)).thenReturn(first);

		mockMvc.perform(get("/api/owner/{id}.json", 1))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.id", is(1)))
		    .andExpect(jsonPath("$.address", is("Kungsgatan 1")))
		    .andExpect(jsonPath("$.city", is("Göteborg")))
		    .andExpect(jsonPath("$.firstName", is("Hannes")))
		    .andExpect(jsonPath("$.lastName", is("Johansson")));
		
		verify(clinicServiceMock, times(1)).findOwnerById(1);
		verifyNoMoreInteractions(clinicServiceMock);		
	}

	@Test
	public void getOwnerByLastName_OwnerFound_ShouldReturnSingleOwnerAsJson() throws Exception {
        Owner first = new OwnerBuilder()
    	.id(1)
    	.address("Kungsgatan 2")
    	.city("Göteborg")
    	.firstName("Johannes")
    	.lastName("Hansson")
    	.build();
    
		when(clinicServiceMock.findOwnerByLastName("Hansson")).thenReturn(Arrays.asList(first));

		mockMvc.perform(get("/api/owners/{lastName}.json", "Hansson"))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.owners[0].id", is(1)))
		    .andExpect(jsonPath("$.owners[0].address", is("Kungsgatan 2")))
		    .andExpect(jsonPath("$.owners[0].city", is("Göteborg")))
		    .andExpect(jsonPath("$.owners[0].firstName", is("Johannes")))
		    .andExpect(jsonPath("$.owners[0].lastName", is("Hansson")));
		
		verify(clinicServiceMock, times(1)).findOwnerByLastName("Hansson");
		verifyNoMoreInteractions(clinicServiceMock);		
	}

	@Test
	public void getOwnerByLastName_OwnersFound_ShouldReturnMultipleOwnersAsJson() throws Exception {
        Owner first = new OwnerBuilder()
    	.id(1)
    	.address("Kungsgatan 1")
    	.city("Göteborg")
    	.firstName("Johannes")
    	.lastName("Hansson")
    	.build();
    
        Owner second = new OwnerBuilder()
    	.id(2)
    	.address("Kungsgatan 2")
    	.city("Göteborg")
    	.firstName("Lars")
    	.lastName("Hansson")
    	.build();

        when(clinicServiceMock.findOwnerByLastName("Hansson")).thenReturn(Arrays.asList(first, second));

		mockMvc.perform(get("/api/owners/{lastName}.json", "Hansson"))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.owners", hasSize(2)))
		    .andExpect(jsonPath("$.owners[0].id", is(1)))
		    .andExpect(jsonPath("$.owners[0].address", is("Kungsgatan 1")))
		    .andExpect(jsonPath("$.owners[0].city", is("Göteborg")))
		    .andExpect(jsonPath("$.owners[0].firstName", is("Johannes")))
		    .andExpect(jsonPath("$.owners[0].lastName", is("Hansson")))
		    .andExpect(jsonPath("$.owners[1].id", is(2)))
		    .andExpect(jsonPath("$.owners[1].address", is("Kungsgatan 2")))
		    .andExpect(jsonPath("$.owners[1].city", is("Göteborg")))
		    .andExpect(jsonPath("$.owners[1].firstName", is("Lars")))
		    .andExpect(jsonPath("$.owners[1].lastName", is("Hansson")));
		
		verify(clinicServiceMock, times(1)).findOwnerByLastName("Hansson");
		verifyNoMoreInteractions(clinicServiceMock);		
	}

	@Test
	public void getOwners_OwnersFound_ShouldReturnAllOwnersAsJson() throws Exception {
        Owner first = new OwnerBuilder()
    	.id(1)
    	.address("Kungsgatan 1")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.build();
    
        Owner second = new OwnerBuilder()
    	.id(2)
    	.address("Kungsgatan 2")
    	.city("Göteborg")
    	.firstName("Johannes")
    	.lastName("Hansson")
    	.build();
    
		when(clinicServiceMock.findOwners()).thenReturn(Arrays.asList(first, second));
		
		mockMvc.perform(get("/api/owners.json"))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.owners", hasSize(2)))
		    .andExpect(jsonPath("$.owners[0].id", is(1)))
		    .andExpect(jsonPath("$.owners[0].address", is("Kungsgatan 1")))
		    .andExpect(jsonPath("$.owners[0].city", is("Göteborg")))
		    .andExpect(jsonPath("$.owners[0].firstName", is("Hannes")))
		    .andExpect(jsonPath("$.owners[0].lastName", is("Johansson")))
		    .andExpect(jsonPath("$.owners[1].id", is(2)))
		    .andExpect(jsonPath("$.owners[1].address", is("Kungsgatan 2")))
		    .andExpect(jsonPath("$.owners[1].city", is("Göteborg")))
		    .andExpect(jsonPath("$.owners[1].firstName", is("Johannes")))
		    .andExpect(jsonPath("$.owners[1].lastName", is("Hansson")));
		
		verify(clinicServiceMock, times(1)).findOwners();
		verifyNoMoreInteractions(clinicServiceMock);		
	}

	@Test
	public void update_OwnerFound_ShouldUpdateOwnerAndReturnString() throws Exception {
		Owner first = new OwnerBuilder()
		.id(1)
    	.address("Kungsgatan 1")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.build();
        
		Owner second = new OwnerBuilder()
    	.address("Kungsgatan 24")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.telephone("031-111213")
    	.build();

		byte[] json = TestUtil.convertObjectToJsonBytes(second);
    
		when(clinicServiceMock.findOwnerById(1)).thenReturn(first);
        
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                return "Updated";
            }}).when(clinicServiceMock).saveOwner(any(Owner.class));

		mockMvc.perform(put("/api/owners/{id}.json", 1)
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(json)
				)
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Updated"));
		
		ArgumentCaptor<Owner> argument = ArgumentCaptor.forClass(Owner.class);
		verify(clinicServiceMock, times(1)).findOwnerById(1);
		verify(clinicServiceMock, times(1)).saveOwner(argument.capture());
		verifyNoMoreInteractions(clinicServiceMock);
		
		Owner value = argument.getValue();
		assertThat(value.getId(), is(1));
		assertThat(value.getAddress(), is("Kungsgatan 24"));
		assertThat(value.getCity(), is("Göteborg"));
		assertThat(value.getFirstName(), is("Hannes"));
		assertThat(value.getLastName(), is("Johansson"));
		assertThat(value.getPets(), hasSize(0));
		assertThat(value.getTelephone(), is("031-111213"));
		assertThat(value.isNew(), is(false));
	}

}

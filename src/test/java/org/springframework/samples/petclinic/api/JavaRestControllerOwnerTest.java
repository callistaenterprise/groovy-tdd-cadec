package org.springframework.samples.petclinic.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.OwnerBuilder;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.util.JSONUtil;
import org.springframework.samples.petclinic.util.MediaTypeUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:testContext.xml", "classpath:spring/mvc-core-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class JavaRestControllerOwnerTest {

    private MockMvc mockMvc;

    @Autowired
    private ClinicService clinicServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Owner first, second;
    
    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(clinicServiceMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        first = new OwnerBuilder()
        .id(1)
    	.address("Kungsgatan 1")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.build();
        second = new OwnerBuilder()
        .id(2)
    	.address("Kungsgatan 2")
    	.city("Göteborg")
    	.firstName("Johannes")
    	.lastName("Johansson")
    	.build();
    }        

	@Test
	public void create_NewOwner_ShouldCreateOwnerAndReturnString() throws Exception {
		
		first.setId(null);
        byte[] json = JSONUtil.convertObjectToJsonBytes(first);
    
		mockMvc.perform(post("/api/owners.json")
				.contentType(MediaTypeUtil.APPLICATION_JSON_UTF8)
				.content(json)
				)
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Created"));
		
		ArgumentCaptor<Owner> argument = ArgumentCaptor.forClass(Owner.class);
		verify(clinicServiceMock, times(1)).saveOwner(argument.capture());
		
		Owner value = argument.getValue();
		assertNull(value.getId());
		assertNull(value.getTelephone());
		assertThat(value.getAddress(), is(first.getAddress()));
		assertThat(value.getCity(), is(first.getCity()));
		assertThat(value.getFirstName(), is(first.getFirstName()));
		assertThat(value.getLastName(), is(first.getLastName()));
		assertThat(value.getPets(), hasSize(0));
		assertThat(value.isNew(), is(true));
	}
	
	@Test
	public void deleteById_OwnerFound_ShouldDeleteOwnerAndReturnString() throws Exception {
        
		mockMvc.perform(delete("/api/owners/{id}.json", 1)
				.contentType(MediaTypeUtil.APPLICATION_JSON_UTF8)
				)
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Deleted"));
		
		verify(clinicServiceMock, times(1)).deleteOwnerById(1);
	}
    
	@Test
	public void getOwnerById_OwnerFound_ShouldReturnSingleOwnerAsJson() throws Exception {
		
		when(clinicServiceMock.findOwnerById(1)).thenReturn(first);

		mockMvc.perform(get("/api/owner/{id}.json", 1))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(MediaTypeUtil.APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.id", is(1)))
		    .andExpect(jsonPath("$.address", is(first.getAddress())))
		    .andExpect(jsonPath("$.city", is(first.getCity())))
		    .andExpect(jsonPath("$.firstName", is(first.getFirstName())))
		    .andExpect(jsonPath("$.lastName", is(first.getLastName())));
	}

	@Test
	public void getOwnerByLastName_OwnerFound_ShouldReturnSingleOwnerAsJson() throws Exception {
		
		when(clinicServiceMock.findOwnerByLastName("Hansson")).thenReturn(Arrays.asList(first));

		mockMvc.perform(get("/api/owners/{lastName}.json", "Hansson"))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(MediaTypeUtil.APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.owners[0].id", is(1)))
		    .andExpect(jsonPath("$.owners[0].address", is(first.getAddress())))
		    .andExpect(jsonPath("$.owners[0].city", is(first.getCity())))
		    .andExpect(jsonPath("$.owners[0].firstName", is(first.getFirstName())))
		    .andExpect(jsonPath("$.owners[0].lastName", is(first.getLastName())));
	}

	@Test
	public void getOwnerByLastName_OwnersFound_ShouldReturnMultipleOwnersAsJson() throws Exception {
		
        when(clinicServiceMock.findOwnerByLastName("Hansson")).thenReturn(Arrays.asList(first, second));

		mockMvc.perform(get("/api/owners/{lastName}.json", "Hansson"))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(MediaTypeUtil.APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.owners", hasSize(2)))
		    .andExpect(jsonPath("$.owners[0].id", is(1)))
		    .andExpect(jsonPath("$.owners[0].address", is(first.getAddress())))
		    .andExpect(jsonPath("$.owners[0].city", is(first.getCity())))
		    .andExpect(jsonPath("$.owners[0].firstName", is(first.getFirstName())))
		    .andExpect(jsonPath("$.owners[0].lastName", is(first.getLastName())))
		    .andExpect(jsonPath("$.owners[1].id", is(2)))
		    .andExpect(jsonPath("$.owners[1].address", is(second.getAddress())))
		    .andExpect(jsonPath("$.owners[1].city", is(second.getCity())))
		    .andExpect(jsonPath("$.owners[1].firstName", is(second.getFirstName())))
		    .andExpect(jsonPath("$.owners[1].lastName", is(second.getLastName())));
	}

	@Test
	public void getOwners_OwnersFound_ShouldReturnAllOwnersAsJson() throws Exception {
		
		when(clinicServiceMock.findOwners()).thenReturn(Arrays.asList(first, second));
		
		mockMvc.perform(get("/api/owners.json"))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(MediaTypeUtil.APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.owners", hasSize(2)))
		    .andExpect(jsonPath("$.owners[0].id", is(1)))
		    .andExpect(jsonPath("$.owners[0].address", is(first.getAddress())))
		    .andExpect(jsonPath("$.owners[0].city", is(first.getCity())))
		    .andExpect(jsonPath("$.owners[0].firstName", is(first.getFirstName())))
		    .andExpect(jsonPath("$.owners[0].lastName", is(first.getLastName())))
		    .andExpect(jsonPath("$.owners[1].id", is(2)))
		    .andExpect(jsonPath("$.owners[1].address", is(second.getAddress())))
		    .andExpect(jsonPath("$.owners[1].city", is(second.getCity())))
		    .andExpect(jsonPath("$.owners[1].firstName", is(second.getFirstName())))
		    .andExpect(jsonPath("$.owners[1].lastName", is(second.getLastName())));
	}

	@Test
	public void update_OwnerFound_ShouldUpdateOwnerAndReturnString() throws Exception {
		
		Owner updated = new OwnerBuilder()
    	.address("Kungsgatan 24")
    	.city("Göteborg")
    	.firstName("Hannes")
    	.lastName("Johansson")
    	.telephone("031-111213")
    	.build();

		byte[] json = JSONUtil.convertObjectToJsonBytes(updated);
    
		when(clinicServiceMock.findOwnerById(1)).thenReturn(first);
        
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                return "Updated";
            }}).when(clinicServiceMock).saveOwner(any(Owner.class));

		mockMvc.perform(put("/api/owners/{id}.json", 1)
				.contentType(MediaTypeUtil.APPLICATION_JSON_UTF8)
				.content(json)
				)
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Updated"));
		
		ArgumentCaptor<Owner> argument = ArgumentCaptor.forClass(Owner.class);
		verify(clinicServiceMock, times(1)).saveOwner(argument.capture());
		
		Owner value = argument.getValue();
		assertThat(value.getId(), is(1));
		assertThat(value.getAddress(), is(updated.getAddress()));
		assertThat(value.getCity(), is(updated.getCity()));
		assertThat(value.getFirstName(), is(updated.getFirstName()));
		assertThat(value.getLastName(), is(updated.getLastName()));
		assertThat(value.getPets(), hasSize(0));
		assertThat(value.getTelephone(), is(updated.getTelephone()));
		assertThat(value.isNew(), is(false));
	}

}

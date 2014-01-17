package org.springframework.samples.petclinic.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.SpecialtyBuilder;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.VetBuilder;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.util.TestUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:testContext.xml", "classpath:spring/mvc-core-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class RestControllerVeterinarianTest {

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
	public void getVets_VetsFound_ShouldReturnAllVetsAsJson() throws Exception {
        Vet first = new VetBuilder()
    	.id(1)
    	.firstName("Laura")
    	.lastName("Pipping")
    	.specialties(Arrays.asList(
    			new SpecialtyBuilder().id(1).name("fågel").build(),
    			new SpecialtyBuilder().id(1).name("orm").build()))
    	.build();

        Vet second = new VetBuilder()
    	.id(2)
    	.firstName("Annie")
    	.lastName("Jones")
    	.specialties(Arrays.asList(new SpecialtyBuilder().id(1).name("hund och katt").build()))
    	.build();

        Vet third = new VetBuilder()
    	.id(3)
    	.firstName("Elvira")
    	.lastName("Ekstrand")
    	.specialties(Arrays.asList(new SpecialtyBuilder().id(1).name("häst").build()))
    	.build();

		when(clinicServiceMock.findVets()).thenReturn(Arrays.asList(first, second, third));
		
		MvcResult result = mockMvc.perform(get("/api/vets.json"))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.vetList", hasSize(3)))
		    .andExpect(jsonPath("$.vetList[0].id", is(1)))
		    .andExpect(jsonPath("$.vetList[0].firstName", is("Laura")))
		    .andExpect(jsonPath("$.vetList[0].lastName", is("Pipping")))
		    .andExpect(jsonPath("$.vetList[0].specialties", hasSize(2)))
		    .andExpect(jsonPath("$.vetList[0].specialties[0].name", is("fågel")))
		    .andExpect(jsonPath("$.vetList[0].specialties[1].name", is("orm")))
		    .andExpect(jsonPath("$.vetList[0].nrOfSpecialties", is(2)))
		    .andExpect(jsonPath("$.vetList[1].id", is(2)))
		    .andExpect(jsonPath("$.vetList[1].firstName", is("Annie")))
		    .andExpect(jsonPath("$.vetList[1].lastName", is("Jones")))
		    .andExpect(jsonPath("$.vetList[1].specialties", hasSize(1)))
		    .andExpect(jsonPath("$.vetList[1].specialties[0].name", is("hund och katt")))
		    .andExpect(jsonPath("$.vetList[1].nrOfSpecialties", is(1)))
		    .andExpect(jsonPath("$.vetList[2].id", is(3)))
		    .andExpect(jsonPath("$.vetList[2].firstName", is("Elvira")))
		    .andExpect(jsonPath("$.vetList[2].lastName", is("Ekstrand")))
		    .andExpect(jsonPath("$.vetList[2].specialties", hasSize(1)))
		    .andExpect(jsonPath("$.vetList[2].specialties[0].name", is("häst")))
		    .andExpect(jsonPath("$.vetList[2].nrOfSpecialties", is(1)))
        	.andReturn();
		
//		String content = result.getResponse().getContentAsString();
//		System.err.println(content);
		
		verify(clinicServiceMock, times(1)).findVets();
		verifyNoMoreInteractions(clinicServiceMock);		
	}

}

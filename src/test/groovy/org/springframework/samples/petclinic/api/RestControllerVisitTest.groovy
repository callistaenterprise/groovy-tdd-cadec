package org.springframework.samples.petclinic.api

import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertThat
import static org.mockito.Mockito.doAnswer
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.verifyNoMoreInteractions
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.model.BaseEntity
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.OwnerBuilder;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetBuilder;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.PetTypeBuilder;
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.model.VisitBuilder;
import org.springframework.samples.petclinic.service.ClinicService
import org.springframework.samples.petclinic.util.DateUtil;
import org.springframework.samples.petclinic.util.JSONUtil
import org.springframework.samples.petclinic.util.MediaTypeUtil
import org.springframework.samples.petclinic.util.XMLUtil;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(locations = ["classpath:testContext.xml", "classpath:spring/mvc-core-config.xml"])
@WebAppConfiguration
public class RestControllerVisitTest {

    @Autowired
    def ClinicService clinicServiceMock

    @Autowired
    def WebApplicationContext webApplicationContext

    def mockMvc
	def builder = new ObjectGraphBuilder()
	def birthDate = new DateTime(2009, 5, 19, 0, 0)
	def visitDate = new DateTime(2014, 1, 14, 0, 0)
	def visit
	
	def RestControllerVisitTest() {
		builder.identifierResolver = "ref_id"
		builder.classNameResolver = "org.springframework.samples.petclinic.model"
		BaseEntity.metaClass.toJson = { JSONUtil.convertObjectToJsonBytes(delegate) }
		BaseEntity.metaClass.toXml = { XMLUtil.serialize(delegate) }
	}
	
    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(clinicServiceMock)

		// Main entry point for server-side Spring MVC test support.
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
		
		visit = builder.visit(date: visitDate, description: "bruten svans", price: 750.00,
			                 {pet(id: 3, birthDate: birthDate, name: "Spöket",
			                     {petType(id: 2, name: "Katt")},
			                     {owner(id: 1, address: "Kungsgatan 1", city: "Göteborg", firstName: "Hannes",
					                    lastName: "Johansson", telephone: "031-111213", email: "foo@bar.com")})})
    }
    
	@Test
	public void createVisit_ContentAsJson_ShouldCreateNewVisitAndReturnString() throws Exception {

        mockMvc.perform(post("/api/visits.json")
				.contentType(MediaTypeUtil.APPLICATION_JSON_UTF8)
				.content(visit.toJson())
				)
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Created"))

		ArgumentCaptor<Visit> argument = ArgumentCaptor.forClass(Visit)
		verify(clinicServiceMock, times(1)).saveVisit(argument.capture())
		
		Visit value = argument.value
		assert value.id == null
		assert value.date == visit.date
		assert value.description == visit.description
		assert value.price == visit.price
		assert value.new
	}

	@Test
	public void createVisit_ContentAsXml_ShouldCreateNewVisitAndReturnString() throws Exception {

        mockMvc.perform(post("/api/visits.xml")
				.contentType(MediaTypeUtil.APPLICATION_XML_UTF8)
				.content(visit.toXml())
				)
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().string("Created"))

		ArgumentCaptor<Visit> argument = ArgumentCaptor.forClass(Visit)
		verify(clinicServiceMock, times(1)).saveVisit(argument.capture())
		
		Visit value = argument.value
		assert value.id == null
		assert value.date == visit.date
		assert value.description == visit.description
		assert value.price == visit.price
		assert value.new
	}

	@Test
	public void update_VisitFound_ShouldUpdateVisitAndReturnString() throws Exception {
		visit.id = 4
		when(clinicServiceMock.findVisitById(4)).thenReturn(visit)
		
        mockMvc.perform(put("/api/visits/{id}.json", 1)
				.contentType(MediaTypeUtil.APPLICATION_JSON_UTF8)
				.content(visit.toJson())
				)
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string("Updated"))
		
		ArgumentCaptor<Visit> argument = ArgumentCaptor.forClass(Visit)
		verify(clinicServiceMock, times(1)).saveVisit(argument.capture())
		
		Visit value = argument.getValue()
		assert value.id == 4
		assert value.date == visit.date
		assert value.description == visit.description
		assert value.price == visit.price
		assert value.pet.id == visit.pet.id
		assert !value.new
	}	
}

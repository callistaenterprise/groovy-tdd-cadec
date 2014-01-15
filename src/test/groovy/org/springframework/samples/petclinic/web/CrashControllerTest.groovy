package org.springframework.samples.petclinic.web

import static org.mockito.Mockito.*

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.*
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner.class)
@PrepareForTest([DateTime.class])
class CrashControllerTest {

	private CrashController controller;
	final shouldFail = new GroovyTestCase().&shouldFail
	
	@Before
	void setup() {
		controller = new CrashController();
	}
	
	@Test
	void testNoExceptionBeforeLunch() {
		def dateTime = DateTime.parse("1980-01-01 09:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
		PowerMockito.mockStatic(DateTime.class);
		PowerMockito.when(DateTime.now()).thenReturn(dateTime)
		assert controller.triggerException() == "/welcome"
	}

	@Test
	void testTriggerExceptionAfterLunch() {
		def dateTime = DateTime.parse("1980-01-01 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
		PowerMockito.mockStatic(DateTime.class);
		PowerMockito.when(DateTime.now()).thenReturn(dateTime)
		shouldFail(RuntimeException) {
			controller.triggerException()
		}
		
	}
}

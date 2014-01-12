package org.springframework.samples.petclinic.web

import static org.mockito.Mockito.*

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.*

class GroovyCrashControllerTest {

	private GroovyCrashController controller;
	final shouldFail = new GroovyTestCase().&shouldFail
	
	@Before
	void setup() {
		controller = new GroovyCrashController();
	}
	
	@Test
	void testNoExceptionBeforeLunch() {
		def dateTime = DateTime.parse("1980-01-01 09:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
		DateTime.metaClass.static.now = {dateTime}
		assert controller.triggerException() == "/welcome"
	}

	@Test
	void testTriggerExceptionAfterLunch() {
		def dateTime = DateTime.parse("1980-01-01 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
		DateTime.metaClass.static.now = {dateTime}
		shouldFail(RuntimeException) {
			controller.triggerException()
		}
		
	}
}

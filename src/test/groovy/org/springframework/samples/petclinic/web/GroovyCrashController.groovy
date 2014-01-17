package org.springframework.samples.petclinic.web;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Michael Isvy
 *         <p/>
 *         Also see how the bean of type 'SimpleMappingExceptionResolver' has been declared inside
 *         /WEB-INF/mvc-core-config.xml
 */
@Controller
class GroovyCrashController {

    @RequestMapping(value = "/oops", method = RequestMethod.GET)
    String triggerException() {
    	DateTime now = DateTime.now()
    	boolean isBeforeNoon = now.getHourOfDay() < 12
    	if (isBeforeNoon) {
    		return "/welcome";
    	} else { 
    		throw new RuntimeException("Expected: controller used to showcase what " +
                "happens when an exception is thrown");
    	}
    }


}

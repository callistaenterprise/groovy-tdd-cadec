package org.springframework.webflow.samples.booking;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class HotelsController {

	private BookingService bookingService;

	@Autowired
	public HotelsController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@RequestMapping(value = "/hotels/search", method = RequestMethod.GET)
	@ResponseBody
	public void search(SearchCriteria searchCriteria, Principal currentUser,
			Model model) {
		if (currentUser != null) {
			List<Booking> booking = bookingService.findBookings(currentUser
					.getName());
			model.addAttribute(booking);
		}
	}

	@RequestMapping(value = "/hotels", method = RequestMethod.GET)
	@ResponseBody
	public String list(SearchCriteria criteria, Model model) {
		List<Hotel> hotels = bookingService.findHotels(criteria);
		model.addAttribute(hotels);
		return "hotels/list";
	}

	@RequestMapping(value = "/hotels/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String show(@PathVariable("id") Long id, Model model) {
		model.addAttribute(bookingService.findHotelById(id));
		return "hotels/show";
	}

	@RequestMapping(value = "/bookings/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public String deleteBooking(@PathVariable("id")  Long id) {
		bookingService.cancelBooking(id);
		return "redirect:../hotels/search";
	}

}

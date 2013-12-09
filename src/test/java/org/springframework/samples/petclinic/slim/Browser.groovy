package org.springframework.samples.petclinic.slim;

public class Browser {

	private static geb.Browser browser

	static void initialize() {
		if (browser) throw new IllegalStateException("Browser already initialized")
		browser = new geb.Browser()
	}
	
    static void close() {
		if (!browser) throw new IllegalStateException("Browser not initialized")
        browser.quit()
		browser = null
    }
	
	static void drive(Closure script) {
		if (!browser) throw new IllegalStateException("Browser not initialized")
		script.delegate = browser
		script()
	}

}

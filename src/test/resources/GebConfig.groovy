import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver

//driver = { new FirefoxDriver() }
//driver = { new ChromeDriver() }
//driver = { new SafariDriver() }
//driver = { new InternetExplorerDriver() }
driver = { new HtmlUnitDriver() } // use HtmlUnit by default
baseUrl = "http://localhost:9966/petclinic/"
waiting {
    timeout = 5 // default wait is two seconds
}
environments {
    chrome {
        driver = { new ChromeDriver() }
    }
    safari {
        driver = { new SafariDriver() }
    }
    firefox {
        driver = { new FirefoxDriver() }
    }
    ie {
        driver = { new InternetExplorerDriver() }
    }
    headless {
        driver = { new HtmlUnitDriver() }
    }
}
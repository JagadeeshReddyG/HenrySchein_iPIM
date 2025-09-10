package com.pim.factories;

import com.pim.enums.ConfigProperties;
import com.pim.utils.PropertyFileRead;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.pim.constants.Constants;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

	/*
	 * private DriverFactory() {}
	 * 
	 *//**
		 * @param browser
		 * @return
		 * @throws MalformedURLException TODO Remove hardcoded value of grid url
		 */
	public static WebDriver getDriver(String browser) throws MalformedURLException {

		WebDriver driver = null;

		String runmode = "";
		if (Constants.getRunMode() != null) {
			runmode = Constants.getRunMode();
		}
		// String runmode = PropertyFileRead.getPropValue(ConfigProperties.RUNMODE);
		if (browser.equalsIgnoreCase("chrome")) {
			if (runmode.equalsIgnoreCase("remote")) {
				String SELENIUMGRIDURL = PropertyFileRead.getPropValue(ConfigProperties.SELENIUMGRIDURL);
				//String Node = "http://192.168.1.6:4444/wd/hub";

				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setBrowserName(BrowserType.CHROME);
				driver = new RemoteWebDriver(new URL(SELENIUMGRIDURL), cap);
				// cap.setVersion(version);
				// driver =new RemoteWebDriver(new
				// URL(PropertyFileRead.getPropValue(ConfigProperties.SELENIUMGRIDURL)), cap);
			}else {
				System.setProperty("webdriver.chrome.driver", "./src/test/resources/executables/chromedriver.exe");
//				WebDriverManager.chromedriver().setup();
//				WebDriverManager.chromedriver().browserVersion("98.0.4758.82").setup();
				ChromeOptions options = new ChromeOptions();
				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("profile.default_content_settings.popups", 0);
				prefs.put("download.default_directory", Constants.getTargetPath());
				options.setExperimentalOption("prefs", prefs);
				driver = new ChromeDriver(options);
			}
		} else if (browser.equalsIgnoreCase("firefox")) {

			if (runmode.equalsIgnoreCase("remote")) {
				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setBrowserName(BrowserType.FIREFOX);
				String SELENIUMGRIDURL = PropertyFileRead.getPropValue(ConfigProperties.SELENIUMGRIDURL);
				driver = new RemoteWebDriver(new URL(SELENIUMGRIDURL), cap);
		
			} else {
				WebDriverManager.firefoxdriver().setup();
//		        System.setProperty("webdriver.gecko.driver", "./src/test/resources/executables/geckodriver.exe");
				driver = new FirefoxDriver();
			}
		}
		return driver;
	}
}
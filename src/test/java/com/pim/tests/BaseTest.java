package com.pim.tests;

import com.pim.constants.Constants;
import com.pim.driver.Driver;
import com.pim.driver.DriverManager;
import com.pim.enums.ConfigProperties;
import com.pim.listeners.RetryFailedTests;
import com.pim.utils.ExcelUtils;
import com.pim.utils.PropertyFileRead;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class BaseTest {

	protected BaseTest() {

	}

	@BeforeSuite(alwaysRun = true)
	public void setupSuite(ITestContext context) {
		/*
		 * for (ITestNGMethod method : context.getAllTestMethods()) {
		 * method.setRetryAnalyzer(new RetryFailedTests()); }
		 */
		// if it is CA then write operation will perform
		ExcelUtils excelutils = new ExcelUtils();
		if (Constants.getDivision() == null) {
			excelutils.writeItemCodeForPIM();
		} else if (Constants.getDivision().equals("CA")) {
			excelutils.writeItemCode();
		}
	

	}

	@BeforeTest
	public void testSetUp() {

	}

	/*
	 * @BeforeMethod(alwaysRun = true) public void setUp(Method m) throws Exception
	 * {
	 * 
	 * Test testClass = m.getAnnotation(Test.class); String envURL =
	 * PropertyFileRead.getPropValue(ConfigProperties.URL); for (int i = 0; i <
	 * testClass.groups().length; i++) { if
	 * (testClass.groups()[i].equalsIgnoreCase("pixUS")) { envURL =
	 * PropertyFileRead.getPropValue(ConfigProperties.pixurlus); break; } if
	 * (testClass.groups()[i].equalsIgnoreCase("pixCA")) { envURL =
	 * PropertyFileRead.getPropValue(ConfigProperties.pixurlca); break; } if
	 * (testClass.groups()[i].equalsIgnoreCase("dam")) { envURL =
	 * PropertyFileRead.getPropValue(ConfigProperties.damurl); break; }
	 * 
	 * } Driver.initDriver(PropertyFileRead.getPropValue(ConfigProperties.BROWSER),
	 * envURL);
	 * 
	 * }
	 */

	@BeforeMethod(alwaysRun = true)
	public void setUp(Method m) throws Exception {

		String browserForDragAndDrop = "";

		Test testClass = m.getAnnotation(Test.class);
		String envURL = PropertyFileRead.getPropValue(ConfigProperties.URL);
		for (int i = 0; i < testClass.groups().length; i++) {

			if (testClass.groups()[i].equalsIgnoreCase("firefox")) {
				browserForDragAndDrop = "firefox";

			}

			if (testClass.groups()[i].equalsIgnoreCase("pixUS")) {
				envURL = PropertyFileRead.getPropValue(ConfigProperties.pixurlus);
				break;
			}
			if (testClass.groups()[i].equalsIgnoreCase("pixCA")) {
				envURL = PropertyFileRead.getPropValue(ConfigProperties.pixurlca);
				break;
			}
			if (testClass.groups()[i].equalsIgnoreCase("dam")) {
				envURL = PropertyFileRead.getPropValue(ConfigProperties.damurl);
				break;
			}

		}

		if (browserForDragAndDrop.equals("firefox")) {
			Driver.initDriver(browserForDragAndDrop, envURL);
		} else {
			Driver.initDriver(PropertyFileRead.getPropValue(ConfigProperties.BROWSER), envURL);
		}

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) {
		Driver.quitDriver();
	}

	@AfterTest
	public void testTearDown() {
	}

	@AfterSuite
	public void suiteTearDown() {

	}

//	@BeforeMethod(alwaysRun = true)
	public static void launchIPIM(){
		String iPIMUrl = PropertyFileRead.getPropValue(ConfigProperties.iPIM_URL);
		Driver.initDriver("chrome",iPIMUrl);
	}

	public static void launchIpimInNewTab() {
		String iPIMUrl = PropertyFileRead.getPropValue(ConfigProperties.iPIM_URL);
		// Open a new tab and navigate to iPIM
		((JavascriptExecutor) DriverManager.getDriver())
				.executeScript("window.open(arguments[0], '_blank');", iPIMUrl);

		// Switch WebDriver to the new tab
		ArrayList<String> tabs = new ArrayList<>(DriverManager.getDriver().getWindowHandles());
		DriverManager.getDriver().switchTo().window(tabs.get(tabs.size() - 1));
	}


}

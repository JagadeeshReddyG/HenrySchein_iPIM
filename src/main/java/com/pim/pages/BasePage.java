package com.pim.pages;

import static com.pim.reports.FrameworkLogger.log;
import com.pim.constants.Constants;
import com.pim.driver.Driver;
import com.pim.driver.DriverManager;
import com.pim.enums.ConfigProperties;
import com.pim.enums.LogType;
import com.pim.enums.WaitLogic;
import com.pim.factories.ExplicitWaitFactory;
import com.pim.utils.PropertyFileRead;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.ClickAndHoldAction;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.awt.event.KeyEvent;

public class BasePage {
	
	protected void moveToElementAndClick(By by, String elementName) {
		Actions actions = new Actions(DriverManager.getDriver());
		WebElement element = DriverManager.getDriver().findElement(by);
		ExplicitWaitFactory.performExplicitWait(WaitLogic.CLICKABLE, by);
		actions.moveToElement(element);
		actions.click().build().perform();
		log(LogType.EXTENTANDCONSOLE, "Moved to "+elementName+" and clicked");
	}
	
	protected void click(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		element.click();
		log(LogType.EXTENTANDCONSOLE, elementname + " is clicked");

	}

	public static void waitUtilLoadingIconDisappear(By by, WaitLogic waitstrategy, String elementname) {
//		WaitForMiliSec(500);
		ExplicitWaitFactory.performExplicitWait(waitstrategy, by, 120);
		log(LogType.EXTENTANDCONSOLE, "Waiting for invisibility " + elementname);
		WaitForMiliSec(5000);
	}

	protected void sendKeys(By by, String value, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		element.clear();
		WaitForMiliSec(1000);
		element.sendKeys(value);
		WaitForMiliSec(1000);
		log(LogType.EXTENTANDCONSOLE, value + " is entered successfully in " + elementname);
	}
	
	protected void sendKeys(By by, String value, WaitLogic waitstrategy) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		element.clear();
		WaitForMiliSec(1000);
		element.sendKeys(value);
	}

	protected void sendKeysByJS(By by, String value, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		JavascriptExecutor jse = (JavascriptExecutor) DriverManager.getDriver();
		jse.executeScript("arguments[0].value='" + value + "';", element);
		log(LogType.EXTENTANDCONSOLE, value + " is entered successfully in " + elementname);
	}

	protected By getElementByReplaceText(String xpath, String replaceValue) {
		String xpathUpdated = xpath.replace("${variable}", replaceValue);
		return By.xpath(xpathUpdated);
	}

	// using isdisplayed()

	protected boolean elementPresent(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		return element.isDisplayed();
	}

	protected void dropdown(By by, String value, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		Select sel = new Select(element);
		sel.selectByValue(value);
		log(LogType.EXTENTANDCONSOLE, value + " is selected" + elementname);
	}

        protected String getDropdownSelectedValue(By by, WaitLogic waitStrategy, String elementName) {

	    WebElement element = ExplicitWaitFactory.performExplicitWait(waitStrategy, by);

	    Select select = new Select(element);

	    String selectedValue = select.getFirstSelectedOption().getText().trim();

	    log(LogType.EXTENTANDCONSOLE, "Selected value for " + elementName + ": " + selectedValue);

	    return selectedValue;

	}


        protected String getTextUsingJS(By by, WaitLogic waitStrategy, String elementName) {

	    WebElement element = ExplicitWaitFactory.performExplicitWait(waitStrategy, by);

	    JavascriptExecutor executor = (JavascriptExecutor) DriverManager.getDriver();

	    String text = (String) executor.executeScript("return arguments[0].textContent.trim();", element);

	    log(LogType.EXTENTANDCONSOLE, "Text retrieved from " + elementName + ": " + text);

	    return text;

	}

	protected int getElementsCount(By by) {

	    List<WebElement> elements = DriverManager.getDriver().findElements(by);

	    int count = elements.size();

	    log(LogType.EXTENTANDCONSOLE, "Number of elements found: " + count);

	    return count;

	}
	protected List<WebElement> getElementsList(String xpath) {
		List<WebElement> elements = DriverManager.getDriver().findElements(By.xpath(xpath));
		log(LogType.EXTENTANDCONSOLE, elements.size() + " elements found for locator: " + xpath);
		return elements;
	}
	protected String getValuefromSelectClass(By by,  WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		Select sel = new Select(element);
		WebElement a = sel.getFirstSelectedOption();
		String SelectValue =a.getText();
		log(LogType.EXTENTANDCONSOLE, " is selected" + elementname);
		return SelectValue;
	}
	
	protected List<String> getAllOptionsFromSelectClass(By by,  WaitLogic waitstrategy) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		Select sel = new Select(element);
		List<String> list = new ArrayList<String>();
		List<WebElement> a = sel.getOptions();
		for (int i=0; i<a.size(); i++) {
			list.add(a.get(i).getText());
		}
		return list;
	}

	protected void deselectAllDropdowns(By by, WaitLogic waitstrategy) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		Select sel = new Select(element);
		sel.deselectAll();
		log(LogType.EXTENTANDCONSOLE, " all deselected");
	}
	
	protected void selectDropdown(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		Select sel = new Select(element);
		sel.selectByVisibleText(elementname);
		log(LogType.EXTENTANDCONSOLE, " is selected" + elementname);
	}

	public void switchToWindow() {
		// It will return the parent window name as a String
		String parent = DriverManager.getDriver().getWindowHandle();
		Set<String> set = DriverManager.getDriver().getWindowHandles();
		// Now iterate using Iterator
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String child_window = iterator.next();
			if (!parent.equals(child_window)) {
				DriverManager.getDriver().switchTo().window(child_window);
				System.out.println(DriverManager.getDriver().switchTo().window(child_window).getTitle());
				// getDriver().close();
			}
		}
		// switch to the parent window
		DriverManager.getDriver().switchTo().window(parent);
	}

	protected void downArrow() throws AWTException {
		Robot robot = new Robot();
		WaitForMiliSec(1000);
		robot.keyPress(KeyEvent.VK_DOWN);
		WaitForMiliSec(1000);
		robot.keyPress(KeyEvent.VK_ENTER);
		log(LogType.EXTENTANDCONSOLE, " down arrow");
	}

	protected String getTextValue(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		log(LogType.EXTENTANDCONSOLE, elementname + " text capture successfully");
		return element.getText().trim();
	}

	protected By getElementByReplaceTexts(String xpath, String replaceValue1, String replaceValue2) {
		String xpathVariable = xpath.replace("${variable1}", replaceValue1);
		String xpathUpdated = xpathVariable.replace("${variable2}", replaceValue2);
		return By.xpath(xpathUpdated);
	}

	public static void switchTab(int tabIndex) {

		Set<String> windows = DriverManager.getDriver().getWindowHandles();
		ArrayList<String> list = new ArrayList<>(windows);
		DriverManager.getDriver().switchTo().window(list.get(tabIndex));
	}

	protected void clearField(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		element.clear();
		log(LogType.EXTENTANDCONSOLE, elementname + " textfield is cleared successfully");
	}

	protected void selectTextViaKeyboard() {
		Actions builder = new Actions(DriverManager.getDriver());
		Action select = builder.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).build();
		select.perform();
	}


	protected void deleteSelected() {
		Actions builder = new Actions(DriverManager.getDriver());
		builder.sendKeys(Keys.DELETE).release().perform();
	}
	protected void clearWithDelete(WebElement element) {
		Actions builder = new Actions(DriverManager.getDriver());
		builder.click(element)
				.keyDown(Keys.CONTROL)
				.sendKeys("a")
				.keyUp(Keys.CONTROL)
				.sendKeys(Keys.DELETE)
				.perform();
	}

	public void clickFirstResultDropDown() {
		Actions actionDown = new Actions(DriverManager.getDriver());
		actionDown.sendKeys(Keys.DOWN).release().perform();
		Actions actionEnter = new Actions(DriverManager.getDriver());
		actionEnter.sendKeys(Keys.ENTER).release().perform();
	}

	protected void ClickENTER(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		element.sendKeys(Keys.chord(Keys.ENTER));
		log(LogType.EXTENTANDCONSOLE, elementname + " textfield is cleared successfully");
	}

	protected boolean isVisible(By by, String elementname) {
		boolean result = true;
		try {
			ExplicitWaitFactory.performExplicitWait(WaitLogic.VISIBLE, by);
			log(LogType.EXTENTANDCONSOLE, elementname + " is visible");
		} catch (Exception e) {
			log(LogType.EXTENTANDCONSOLE, elementname + " is not visible");
			result = false;
		}
		return result;
	}

	protected boolean isVisible(By by, String elementname, int timeout) {
		boolean result = true;
		try {
			WaitForMiliSec(3000);
			ExplicitWaitFactory.performExplicitWait(WaitLogic.VISIBLE, by, timeout);
			log(LogType.EXTENTANDCONSOLE, elementname + " is visible");
		} catch (Exception e) {
			log(LogType.EXTENTANDCONSOLE, elementname + " is not visible");
			result = false;
		}
		return result;
	}

	protected boolean isClickable(By by, String elementname, int timeout) {
		boolean result = true;
		try {
			WaitForMiliSec(3000);
			ExplicitWaitFactory.performExplicitWait(WaitLogic.CLICKABLE, by, timeout);
			log(LogType.EXTENTANDCONSOLE, elementname + " is clicked");
		} catch (Exception e) {
			log(LogType.EXTENTANDCONSOLE, elementname + " is not clicked");
			result = false;
		}
		return result;
	}

	// To validate element field is able to edit or not -- using clickable
	protected boolean isClickable(By by, String elementname) {
		boolean result = true;
		try {
			ExplicitWaitFactory.performExplicitWait(WaitLogic.CLICKABLE, by);
			log(LogType.EXTENTANDCONSOLE, elementname + " is clickable");
		} catch (Exception e) {
			log(LogType.EXTENTANDCONSOLE, elementname + " is not clickable");
			result = false;
		}
		return result;
	}

	// To validate element field is able to edit or not -- using isEnable
	protected boolean isEnabled(By by, String elementname) {
		boolean result = true;
		try {
			WebElement element = ExplicitWaitFactory.performExplicitWait(WaitLogic.CLICKABLE, by);
			if (element.isEnabled()) {
				log(LogType.EXTENTANDCONSOLE, elementname + " is enabled");
			} else {
				log(LogType.EXTENTANDCONSOLE, elementname + " is not enabled");
				result = false;
			}

		} catch (ElementNotInteractableException e) {
			log(LogType.EXTENTANDCONSOLE, elementname + " is not enabled");
			result = false;
		}
		return result;
	}

	public static void WaitForMiliSec(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (Exception e) {

		}

	}

	public String getPageTitle() {
		return DriverManager.getDriver().getTitle();
	}

	// using gettext() to extract the text from webelement
	protected String getStringValues(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		return element.getText().trim();
	}

	protected String getAttributeValues(By by, WaitLogic waitstrategy) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		return element.getAttribute("value");
	}
	
	protected String getAttributeValuesOfTitle(By by, WaitLogic waitstrategy) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		return element.getAttribute("title");
	}

	protected String getAttributeValues(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		return element.getAttribute("value");
	}

	// using isdisplayed()
	protected boolean iselementPresent(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		return element.isDisplayed();
	}

	public void releaseCtrlKey() {
		Actions actions = new Actions(DriverManager.getDriver());
		WaitForMiliSec(2000);
		actions.keyUp(Keys.CONTROL).perform();
	}

	public void clickAndHoldCtrlKey() {
		Actions actions = new Actions(DriverManager.getDriver());
		actions.keyDown(Keys.CONTROL).perform();
	}

	// Drag and Drop
	public void dragAndDrop(WebElement PickupElement, WebElement DropElemnet) {
		Actions actions = new Actions(DriverManager.getDriver());
		actions.clickAndHold(PickupElement);
		WaitForMiliSec(6000);
		actions.moveToElement(DropElemnet);
		WaitForMiliSec(6000);
		actions.release(DropElemnet).build().perform();
	}

	public void dragAndDropForDam(WebElement PickupElement, WebElement DropElemnet) {
		Actions actions = new Actions(DriverManager.getDriver());
		actions.dragAndDrop(PickupElement, DropElemnet).build().perform();
	}
	
	public void dragAndDropForDamManually(WebElement PickupElement, WebElement DropElemnet) {
		Actions actions = new Actions(DriverManager.getDriver());
		actions.clickAndHold(PickupElement);
		WaitForMiliSec(2000);
		actions.moveToElement(DropElemnet);
		WaitForMiliSec(2000);
		actions.release().build().perform();
	}

	public static void DragAndDropJS(WebElement source, WebElement destination) throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
		js.executeScript("function createEvent(typeOfEvent) {\n" + "var event =document.createEvent(\"CustomEvent\");\n"
				+ "event.initCustomEvent(typeOfEvent,true, true, null);\n" + "event.dataTransfer = {\n" + "data: {},\n"
				+ "setData: function (key, value) {\n" + "this.data[key] = value;\n" + "},\n"
				+ "getData: function (key) {\n" + "return this.data[key];\n" + "}\n" + "};\n" + "return event;\n"
				+ "}\n" + "\n" + "function dispatchEvent(element, event,transferData) {\n"
				+ "if (transferData !== undefined) {\n" + "event.dataTransfer = transferData;\n" + "}\n"
				+ "if (element.dispatchEvent) {\n" + "element.dispatchEvent(event);\n"
				+ "} else if (element.fireEvent) {\n" + "element.fireEvent(\"on\" + event.type, event);\n" + "}\n"
				+ "}\n" + "\n" + "function simulateHTML5DragAndDrop(element, destination) {\n"
				+ "var dragStartEvent =createEvent('dragstart');\n" + "dispatchEvent(element, dragStartEvent);\n"
				+ "var dropEvent = createEvent('drop');\n"
				+ "dispatchEvent(destination, dropEvent,dragStartEvent.dataTransfer);\n"
				+ "var dragEndEvent = createEvent('dragend');\n"
				+ "dispatchEvent(element, dragEndEvent,dropEvent.dataTransfer);\n" + "}\n" + "\n"
				+ "var source = arguments[0];\n" + "var destination = arguments[1];\n"
				+ "simulateHTML5DragAndDrop(source,destination);", source, destination);
		Thread.sleep(1500);

	}

	// Tab and Enter using keyboard
	public static void keyboardTabAndEnter() throws AWTException, InterruptedException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_TAB);
		Thread.sleep(2000);
		robot.keyPress(KeyEvent.VK_ENTER);
	}

	public void scrollToElement(By by, String elementname) {
		WaitForMiliSec(4000);
		WebElement element = DriverManager.getDriver().findElement(by);
		JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		log(LogType.INFO, "scroll upto " + elementname);
	}

	protected void JSClick(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		JavascriptExecutor executor = (JavascriptExecutor) DriverManager.getDriver();
		executor.executeScript("arguments[0].click();", element);
		log(LogType.EXTENTANDCONSOLE, elementname + " is clicked");
	}

	public static void scrollToTop() {
		WaitForMiliSec(4000);
		JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
		js.executeScript("window.scrollTo(0, 0);");
	}

	public void scrollToBottom() {
		WaitForMiliSec(4000);
		JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
		js.executeScript("window.scrollTo(0, 1000);");
	}

	public void hideElement(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = DriverManager.getDriver().findElement(by);
		JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
		js.executeScript("arguments[0].style.visibility='hidden'", element);
	}
	
	/**
	 * Name of the method: isCheckBoxEditable
	 * Description: we are verifying checkbox is editing or not.
	 * Author: Darshan
	 * Parameters: Paasing xpth and String name
	 */

	public boolean isCheckBoxEditable(By by, String elementname) {
		boolean fieldIsEditable = true;

		WebElement element = ExplicitWaitFactory.performExplicitWait(WaitLogic.VISIBLE, by);

		boolean beforeClickSelectedValue = element.isSelected();

		element.click();

		boolean afterClickSelectedValue = element.isSelected();

		if (beforeClickSelectedValue == afterClickSelectedValue) {
			fieldIsEditable = false;
		}

		element.click();

		return fieldIsEditable;

	}

	public boolean isSelected(By by, String elementname) {
		boolean result = true;
		try {
			WebElement element = ExplicitWaitFactory.performExplicitWait(WaitLogic.VISIBLE, by);
			if (element.isSelected()) {
				log(LogType.EXTENTANDCONSOLE, elementname + " is Selected");
			} else {
				log(LogType.EXTENTANDCONSOLE, elementname + " is not Selected");
				result = false;
			}

		} catch (ElementNotInteractableException e) {
			log(LogType.EXTENTANDCONSOLE, elementname + " is not enabled");
			result = false;
		}
		return result;
	}

	/**
	 * Name of the method: isAttributePresent 
	 * Description: Method will verify attribute is present or not
	 * Author:Manisha 
	 * Parameters: By, wait, attribute value and elementname
	 * 
	 */
	public boolean isAttributePresent(By by, WaitLogic waitstrategy, String attribute, String elementname) {
		Boolean result = false;
	    try {
	    	WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
	        String value = element.getAttribute(attribute);
	        if (value != null){
	            result = true;
	        }
	    } catch (Exception e) {
	    	log(LogType.EXTENTANDCONSOLE, elementname + " is not present");
	    }
	    return result;
	}
	
	/**
	 * Name of the method: naviagteToDAMApplication
	 * Description: method for launching dam url
	 * Author:Manisha
	 * Parameters: 
	 */
	public static void naviagteToDAMApplication() {
		DriverManager.getDriver().navigate().to(PropertyFileRead.getPropValue(ConfigProperties.damurl));
	}
	
	/**
	 * Name of the method: navigateToPIMApplication
	 * Description: method for launching pim url
	 * Author:Manisha
	 * Parameters: 
	 */
	public static void navigateToPIMApplication() {
		DriverManager.getDriver().navigate().to(PropertyFileRead.getPropValue(ConfigProperties.URL)); 
	}
	
	/**
	 * Name of the method: refreshingPage
	 * Description: method for refreshing the page
	 * Author:Manisha
	 * Parameters: 
	 */
	public static void refreshingPage() {
		DriverManager.getDriver().navigate().refresh();
	}
	
	/**
	 * Name of the method: rightClickOnElement
	 * Description: method to reight click on element
	 * Author:
	 * Parameters: xpath 
	 */
	public void rightClickOnElement(By by) {
		Actions action = new Actions(DriverManager.getDriver());
		action.moveToElement(DriverManager.getDriver().findElement(by)).contextClick().build().perform();
	}
	
	public void rightClickOnElementByJS(By by) {
//		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		JavascriptExecutor jse = (JavascriptExecutor) DriverManager.getDriver();
        jse.executeScript("arguments[0].dispatchEvent(new MouseEvent('contextmenu');", by);
		log(LogType.EXTENTANDCONSOLE, "Right click by JS on " + by);
	}
	
	/**
	 * Name of the method: getAttributeValues
	 * Description: method to get Attribute
	 * Author:Manisha
	 * Parameters: xpath, waitlogic,elementname ,attribute value 
	 */
	protected String getAttributeValues(By by, WaitLogic waitstrategy, String elementname, String attribute) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		return element.getAttribute(attribute);
	}
	
	/**
	 * Name of the method: waitUtilFileProcess
	 * Description: method to wait until file process in PIM/DAM
	 * Author:Manisha
	 * Parameters: waittime
	 */
	public static void waitUtilFileProcess(int timeout) {
		 WaitForMiliSec(timeout);
	}
	
	/**
	 * Name of the method: waitUtilFileProcess
	 * Description: method to wait until file process in PIM/DAM
	 * Author:Darshan
	 * Parameters: waittime
	 * @return 
	 */
	public static Boolean VerifyingImagePreview(WebElement elemnet) {
		Boolean ImagePresent = (Boolean) ((JavascriptExecutor)DriverManager.getDriver()).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", elemnet);
		return ImagePresent;
	}
	
	/**
	 * Name of the method: deSelectDropdown
	 * Description: method to de select the selected the value
	 * Author:Manisha
	 * Parameters: 
	 * @return 
	 */
	protected void deSelectDropdown(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		Select sel = new Select(element);
		sel.deselectByVisibleText(elementname);
		log(LogType.EXTENTANDCONSOLE, " is selected" + elementname);
	}
	
	/**
	 * Name of the method: zoomIn
	 * Description: method to zoom in the scroll bar 
	 * Author:Manisha
	 * Parameters: locator, wait, elementname
	 */
	protected void zoomIn(By by, WaitLogic waitstrategy, String elementname) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		element.click();
        Actions move = new Actions(DriverManager.getDriver());
        move.moveToElement(element).clickAndHold();
        move.moveByOffset(5,0);
        move.release();
        move.perform();
		log(LogType.EXTENTANDCONSOLE, " zoom in and out" + elementname);
	} 
	
	/**
	 * Name of the method: refreshWebPageMultipleTimes
	 * Description: method to refresh page multiple times 
	 * Author:Manisha
	 * Parameters: n times page will refresh
	 */
	public static void refreshWebPageMultipleTimes(int n) {
		int i=0;
		while(i<n) {
		refreshingPage();
		i++;
	  }
	}
	
	public static void navigateToGivenURL(String Url) {
		DriverManager.getDriver().navigate().to(Url); 
	}

	protected String getGivenAttribute(By by, WaitLogic waitstrategy, String elementname, String AttributeName) {
		WebElement element = ExplicitWaitFactory.performExplicitWait(waitstrategy, by);
		return element.getAttribute(AttributeName);
	}
		

}




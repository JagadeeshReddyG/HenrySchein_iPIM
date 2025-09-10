package com.pim.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.pim.constants.Constants;
import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.Javautils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailSearchPage extends BasePage {
	QualityStatusPage qualitystatus = new QualityStatusPage();
	PimHomepage pimHomepage = new PimHomepage();

	public By firstItemCode = By.xpath("(//td[@class='v-table-cell-content v-table-cell-content-rowheader']/..)[1]");

	private final By dropdownbuttonfortab = By.xpath(
			"//button[@class='v-nativebutton v-widget hpmw-tab-navigation-menubar v-nativebutton-hpmw-tab-navigation-menubar']");
	private final By tabnameSearchTextbox = By.xpath("//div[@class='v-slot v-slot-hpmw-tab-search-input']//input");
	private final String tabSearchxpath = "//span[normalize-space()='${variable}']";
	private final By filterIcon = By
			.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-search v-menubar-menuitem-unchecked']");

	private final By filterNumberSearchBoxValuePresent = By.xpath(
			"//input[@class='v-textfield v-widget v-has-width hpmw-quicksearch-input v-textfield-hpmw-quicksearch-input']");
	private final By filterButton = By
			.xpath("//div[@class='v-slot v-slot-hpmw-quick-search-button']//span[@class='v-button-wrap']");
	public By resultFirstRow = By.xpath("(//td[@class='v-table-cell-content v-table-cell-content-rowheader']/..)[1]");
	public By loadingIcon = By.xpath("//div[@class='v-loading-indicator second v-loading-indicator-delay']");
	private final By menuRefreshIcon = By
			.xpath("(//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-refresh'])[1]");
	private final By refreshIcon = By.xpath(
			"//div[@class='v-menubar v-widget hpmw-menubar v-menubar-hpmw-menubar hpmw-menuBar-right v-menubar-hpmw-menuBar-right']//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-refresh']");
	private final By filterNumberSearchBox = By.xpath(
			"//input[@class='v-textfield v-widget hpmw-quicksearch-input v-textfield-hpmw-quicksearch-input v-has-width v-textfield-prompt']");

	private final By filterSelectButton = By.xpath("//div[text()='Filter by']/ancestor::div[@class='v-expand']/descendant::div[@id='article_search_combo']");
	//private final By filterSelectButtontask = By.xpath("//div[text()='Filter by']/ancestor::div[@class='v-expand']/descendant::div[@id='task_search_combo']");
	
	
	private final By filterSelectButtontask = By.xpath("//div[@class='v-filterselect-button']");
	private final By filterSelectButtonCompareDate = By
			.xpath("//div[@class='v-filterselect v-widget']/div[@class='v-filterselect-button']");
	private final By DateField = By.xpath("//div//input[@class='v-textfield v-widget']");
	private final By DateFilterActionField = By.xpath(
			"//div[@class='v-slot v-slot-hpmw-quicksearch-input v-align-right v-align-middle']//input[@class='v-filterselect-input']");
	private final By filterTextbox = By.xpath(
			"//div[@class='v-filterselect v-widget hpmw-filterSelector v-filterselect-hpmw-filterSelector v-has-width']//input[@class='v-filterselect-input']");
	private final By filteredItem = By.xpath("//div[@class='v-table-cell-wrapper']/ancestor::tr");
	private final By classificationTab = By.xpath("//div[@class='v-captiontext'][text()='Classification']");
	private final String errorMsg = "//div[text()='${variable}']";
	private final By tabListXpath = By
			.xpath("//div[@class='v-tree-node-caption v-tree-node-caption-no-expander-icon']/div/span");
	private final String tabList = "//div[@class='v-tree-node-caption v-tree-node-caption-no-expander-icon']/div/span[contains(text(),'${variable}')]";
	private final By expandTab = By.xpath(
			"//div[@class='v-button v-widget hpmw-detail-view-focus-btn v-button-hpmw-detail-view-focus-btn hpmw-expand-all-button v-button-hpmw-expand-all-button']//span[@class='v-button-wrap']");
	private final By setting = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-config']");
	private final By fieldSelection = By.xpath("//span[text()='Field Selection']");
	private final String itemField = "((//div[contains(text(),'Ava')]/ancestor::tr/ancestor::div[@class='v-table-header-wrap']/following-sibling::div//table)[1]//td//div)[${variable}]";
	private final By jde_description_field = By.xpath("//input[@id='article_search_field']");
	private final By resultSecondrow = By
			.xpath("(//td[@class='v-table-cell-content v-table-cell-content-rowheader']/..)[2]");
	private final By filterDropDown = By.xpath("//td[@class='gwt-MenuItem gwt-MenuItem-selected']//span");
	private final By firstRowResult = By
			.xpath("//div[contains(@class,'v-panel-content-hpmw-horizontal')]//table[@class='v-table-table']//td//div");

	private final By MaximizeButton = By.xpath(
			"//div[@class='v-button v-widget hpmw-detail-view-focus-btn v-button-hpmw-detail-view-focus-btn hpmw-expand-all-button v-button-hpmw-expand-all-button']//span[@class='v-button-wrap']");
	private final By MinimizeButton = By.xpath("//div//span[@class='v-button-wrap']");
	private final String searchResult = "//td[@class='v-table-cell-content v-table-cell-content-rowheader']/..//div[contains(text(),'${variable}')]";
	private final By hsiItemNumber = By.xpath("//table[@class='v-table-table']//tr/td[2]//div");
	private final By filterByName = By.xpath("//input[contains(@class,'v-textfield v-widget hpmw-quicksearch-input')]");
	private final String getHSIItemByJDE = "(//div[contains(text(),'${variable}')]/ancestor::td/preceding-sibling::td)[2]//div";
    private final By OK = By.xpath("//span[contains(text(),'OK')]");
	private final By hsiBtn = By.xpath("//span[text()='HSI']");
	private final By productNoteFieldValue = By.xpath("//*[@id='article_table']/div[2]/div[1]/table/tbody/tr/td[3]/div");


	public ProductDetailSearchPage clickOnFirstResult() {
		if (isVisible(loadingIcon, "Loading Element", 5)) {
			waitUtilLoadingIconDisappear(loadingIcon, WaitLogic.INVISIBLE, "Product Load page");
			// WaitForMiliSec(30000);
		}
		WaitForMiliSec(3000);
		click(resultFirstRow, WaitLogic.CLICKABLE, "resultFirstRow");
		return this;
	}

	public ProductDetailSearchPage applyFilterInSearchPage(String itemNumber) {

		try {
			sendKeys(filterNumberSearchBoxValuePresent, itemNumber, WaitLogic.PRESENCE, "itemNumber");
			click(filterButton, WaitLogic.CLICKABLE, "filterButton");
			WaitForMiliSec(10000);
		} catch (Exception e) {

			try {
				click(filterIcon, WaitLogic.CLICKABLE, "filterIcon");
				sendKeys(filterNumberSearchBox, itemNumber, WaitLogic.PRESENCE, "itemNumber");
				click(filterButton, WaitLogic.CLICKABLE, "filterButton");
				WaitForMiliSec(10000);
			} catch (Exception e2) {
				sendKeys(filterNumberSearchBox, itemNumber, WaitLogic.PRESENCE, "itemNumber");
				click(filterButton, WaitLogic.CLICKABLE, "filterButton");
				WaitForMiliSec(10000);
			}

		}

		return this;
	}

	// This will be useful for filter dropdown
	public ProductDetailSearchPage applyFilterInSearchPageByAnyValue(String subDropdownValue, String dropdownValue) {
		try {
			WaitForMiliSec(7000);
			click(filterIcon, WaitLogic.CLICKABLE, "filterIcon");
			click(filterSelectButton, WaitLogic.CLICKABLE, "Filter Select Button");
			selectTextViaKeyboard();
			deleteSelected();
			WaitForMiliSec(1000);
			DriverManager.getDriver().switchTo().activeElement().click();
			WaitForMiliSec(3000);
			DriverManager.getDriver().switchTo().activeElement().sendKeys(subDropdownValue);
			// WaitForMiliSec(3000);
			// sendKeys(filterTextbox, subDropdownValue, WaitLogic.PRESENCE, "filter using
			// some value");
			WaitForMiliSec(3000);
			click(filterDropDown, WaitLogic.CLICKABLE, "select from dropdown");
			WaitForMiliSec(2000);
			sendKeys(filterNumberSearchBox, dropdownValue, WaitLogic.PRESENCE, "dropdown value is selected");
			WaitForMiliSec(3000);
			click(filterButton, WaitLogic.CLICKABLE, "filterButton");
			WaitForMiliSec(10000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	

	// This will be useful for filter dropdown in task section for same session
	public ProductDetailSearchPage applyFilterInSearchPageByAnyValueinTask(String subDropdownValue,
			String dropdownValue) {
		try {
			WaitForMiliSec(5000);
			click(filterSelectButtontask, WaitLogic.CLICKABLE, "Filter Select Button");
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_A);
			r.keyRelease(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_BACK_SPACE);
			sendKeys(filterTextbox, subDropdownValue, WaitLogic.PRESENCE, "filter using some value");
			WaitForMiliSec(3000);
			r.keyPress(KeyEvent.VK_ENTER);
			WaitForMiliSec(2000);
			sendKeys(filterNumberSearchBox, dropdownValue, WaitLogic.PRESENCE, "dropdown value is selected");
			WaitForMiliSec(3000);
			click(filterButton, WaitLogic.CLICKABLE, "filterButton");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	

	// This will be useful for filter dropdown in task section for same session
	public ProductDetailSearchPage applyFilterToSearchAnyValueinTaskList(String subDropdownValue,
			String dropdownValue) {
		try {
			WaitForMiliSec(5000);
			click(filterIcon, WaitLogic.CLICKABLE, "filterIcon");
			click(filterSelectButtontask, WaitLogic.CLICKABLE, "Filter Select Button");
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_A);
			r.keyRelease(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_BACK_SPACE);
			sendKeys(filterTextbox, subDropdownValue, WaitLogic.PRESENCE, "filter using some value");
			WaitForMiliSec(3000);
			r.keyPress(KeyEvent.VK_ENTER);
			WaitForMiliSec(2000);
			sendKeys(filterNumberSearchBox, dropdownValue, WaitLogic.PRESENCE, "dropdown value is selected");
			WaitForMiliSec(3000);
			click(filterButton, WaitLogic.CLICKABLE, "filterButton");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	

	// This will be useful for filter with date
	public ProductDetailSearchPage applyDateFilterInSearchPageByAnyValue(String subDropdownValue, String Action) {
		try {
			WaitForMiliSec(5000);
			click(filterIcon, WaitLogic.CLICKABLE, "filterIcon");
			click(filterTextbox, WaitLogic.CLICKABLE, "Filter Select Button");
			selectTextViaKeyboard();
			deleteSelected();
			WaitForMiliSec(1000);
			DriverManager.getDriver().switchTo().activeElement().click();
			WaitForMiliSec(3000);
			DriverManager.getDriver().switchTo().activeElement().sendKeys(subDropdownValue);
			WaitForMiliSec(3000);
			// sendKeys(filterTextbox, subDropdownValue, WaitLogic.PRESENCE, "filter using
			// some value");
			click(getElementByReplaceText(tabSearchxpath, subDropdownValue), WaitLogic.CLICKABLE, "tabname");
			WaitForMiliSec(3000);
			click(DateFilterActionField, WaitLogic.CLICKABLE, "Filter Select Button");
			selectTextViaKeyboard();
			deleteSelected();
			WaitForMiliSec(1000);
			DriverManager.getDriver().switchTo().activeElement().click();
			WaitForMiliSec(3000);
			DriverManager.getDriver().switchTo().activeElement().sendKeys(Action);
			WaitForMiliSec(3000);
			click(getElementByReplaceText(tabSearchxpath, Action), WaitLogic.CLICKABLE, "tabname");
			WaitForMiliSec(3000);
			sendKeys(DateField, DateandTimeUtils.getSpecifiedDayFromCurrentDay(-8), WaitLogic.PRESENCE,
					"filter using some value");
			WaitForMiliSec(2000);
			click(filterButton, WaitLogic.CLICKABLE, "filterButton");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public ProductDetailSearchPage selectTabfromDropdown(String tabname) {
		WaitForMiliSec(1000);
		click(dropdownbuttonfortab, WaitLogic.CLICKABLE, "dropdownbuttonfortab");
		sendKeys(tabnameSearchTextbox, tabname, WaitLogic.VISIBLE, "tabnameSearchTextbox");
		WaitForMiliSec(2000);
		click(getElementByReplaceText(tabSearchxpath, tabname), WaitLogic.CLICKABLE, "tabname");
		WaitForMiliSec(2000);
		//have to remove this code
		/*if(isVisible(OK,"OK")) {
		click(OK, WaitLogic.CLICKABLE, "OK");
		}*/
		return this;
	}

	public ProductDetailSearchPage clickRefreshIcon() {
		WaitForMiliSec(3000);
		click(refreshIcon, WaitLogic.CLICKABLE, "ClickedRefreshIcon");
		return this;
	}

	public boolean isFirstResultFieldVisible(String fieldname) {
		By by = null;
		try {
			by = (By) ProductDetailSearchPage.class.getField(fieldname).get(ProductDetailSearchPage.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldname);
	}

	public boolean isFirstResultFieldNotVisible(String fieldname) {
		By by = null;
		try {
			by = (By) ProductDetailSearchPage.class.getField(fieldname).get(ProductDetailSearchPage.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldname, 10);
	}

	public ProductDetailSearchPage clickMenuRefreshIcon() {
		WaitForMiliSec(5000);
		click(menuRefreshIcon, WaitLogic.CLICKABLE, "menu refresh icon");
		WaitForMiliSec(5000);
		return this;
	}

	public List<String> getTabList() {
		List<WebElement> tabListSize = DriverManager.getDriver().findElements(tabListXpath);
		List<String> listTab = new ArrayList<String>();
		for (WebElement tabRows : tabListSize) {
			String tabNumber = tabRows.getText();
			listTab.add(tabNumber);
		}
		System.out.println(listTab);
		WaitForMiliSec(4000);
		return listTab;
	}

	public ProductDetailSearchPage clickOnTabDropDown() {
		click(dropdownbuttonfortab, WaitLogic.CLICKABLE, "dropdownbuttonfortab");
		WaitForMiliSec(3000);
		return this;
	}

	public ProductDetailSearchPage expandTab() {
		click(expandTab, WaitLogic.CLICKABLE, "menu refresh icon");
		return this;
	}

	public ProductDetailSearchPage clickSettingIcon() {
		WaitForMiliSec(5000);
		click(setting, WaitLogic.CLICKABLE, "setting icon");
		WaitForMiliSec(5000);
		return this;
	}

	public ProductDetailSearchPage clickFieldSelectionOption() {
		WaitForMiliSec(2000);
		click(fieldSelection, WaitLogic.CLICKABLE, "Field Selection");
		WaitForMiliSec(5000);
		return this;
	}

	public String getItemField(String fieldIndex) {
		WaitForMiliSec(2000);
		String fieldValue = getTextValue(getElementByReplaceText(itemField, fieldIndex), WaitLogic.VISIBLE,
				"Field Value");
		return fieldValue;
	}

	public ProductDetailSearchPage clickOnSecondResult() {
		WaitForMiliSec(5000);
		click(resultSecondrow, WaitLogic.CLICKABLE, "resultsecondRow");
		return this;
	}

	// To return all the item details viewable
	public List<String> viewAllItemDetails() {
		WaitForMiliSec(5000);
		List<WebElement> listSize = DriverManager.getDriver().findElements(firstRowResult);
		List<String> allItemDetails = new ArrayList<String>();
		for (WebElement ItemDetails : listSize) {
			String ItemDetailsDisplayed = ItemDetails.getText();
			allItemDetails.add(ItemDetailsDisplayed);
		}
		return allItemDetails;
	}

	// This will be useful for filter with JDE Descrption
	public ProductDetailSearchPage applyJDEFilter(String subDropdownValue, String description) {
		try {
			WaitForMiliSec(5000);
			click(filterIcon, WaitLogic.CLICKABLE, "filterIcon");
			click(filterTextbox, WaitLogic.CLICKABLE, "Filter Select Button");
			selectTextViaKeyboard();
			deleteSelected();
			WaitForMiliSec(1000);
			DriverManager.getDriver().switchTo().activeElement().click();
			WaitForMiliSec(3000);
			DriverManager.getDriver().switchTo().activeElement().sendKeys(subDropdownValue);
			WaitForMiliSec(3000);
			// sendKeys(filterTextbox, subDropdownValue, WaitLogic.PRESENCE, "filter using
			// some value");
			click(getElementByReplaceText(tabSearchxpath, subDropdownValue), WaitLogic.CLICKABLE, "tabname");
			WaitForMiliSec(3000);

			sendKeys(jde_description_field, description, WaitLogic.PRESENCE, "filter using some value");
			WaitForMiliSec(2000);
			click(filterButton, WaitLogic.CLICKABLE, "filterButton");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public ProductDetailSearchPage clickFilterButton() {
		click(filterButton, WaitLogic.CLICKABLE, "filterButton");
		return this;
	}

	public ProductDetailSearchPage maximizeProductDetailTab() {
		WaitForMiliSec(2000);
		click(MaximizeButton, WaitLogic.CLICKABLE, "Maximize button clicked");
		WaitForMiliSec(2000);
		return this;
	}

	public ProductDetailSearchPage minimizeProductDetailTab() {
		WaitForMiliSec(2000);
		click(MinimizeButton, WaitLogic.CLICKABLE, "Minimize button clicked");
		WaitForMiliSec(2000);
		return this;
	}

	/**
	 * Name of the method: clickOnSearchResultOnMasterCatalog Description: method to
	 * click on search result Author:Manisha Parameters:
	 */
	public ProductDetailSearchPage clickOnSearchResultOnMasterCatalog() {
		if (isVisible(loadingIcon, "Loading Element", 5)) {
			waitUtilLoadingIconDisappear(loadingIcon, WaitLogic.INVISIBLE, "Product Load page");
			// WaitForMiliSec(30000);
		}
		WaitForMiliSec(3000);
		if (Constants.getDivision().equals("US")) {
			click(resultFirstRow, WaitLogic.CLICKABLE, "resultFirstRow");
		} else {
			click(resultSecondrow, WaitLogic.CLICKABLE, "second search result");
		}
		return this;
	}

	/**
	 * Name of the method: clickOnSearchResult Description: method to click on
	 * search result using JDE Description Author:Manisha Parameters: jde
	 * description
	 */
	public ProductDetailSearchPage clickOnSearchResult(String description) {
		for (int i = 0; i < 10; i++) {
            if (isVisible(getElementByReplaceText(searchResult, description), "Specific item code")) {
                WaitForMiliSec(3000);
                click(getElementByReplaceText(searchResult, description), WaitLogic.CLICKABLE,"specificItemCode");
                
                break;
            }
            else {
                System.out.println("Scroll Start");
                WebElement element = DriverManager.getDriver().findElement(By.xpath("//table[@class='v-table-table']//tr[last()]//td[3]/div"));
                ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);",
                        element);
                WaitForMiliSec(5000);
                System.out.println("Scroll Done");
            }
                
            }
        return this;
	}

	/**
	 * Name of the method: searchItemNumberInRegionalCatalogAndNavigateToTab
	 * Description: method to search item in regional catalog and navigate to
	 * mentioned tab Author:Manisha Parameters: itemtype, catalogtype, itemnumber
	 * and tabname
	 */
	public ProductDetailSearchPage searchItemNumberInRegionalCatalogAndNavigateToTab(String ItemType,
			String CatalogType, String SearchValue, String TabName) {
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(ItemType).selectCatalogType(CatalogType)
				.enterHsiItemNumber(SearchValue).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(TabName);
		qualitystatus.maximizeQualityStatusTab();
		clickRefreshIcon();
		return this;
	}

	/**
	 * Name of the method: searchItemAndNavigateToTab Description: method to search
	 * item in regional and master catalog and navigate to mentioned tab
	 * Author:Manisha Parameters: CatalogType and tabName
	 */
	public ProductDetailSearchPage searchItemInMasterCatalogAndNavigateToTab(String Catalog, String TabName) {
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(Catalog).clickSeachButton()
				.clickOnSearchResultOnMasterCatalog().selectTabfromDropdown(TabName).clickRefreshIcon();
		qualitystatus.maximizeQualityStatusTab();
		return this;
	}

	/**
	 * Name of the method: getHSIItemNumber Description: method to get item number
	 * from search result Author:Manisha Parameters:
	 */
	public String getHSIItemNumber() {
		waitUntilBufferingIconDisappear();
		return getStringValues(hsiItemNumber, WaitLogic.VISIBLE, "HSI Item Number");
	}

	/**
	 * Name of the method: waitUntilBufferingIconDisappear
	 * Description: method to wait until loading icon disappear
	 * Author:Manisha
	 * Parameters: 
	 */
	public ProductDetailSearchPage waitUntilBufferingIconDisappear() {
		if (isVisible(loadingIcon, "Loading Element", 5)) {
			waitUtilLoadingIconDisappear(loadingIcon, WaitLogic.INVISIBLE, "Product Load page");
		}
		return this;
	}

	/**
	 * Name of the method: taskSearchThroughTextField
	 * Description: method to search task by applying filter via text field
	 * Author:Manisha 
	 * Parameters:subDropdownValue and dropdownValue
	 */
	public ProductDetailSearchPage taskSearchThroughTextField(String subDropdownValue, String dropdownValue) {
		waitUntilBufferingIconDisappear();
		click(filterIcon, WaitLogic.CLICKABLE, "filterIcon");
		click(filterSelectButtontask, WaitLogic.CLICKABLE, "Filter Select Button");
		selectTextViaKeyboard();
		deleteSelected();
		WaitForMiliSec(1000);
		DriverManager.getDriver().switchTo().activeElement().click();
		WaitForMiliSec(3000);
		DriverManager.getDriver().switchTo().activeElement().sendKeys(subDropdownValue);
		click(getElementByReplaceText(tabSearchxpath, subDropdownValue), WaitLogic.CLICKABLE, "tabname");
		WaitForMiliSec(3000);
		sendKeys(filterByName, dropdownValue, WaitLogic.PRESENCE, "dropdown value is selected");
		WaitForMiliSec(3000);
		click(filterButton, WaitLogic.CLICKABLE, "filterButton");
	   return this;	
	}	

	/**
	 * Name of the method: getHSIItemNumberByJDEDescription 
	 * Description: method to get HSI itemNumber by JDE Description
	 * Author:Manisha 
	 * Parameters: JDE Description
	 * 
	 */
	public String getHSIItemNumberByJDEDescription(String desc) {
		waitUntilBufferingIconDisappear();
		return getStringValues(getElementByReplaceText(getHSIItemByJDE,desc), WaitLogic.VISIBLE, "HSI Item Number");
	}
	/**
	 * Name of the method: clickShortSelectionOption
	 * Description: method to click Short Button in Gear Icon
	 * Author:Jagadeesh
	 */
	public ProductDetailSearchPage clickHSISelectionOption() {
		WaitForMiliSec(2000);
		click(hsiBtn, WaitLogic.CLICKABLE, "HSU Btn");
		WaitForMiliSec(5000);
		return this;
	}
	/**
	 * Name of the method: getDisplayedProductNotes
	 * Description: method to get Displayed Product Notes
	 * Author:Jagadeesh
	 */

	public String getDisplayedProductNotes() {

		WaitForMiliSec(2000);
		return getStringValues(productNoteFieldValue,WaitLogic.VISIBLE,"Product Note value");
	}
	public String getDisplayedValue() {
		WaitForMiliSec(2000);
		return getStringValues(productNoteFieldValue,WaitLogic.VISIBLE,"Displayed value");
	}

}

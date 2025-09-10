package com.pim.pages;

import com.pim.enums.LogType;
import com.pim.enums.WaitLogic;
import com.pim.factories.ExplicitWaitFactory;
import com.pim.utils.ExcelUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import java.io.IOException;
import java.util.Map;

import static com.pim.reports.FrameworkLogger.log;

public class WebDescription extends BasePage {

	public By WebDecriptionTab = By.xpath("//div[@class='v-captiontext' and contains(text(),'Web Description')]");
	//Division title and dropdown
	private final By divisionTitle = By.xpath("//span[contains(text(),'Division')]");
	private final By divisionDropdown = By.xpath("//span[contains(text(),'Division')]/ancestor::tr//td[@class='v-formlayout-contentcell']//div[@class='v-filterselect-button']");
	private final By divisionTextfield = By.xpath("//span[contains(text(),'Division')]/ancestor::tr//td[@class='v-formlayout-contentcell']//input");
	//after entering text on textfield will get option for suggested menu. for both division and language dropdown
	private final By divisionlanguagedropdownSuggestmenu = By.xpath("//div[@class='v-filterselect-suggestmenu']");
	private final By divisionDropdwonvalues = By.xpath("//div[@class='v-filterselect-suggestmenu']//table//tbody//tr"); //list value

	//locator for divisional value
	private final By divisionValues = By.xpath("//td[@class='gwt-MenuItem gwt-MenuItem-selected']//span");
	private final By divisionValuesZahn = By.xpath("//span[contains(text(),'Zahn')]");
	private final By divisionValuesMedical = By.xpath("//td[@class='gwt-MenuItem']//span[contains(text(),'Medical')]");
	private final By divisionValuesDental = By.xpath("//td[@class='gwt-MenuItem gwt-MenuItem-selected']//span[contains(text(),'Dental')]");

	//Language title and dropdown
	private final By languageTitle = By.xpath("//span[contains(text(),'Language')]");
	private final By languageDropdown = By.xpath("//span[contains(text(),'Language')]/ancestor::tr//td[@class='v-formlayout-contentcell']//div[@class='v-filterselect-button']");
	private final By languageTextfield = By.xpath("//span[contains(text(),'Language')]/ancestor::tr//td[@class='v-formlayout-contentcell']//input[@type='text']");
	private final By languageDropdownvalues = By.xpath("//div[@class='v-filterselect-suggestmenu']/../..//tr//td//span");
	private final String selectingValuesFromLaungugeField = "//div[@class='v-filterselect-suggestmenu']/../..//tr//td//span[contains(text(),'${variable}')]";

	//full display description title and textfield
	private final By fddTitle = By.xpath("//span[contains(text(),'Full Display Description:')]");
	public  By fddTextfield = By.xpath("(//span[contains(text(),'Full Display Description:')]/ancestor::tr//td)[3]");
	private final By fddTextfieldDropDown  = By.xpath("//span[normalize-space()='Full Display Description:']/ancestor::tr/td[3]//div[@class='v-filterselect-button']");


	//Abbreviated Display Description:
	private final By addTitle = By.xpath("//span[contains(text(),'Abbreviated Display Description:')]");
	public By addTextfield = By.xpath("(//span[contains(text(),'Abbreviated Display Description:')]/ancestor:: tr//td[@class='v-formlayout-contentcell']//div)[7]");

	//Look ahead Search description
	private final By lsdTitle = By.xpath("//span[contains(text(),'Look Ahead Search Description:')]");
	public By lsdTextField = By.xpath("(//span[contains(text(),'Look Ahead Search Description:')]/ancestor:: tr//td[@class='v-formlayout-contentcell']//div)[7]");

	//Search Description
	private final By sdTitle = By.xpath("(//span[contains(text(),'Search Description:')])[2]");
	public By sdTextField = By.xpath("((//span[contains(text(),'Search Description:')])[2] /ancestor::tr//td[@class='v-formlayout-contentcell'] //div)[7]");

	//Product overview
	private final By poTitle = By.xpath("//span[contains(text(),'Product Overview ')]");
	public By poTextField = By.xpath("(//span[contains(text(),'Product Overview ')]/ancestor::tr/td[@class='v-formlayout-contentcell']//div)[7]");

	//Extended Web Description:
	private final By ewdTitle = By.xpath("//span[contains(text(),'Extended Web Description:')]");
	public By ewdTextField = By.xpath("(//span[contains(text(),'Extended Web Description:')]/ancestor:: tr//td[@class='v-formlayout-contentcell']//div)[7]");

	//print catalog description
	private final By pcdTitle = By.xpath("//span[contains(text(),'Print Catalog Description:')]");
	public By pcdTextField = By.xpath("(//span[contains(text(),'Print Catalog Description:')]/ancestor::tr//td[@class='v-formlayout-contentcell'] //div)[7]");
	private final String tabSearchxpath = "//span[normalize-space()='${variable}']";
	private final By taskLink = By.xpath("//*[@id='view.container.context.iconstrip']/div/div/div[1]/div/div/div/div/div[2]/div/div/div[4]/button");
	private final By taskFilterTextbox = By.xpath("//*[@id='task_tree']//input");
	private final By taskFilterIcon = By.xpath("//span[contains(@class,'v-menubar-menuitem v-menubar-menuitem-hpmw-filter')]");
	private final By updatedTaskInRowFilter = By.xpath("//*[@id='task_table']/div[2]");

	//classification Code
	private final By classificationCode = By.xpath("//span[contains(text(),'Classification')]/ancestor::td/following-sibling::td//div[@class='v-label v-widget hpmw-inline-textfield v-label-hpmw-inline-textfield v-has-width']");

	public WebDescription clickWebDescriptionTab() {
		click(WebDecriptionTab, WaitLogic.PRESENCE, "password");
		return this;
	}

	protected WebDescription selectDivisionTypevalueFromDropDown(By element, String tabname) {
		click(element, WaitLogic.CLICKABLE, "dropdownbuttonfortab");
		click(getElementByReplaceText(tabSearchxpath, tabname), WaitLogic.CLICKABLE, "DropdownValue");
		return this;
	}

	public void SelectLaungugeAndDivisionTypeDropDown(String Divisionvalue, String languageValue) throws InterruptedException {
		selectDivisionTypevalueFromDropDown(divisionDropdown, Divisionvalue);
		Thread.sleep(2000);
		selectDivisionTypevalueFromDropDown(languageDropdown, languageValue);
	}

	public boolean isWebDescriptionFieldVisible(String fieldname) {
		By by = null;
		try {
			by = (By) WebDescription.class.getField(fieldname).get(WebDescription.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldname);
	}

	public String getFullDisplayDescription() {
		String fullDisplayDescription = getStringValues(fddTextfield, WaitLogic.VISIBLE, "get FullDisplayDescription Value");
		return fullDisplayDescription.trim();
	}

	public String getStiboDescription() {
		String catlogDescription = getStringValues(pcdTextField, WaitLogic.VISIBLE, "get Catalogdescription Value");
		return catlogDescription.trim();
	}


	public String getSearchDescription() {
		String searchDescription = getStringValues(sdTextField, WaitLogic.VISIBLE, "get SearchDescription Value");
		return searchDescription.trim();
	}


	public WebDescription searchTaskByFilter(String taskName) {
		click(taskLink, WaitLogic.PRESENCE, "password");
		sendKeys(taskFilterTextbox,taskName,WaitLogic.CLICKABLE,"Enter Task Name");
		click(getElementByReplaceText(tabSearchxpath, taskName), WaitLogic.CLICKABLE, "taskName");
		return this;
	}


	public String getTaskFilterRecord() {
		String taskDescription = getStringValues(updatedTaskInRowFilter, WaitLogic.VISIBLE, "get updated task Value");
		return taskDescription.trim();
	}

	public String getLanguage() {
		WaitForMiliSec(5000);
		String language = getAttributeValues(languageTextfield, WaitLogic.VISIBLE, "get Language Value");
		WaitForMiliSec(5000);
		return language.trim();
	}

	public WebDescription selectLanguageFieldDropdown(String Launguage) {
		WaitForMiliSec(3000);
		click(languageDropdown, WaitLogic.CLICKABLE, "Clicked on LaunguageFieldDropdown");
		WaitForMiliSec(1000);
		click(getElementByReplaceText(selectingValuesFromLaungugeField, Launguage), WaitLogic.CLICKABLE, Launguage);
		WaitForMiliSec(1000);
		return this;

	}

	//To get classification code
	public String getClassificationCode(){
		String classificationCodeDivision = getStringValues(classificationCode,WaitLogic.VISIBLE,"get classification code");
		return classificationCodeDivision;
	}

    //To get selected Dental division from the divisional dropdown values
	public String getDentalDivision(){
		WaitForMiliSec(3000);
		click(divisionDropdown, WaitLogic.CLICKABLE, "click on divisionDropdown");
		WaitForMiliSec(2000);
		click(divisionValuesDental, WaitLogic.CLICKABLE, "click on division");
		WaitForMiliSec(2000);
		String selectedDivision = getAttributeValues(divisionTextfield, WaitLogic.VISIBLE, "selected division");
		return selectedDivision;

	}
	
	 //To get selected Medical division from the divisional dropdown values
	public String getMedicalDivision(){
		WaitForMiliSec(3000);
		click(divisionDropdown, WaitLogic.CLICKABLE, "click on divisionDropdown");
		WaitForMiliSec(2000);
		click(divisionValuesMedical, WaitLogic.CLICKABLE, "click on division");
		WaitForMiliSec(2000);
		String selectedDivision = getAttributeValues(divisionTextfield, WaitLogic.VISIBLE, "selected division");
		return selectedDivision;

	}
		
		 //To get selected division from the divisional dropdown values
	public String getZahnDivision(){
		WaitForMiliSec(3000);
		click(divisionDropdown, WaitLogic.CLICKABLE, "click on divisionDropdown");
		WaitForMiliSec(2000);
		click(divisionValuesZahn, WaitLogic.CLICKABLE, "click on division");
		WaitForMiliSec(2000);
		String selectedDivision = getAttributeValues(divisionTextfield, WaitLogic.VISIBLE, "selected division");
		return selectedDivision;

	}
}

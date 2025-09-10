package com.pim.pages;

import com.pim.constants.Constants;
import com.pim.driver.DriverManager;
import com.pim.enums.LogType;
import com.pim.enums.WaitLogic;
import com.pim.factories.ExplicitWaitFactory;
import com.pim.utils.ApplicationUtils;
import com.pim.utils.Javautils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.pim.reports.FrameworkLogger.log;

public class ClassificationsPage extends BasePage {
	private final By deleteButton = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-remove']");
	private final By classifyItem = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-classify']");
	private final By nameOfStructureSystem = By.xpath("//input[@class='v-filterselect-input']");
	private final By structureGroupPath = By.xpath("//*[contains(text(),'Name of structure system')]");
	private final By settings = By.xpath("//*[contains(text(),'Structure group path')");
	private final String primaryTaxonomypath = "//tr//div[contains(text(),'${variable}')]/ancestor::td/following-sibling::td//div";
	private final String ecommerceTaxonomypath = "//tr//div[contains(text(),'${variable}')]/ancestor::td/following-sibling::td//div";
	private final String itemMessageXpath="//div[contains(text(),'${variable1}')]/../..//div[contains(text(),'${variable2}')]";
	private final By yesButton = By.xpath("(//div[@role='button' and @class='v-button v-widget'])[1]");
	private By itemMessageElement;
	private final String errorMsg = "//div[text()='${variable}']";
	private final String selectDivisionType = "//div[text()='${variable}']/span";
	private final By itemCodeList =By.xpath("(//div[@id=\"article_table\"]//table/tbody)[2]/tr/td[2]");
	private final By hsiItemCodeList=By.xpath("//div[@class=\"v-scrollable v-table-body-wrapper v-table-body\"]//tbody//td[2]/div");
	private final String itemList = "//div[@class=\"v-scrollable v-table-body-wrapper v-table-body\"]//tbody//td[2]/div[contains(text(),'${variable}')]";
	private final By openExpandButtonXpath =By.cssSelector(".v-button-hpmw-expand-all-button>span");
	private final By closeExpandButtonXpath=By.xpath("//span[@class='v-button-wrap']");
	public final String UNSPSC_Code = "(//td[@class='v-table-cell-content']//div[contains(text(),'UNSPSC Taxonomy')])";
    public By PrimaryTaxonomy = By.xpath("//div[text()='Primary Taxonomy']");
	public By EcommerceTaxonomy = By.xpath("//div[text()='Zahn E-Commerce Taxonomy']");
	private final String permissionRequiredErrorMessage = "//div[contains(text(),'${variable}')]";
	private final By permissionRequiredCancelButton=By.xpath("//span[contains(text(),'Cancel')]");
	private final String gepEcommerceTaxonomypath = "//tr//div[contains(text(),'${variable}')]/ancestor::td/following-sibling::td//div";
	private final String zNewGlobalTaxonomypath = "//tr//div[contains(text(),'${variable}')]/ancestor::td/following-sibling::td//div";

	private final String unspscTaxonomypath = "//tr//div[contains(text(),'${variable}')]/ancestor::td/following-sibling::td//div";

	public boolean verifyItemErrorMessageforAllDivisions(String taxonomyName,String errorOrWarning,String divisionName,String errorOrWarningMessage) 
	{
		List<WebElement> classificationTabList = DriverManager.getDriver().findElements(By.cssSelector("#detail_classification_table tr.v-table-row"));
		System.out.println(classificationTabList.size());
		for (WebElement classificationTableRows : classificationTabList) {
			String rowsText = classificationTableRows.getText();
			if(rowsText.contains(taxonomyName) & rowsText.contains(errorOrWarning) & rowsText.contains(divisionName) & rowsText.contains(errorOrWarningMessage))
			{
				return true;
			}
		}
		return false;
	}
	public ClassificationsPage selectDivisionAndErrorMsg(String divisonType,String errorMessage) {
		click(getElementByReplaceText(selectDivisionType, divisonType),WaitLogic.CLICKABLE,  divisonType);
		WaitForMiliSec(5000);
		scrollToElement(getElementByReplaceText(errorMsg, errorMessage),"");
		click(getElementByReplaceText(errorMsg, errorMessage), WaitLogic.VISIBLE, "errorMsg");
		WaitForMiliSec(8000);
		return this;
	}
	public List<String> getHSIItemCodeList()
	{
		List<WebElement> itemCodeListSize=DriverManager.getDriver().findElements(itemCodeList);
		List<String> listItemCode= new ArrayList<String>();
		for (WebElement itemCodeTableRows : itemCodeListSize)
		{
			String itemCodeNumber = itemCodeTableRows.getText();
			listItemCode.add(itemCodeNumber);
		}
		WaitForMiliSec(4000);
		return listItemCode;
	}


	public List<String> selectMultipleuitemcodeList() {

		List<WebElement> hsiitemCodeListSize=DriverManager.getDriver().findElements(hsiItemCodeList);
		List<String> listHSIItemCode= new ArrayList<String>();
		for (WebElement itemCodeTableRows : hsiitemCodeListSize)
		{	
			String itemCodeNumber = itemCodeTableRows.getText();
			listHSIItemCode.add(itemCodeNumber);
		}
		for (int i = 0; i <= listHSIItemCode.size()-1; i++)
		{
			String itemCode = listHSIItemCode.get(i);
			System.out.println(listHSIItemCode.get(i));
			click(getElementByReplaceText(itemList, itemCode), WaitLogic.CLICKABLE, "ItemNumber");
			WaitForMiliSec(4000);
		}

		WaitForMiliSec(8000);
		return listHSIItemCode;
	}

	public void clickOnExpandButton()
	{
		WaitForMiliSec(5000);
		click(openExpandButtonXpath,WaitLogic.CLICKABLE, "Expand Button");
		WaitForMiliSec(5000);
	}

	public void clickOnCloseExpandButton()
	{
		WaitForMiliSec(10000);
		click(closeExpandButtonXpath,WaitLogic.CLICKABLE, "close Expand Button");
		WaitForMiliSec(5000);
	}

	public String clickClassificationItem(String item, String item1) {
		itemMessageElement = getElementByReplaceTexts(itemMessageXpath, item, item1 );
		String text = getStringValues(itemMessageElement, WaitLogic.VISIBLE,"get structure group");
		String lastWord= ApplicationUtils.getLastWord(text);
		click(itemMessageElement, WaitLogic.CLICKABLE, "ClassificationItem element");
		WaitForMiliSec(5000);
		return lastWord;
	}
	public String getPrimaryTaxonomy(String primary) {
		WaitForMiliSec(30000);
		scrollToElement(getElementByReplaceText(primaryTaxonomypath, primary),"Primary taxonomy");
		String text = getStringValues(getElementByReplaceText(primaryTaxonomypath, primary),WaitLogic.VISIBLE,"get primary taxonomy");
		String primarytaxonomy = ApplicationUtils.getTaxonomyId(text);
		return primarytaxonomy;
	}

	public String getEcommerceTaxonomy(String ecommerce){
		String text = getStringValues(getElementByReplaceText(ecommerceTaxonomypath, ecommerce),WaitLogic.VISIBLE,"get ecommerce taxonomy");
		String ecommercetaxonomy = ApplicationUtils.getTaxonomyId(text);
		return ecommercetaxonomy;
	}

	//It will return any structure group
	public String getStructureGroup(String structureSystem, String structurePath) {
		itemMessageElement = getElementByReplaceTexts(itemMessageXpath, structureSystem, structurePath );
		String text = getStringValues(itemMessageElement, WaitLogic.VISIBLE,"get structure group");
		String lastWord= ApplicationUtils.getLastWord(text);
		return lastWord;
	}

	public String getStructureGroupHavingBlankSpace(String structureSystem, String structurePath) {
		itemMessageElement = getElementByReplaceTexts(itemMessageXpath, structureSystem, structurePath );
		String text = getStringValues(itemMessageElement, WaitLogic.VISIBLE,"get structure group");
		String lastTwoWords= ApplicationUtils.getLastTwoWord(text);
		return lastTwoWords;
	}

	public void clickStructureGroup(){
		click(itemMessageElement, WaitLogic.CLICKABLE, "structure group");
		WaitForMiliSec(5000);
	}

	public void removeButton(){
		click(deleteButton, WaitLogic.CLICKABLE, "removed button ");
		WaitForMiliSec(7000);
	}

	public void clickYes(){
		click(yesButton, WaitLogic.CLICKABLE, "Yes Button");
		WaitForMiliSec(2000);
	}

	public void sortStructureGroup() {
		click(structureGroupPath, WaitLogic.CLICKABLE, "structure group path");
		WaitForMiliSec(2000);
	}

    //To check UNSPSC Code, Primary and Ecommerce Taxonomy feilds
	public boolean isAllAttributesVisible(String fieldName) {
		By by = null;
		try {
			by = (By) ClassificationsPage.class.getField(fieldName).get(ClassificationsPage.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isVisible(by, fieldName);
	}
	
	public boolean isPermissionErrorMessageVisible(String errorMessageText) {
		WaitForMiliSec(5000);
		By errorMessageXpath = getElementByReplaceText(permissionRequiredErrorMessage, errorMessageText);
		return isVisible(errorMessageXpath, "errorMessageText");
	}
	
	public ClassificationsPage closePermissionRequiredErrorMessage() {
		WaitForMiliSec(1000);
		moveToElementAndClick(permissionRequiredCancelButton, "permissionRequiredCancelButton");
		return this;
	}

	public void deleteMessageIfExisit(String structureName, String structurepath){
		itemMessageElement = getElementByReplaceTexts(itemMessageXpath, structureName, structurepath);
		if(isVisible(itemMessageElement, structurepath)){
			click(itemMessageElement, WaitLogic.CLICKABLE, structurepath + " ");
			WaitForMiliSec(5000);
			removeButton();
			clickYes();
			log(LogType.INFO, structurepath + " is deleted");
		}else {
			log(LogType.INFO, structurepath + " is not present");
		}
	}

	public boolean isMessageAdded(String structureName, String structurepath){
		itemMessageElement = getElementByReplaceTexts(itemMessageXpath, structureName, structurepath);
		return isVisible(itemMessageElement, structurepath);
	}
	public String getGepEcommerceTaxonomy(String gepEcommerce){
		String text = getStringValues(getElementByReplaceText(gepEcommerceTaxonomypath, gepEcommerce),WaitLogic.VISIBLE,"get GEP ecommerce taxonomy");
		String gepecommercetaxonomy = ApplicationUtils.getFormattedTaxonomyId(text);
		return gepecommercetaxonomy; // 3000-650-40 for (Item Category ID)
	}

	public String getFormattedPrimaryTaxonomy(String primaryEcommerce){
		String text = getStringValues(getElementByReplaceText(primaryTaxonomypath, primaryEcommerce),WaitLogic.VISIBLE,"get Primary Taxonomy");
		String primaryTaxanomy = ApplicationUtils.getFormattedTaxonomyId(text);
		return primaryTaxanomy; //005-07-30-01 for (Dimension Class Name)
	}
//	public String getFormattedZNewGlobalMessage(String gepEcommerce){
//		String text = getStringValues(getElementByReplaceText(gepEcommerceTaxonomypath, gepEcommerce),WaitLogic.VISIBLE,"get zNew Global Message");
//		String gepecommercetaxonomy = ApplicationUtils.getFormattedTaxonomyId(text);
//		return gepecommercetaxonomy;
//	}
//	public String getFormattedZNewGlobalMessage(String zNewGlobalMessage) {
//		String text = getStringValues(getElementByReplaceText(zNewGlobalTaxonomypath, zNewGlobalMessage),WaitLogic.VISIBLE, "get zNew Global Message");
//		return ApplicationUtils.getFormattedGlobalMessageId(text); //MEDICAL_OTP for(Global Messages ID)
//	}
	public String getFormattedZNewGlobalMessage(String zNewGlobalMessage) {
		String text = getStringValues(getElementByReplaceText(zNewGlobalTaxonomypath, zNewGlobalMessage),WaitLogic.VISIBLE, "get zNew Global Message");
		return ApplicationUtils.getFormattedGlobalMessageId(text); //MEDICAL_OTP for(Global Messages ID)
	}

	public String getFormattedUNSPSCTaxonomy(String UNSPSCTaxonomy){
		String text = getStringValues(getElementByReplaceText(unspscTaxonomypath, UNSPSCTaxonomy),WaitLogic.VISIBLE,"get UNSPSC Taxonomy");
		String uNSPSCTaxonomyFormatted = ApplicationUtils.getFormattedTaxonomyId(text);
		return uNSPSCTaxonomyFormatted; // 3000-650-40 for (Item Category ID)
	}

	public String getUNSPSCTaxonomy(String UNSPSCTaxonomy){
		String uNSPSCTaxonomy = getStringValues(getElementByReplaceText(UNSPSC_Code, UNSPSCTaxonomy),WaitLogic.VISIBLE,"get UNSPSC Taxonomy");
		//String uNSPSCTaxonomy = ApplicationUtils.getFormattedTaxonomyId(text);
		return uNSPSCTaxonomy; // 3000-650-40 for (Item Category ID)
	}


}
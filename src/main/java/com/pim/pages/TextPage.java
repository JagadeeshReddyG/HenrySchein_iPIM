package com.pim.pages;
import com.pim.enums.WaitLogic;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.Javautils;
import org.openqa.selenium.By;
import org.testng.Assert;
import java.awt.*;
import java.awt.event.KeyEvent;

public class TextPage extends BasePage {
    //locators to select dropdown values in the text feilds
    public  By languageTypeAttribute = By.xpath("//span[contains(text(),'Languages')]/ancestor::tr");
    private final By languageTypeAttributeBox =
            By.xpath("//div[@class='v-filterselect v-widget hpmw-languageComboBox v-filterselect-hpmw-languageComboBox']//input[@class='v-filterselect-input']");
    public  By optionAttribute = By.xpath("//span[contains(text(),'Option')]/ancestor::tr");
    private final By optionAttributeBox =
            By.xpath("(//span[text()='Option:']//following::td[@class='v-formlayout-contentcell'])[1]//div[2]");
    private final By TextFeild =
            By.xpath("//div[contains(@class,'v-filterselect v-widget hpmw-inline-combobox v-filterselect-hpmw-inline-combobox v-has-width')]//input[@type='text']");
    private final String selectValue = "//span[text()='${variable}']";
    public  By webCatalogAttribute = By.xpath("//span[contains(text(),'Web Catalog')]/ancestor::tr");
    private final By webCatalogAttributeBox =
            By.xpath("(//span[contains(text(),'Web Catalog:')]//following::td[@class='v-formlayout-contentcell'])[1]//div[2]");
    private final By CAWebCatalogAttributeBox = By.xpath("(//span[contains(text(),'CA Web Catalog:')]//following::td[@class='v-formlayout-contentcell'])[1]//div[2]");
    private final By FieldDropDown = By.xpath("//div[@class='v-csslayout v-layout v-widget v-has-width']//div[@class='v-filterselect-button']");
    private final By FeildDropDownValue = By.xpath("//td[@class='gwt-MenuItem gwt-MenuItem-selected']");
    private final By FieldDropDown1 = By.xpath("//div[@class='v-csslayout v-layout v-widget v-has-width']//div[@class='v-filterselect-button']");
    private final String selectValue1 = "//span[text()='${variable}']";
    public By hazardousTypeAttribute = By.xpath("//span[contains(text(),'Hazardous')]/ancestor::tr");
    private final By classificationTypeText = By.xpath("//td[@class='gwt-MenuItem gwt-MenuItem-selected']//span");


    //locators to enter codeName and KeywordType Values in the text feilds
    public  By codeNameAttribute = By.xpath("//span[contains(text(),'Name')]/ancestor::tr");
    public  By keywordsAttribute = By.xpath("//span[contains(text(),'Keywords')]/ancestor::tr");

    //to locate description attributes
    public By descriptionAttribute = By.xpath("//span[contains(text(),'Description')]/ancestor::tr");
    public By descriptionAttributeBox = By.xpath("//div[contains(@class,'v-csslayout v-layout v-widget hpmw-inlineCcontainer v-csslayout-hpmw-inlineCcontainer v-has-width')]//pre/..");
    public By descriptionTextField = By.xpath("//textarea[contains(@class,'v-textarea v-widget hpmw-multiline-textfield v-textarea-hpmw-multiline-textfield')]");

    //to locate message attributes
    public By messageTypeAttribute = By.xpath("//span[contains(text(),'Message Type')]/ancestor::tr");
    private final By messageTypeAttributeBox = By.xpath("//div[@class='v-csslayout v-layout v-widget hpmw-inlineCcontainer v-csslayout-hpmw-inlineCcontainer v-has-width']//div[contains(text(),'No content #$#')]");
    private final By messageTypeTextFeild = By.xpath("//div[@class='v-csslayout v-layout v-widget v-has-width']//input[@type='text']");

    //to locate hazardous attributes
    public By hazardousAttributeBox = By.xpath("//div[@class='v-csslayout v-layout v-widget hpmw-inlineCcontainer v-csslayout-hpmw-inlineCcontainer v-has-width']//div[@class='v-label v-widget v-has-width']");
    //public By ItemCodeValue= By.xpath("//div[@class='v-label v-widget hpmw-inline-textfield v-label-hpmw-inline-textfield v-has-width']");
    private final String ItemCodeValue = "((//div[contains(text(),'${variable}')])[1]//ancestor::tr//td[@class='v-table-cell-content']//div[@class='v-table-cell-wrapper'])[1]";

    //locators to set effective date
    public  By effectiveDateAttribute = By.xpath("//span[contains(text(),'Effective Date:')]/ancestor::tr");
    private final By effectiveDateTextBox = By.xpath("(//span[contains(text(),'Effective Date:')]/ancestor::td/following-sibling::td)[2]//div[@class='v-panel v-widget light v-panel-light v-has-width']");
    private final By effectiveDateTextFeild = By.xpath("//div[@class='v-csslayout v-layout v-widget']//input[@type='text']");

   //locators to set expiration date
    public  By expirationDateAttribute = By.xpath("//span[contains(text(),'Expiration Date:')]/ancestor::tr");
    private final By expirationDateTextBox = By.xpath("(//span[contains(text(),'Expiration Date:')]/ancestor::td/following-sibling::td)[2]//div[@class='v-panel v-widget light v-panel-light v-has-width']");
    private final By expirationDateTextFeild = By.xpath("//div[@class='v-csslayout v-layout v-widget']//input[@type='text']");

    //locators to verify French Format Attributes in summary view of the selected taxonomy
    public By description_French = By.xpath("//span[contains(text(),'Description (French):')]");
    public By keywords_French = By.xpath("//span[contains(text(),'Keywords (French):')]");

    //locators to select main and sub category for different categories in the structures group
    private final String mainCategoryDropdown = "//*[contains(text(),'${variable}')]//span[1]";
    private final String subCategoryDropdown = "(//div[contains(text(),'${variable}')])[1]";

    //locators to verify lastChangedBy and LastChangedOn Attribute value in Headers Tab
   private final By lastChangedByAttribute = By.xpath("By.xpath((//div[@class='v-label v-widget v-has-width'])[1]");
   private final By lastChangedOnAttribute = By.xpath("By.xpath((//div[@class='v-label v-widget v-has-width'])[2]");

   //locator for filter button
   private final By filterButton = By.xpath("//div[@class='v-slot v-slot-hpmw-quick-search-button']//span[@class='v-button-wrap']");
   public  By autoAssignedItemMessage_GLOBAL_L20 = By.xpath("//div[contains(text(), 'GLOBAL_LC')]");

    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();

    //Verify dropdown values selection for languageTypeAttribute
    public TextPage verifyLanguageTypeDropDownValues( String languageType ){
        click(languageTypeAttributeBox,  WaitLogic.CLICKABLE, "languageTypeBox");
        WaitForMiliSec(2000);
        clearField(TextFeild, WaitLogic.CLICKABLE, "languageFeild");
        WaitForMiliSec(2000);
        sendKeys(TextFeild, languageType, WaitLogic.VISIBLE, "languageType");
        WaitForMiliSec(2000);
        click(getElementByReplaceText(selectValue, languageType), WaitLogic.CLICKABLE, "languageType");
        return this;
    }
    public String getLanguageAttribute(){
        String languageType = getStringValues(languageTypeAttributeBox,WaitLogic.CLICKABLE, "languageTypeBox");
        return languageType;
    }


    public String getItemCode(String specificItemCode){
    	String languageType = getStringValues(getElementByReplaceText(ItemCodeValue, specificItemCode),WaitLogic.CLICKABLE, "Getting Specific code");
    	return languageType;
    }

    //Verify given description in DescriptionAttribute
    public String verifyDescriptionAttributeMessage()  {
        String Description = getStringValues(descriptionAttributeBox,WaitLogic.CLICKABLE, "descriptionBox");
        String dynamicDesc=addDynamicPattern(Description);
        try {
            WaitForMiliSec(2000);
            click(descriptionAttributeBox, WaitLogic.CLICKABLE, "descriptionBox");
        }
        catch (Exception e){
            productDetailSearchPage.clickRefreshIcon();
            click(descriptionAttributeBox, WaitLogic.CLICKABLE, "descriptionBox");
        }
        WaitForMiliSec(2000);
        selectTextViaKeyboard();
        deleteSelected();
        try {
            WaitForMiliSec(2000);
            sendKeys(descriptionTextField, dynamicDesc, WaitLogic.VISIBLE, "descriptionFeild");
        }
        catch (Exception e){
            productDetailSearchPage.clickRefreshIcon();
        click(descriptionAttributeBox, WaitLogic.CLICKABLE, "descriptionBox");
        sendKeys(descriptionTextField, dynamicDesc, WaitLogic.VISIBLE, "descriptionFeild");
        }
        return dynamicDesc;
    }
    public String addDynamicPattern(String Description){
        String pattern = Description;
        if(Description.contains("#$#"))
            pattern = Description.split("#$#")[0];
        String currentDate = DateandTimeUtils.getTodaysDate();
        pattern = pattern+ "#$#" + currentDate.toString();
        return pattern;
    }
    public String verifyMasterDescription(){
        String Description = getStringValues(descriptionAttributeBox,WaitLogic.CLICKABLE, "descriptionBox");
        return  Description;
    }

    //Verify given Current Date in EffectiveDateAttribute
    public TextPage verifyEffectiveDateFeild(){
        click(effectiveDateTextBox, WaitLogic.CLICKABLE, "effectiveDateBox");
        WaitForMiliSec(2000);
        Javautils.selectAllAndDeletingThroughKeboard();
        String currentDate = DateandTimeUtils.getTodaysDate();
        WaitForMiliSec(2000);
        sendKeys(effectiveDateTextFeild, currentDate, WaitLogic.VISIBLE, "effectiveDateFeild");
        return this;
    }
    public String getEffectiveDate(){
        String Date = getStringValues(effectiveDateTextBox,WaitLogic.CLICKABLE, "effectiveDateBox");
        return Date;
    }
    public String todaysDate(){
        String currentDate = DateandTimeUtils.getTodaysDate();
        return currentDate;
    }

    //Verify given Expiry Date in ExpirationDateAttribute Feild
    public TextPage verifyExpirationDateFeild(){
        click(expirationDateTextBox, WaitLogic.CLICKABLE, "expirationDateBox");
        WaitForMiliSec(2000);
        Javautils.selectAllAndDeletingThroughKeboard();
        String expiryDate = DateandTimeUtils.getFutureDate();
        WaitForMiliSec(2000);
        sendKeys(expirationDateTextFeild, expiryDate, WaitLogic.VISIBLE, "expirationDateFeild");
        WaitForMiliSec(2000);
        return this;
    }
    public String getExpirationDate() {
        String Date = getStringValues(expirationDateTextBox,WaitLogic.CLICKABLE, "expirationDateBox");
        return Date;
    }
    public String addedFiveDaysFromToday(){
        String expiryDate = DateandTimeUtils.getFutureDate();
        return expiryDate;
    }

    //Verify dropdown values selection for OptionsAttribute
    public TextPage verifyOptionsTypeDropDownValues( String optionsValue ){
        WaitForMiliSec(2000);
        click(optionAttributeBox, WaitLogic.CLICKABLE, "optionBox");
        WaitForMiliSec(2000);
        try {
            click(FieldDropDown, WaitLogic.CLICKABLE, "DropDown");
        }
        catch (Exception e){
            click(FieldDropDown1, WaitLogic.CLICKABLE, "DropDown");
        }
        WaitForMiliSec(2000);
        click(getElementByReplaceText(selectValue1, optionsValue), WaitLogic.CLICKABLE, "optionsValue");
        return this;
    }
    public String getOptionsAttribute(){
        String OptionsValue = getStringValues(optionAttributeBox,WaitLogic.CLICKABLE, "optionBox");
        return OptionsValue;
    }

    //Verify dropdown values selection for WebCatalogAttribute
    public TextPage verifyWebCatalogTypeDropDownValues(String webCatalogValue){
        click(webCatalogAttributeBox,  WaitLogic.CLICKABLE, "webCatalogBox");
        WaitForMiliSec(2000);
        click(FieldDropDown, WaitLogic.CLICKABLE, "DropDown");
        WaitForMiliSec(2000);
        click(getElementByReplaceText(selectValue, webCatalogValue), WaitLogic.CLICKABLE, "webCatalogValue");
        return this;
    }
    public String getWebCatalogAttribute(){
        String webCatalogValue = getStringValues(webCatalogAttributeBox,WaitLogic.CLICKABLE, "webCatalogBox");
        return webCatalogValue;
    }

    //Verify dropdown values for CA Web catalog
    public TextPage verifyCAWebCatalogTypeDropDownValues(String webCatalogValue){
        click(CAWebCatalogAttributeBox,  WaitLogic.CLICKABLE, "webCatalogBox");
        WaitForMiliSec(2000);
        click(FieldDropDown1, WaitLogic.CLICKABLE, "DropDown");
        WaitForMiliSec(2000);
        click(getElementByReplaceText(selectValue1, webCatalogValue), WaitLogic.CLICKABLE, "webCatalogValue");
        return this;
    }
    public String getCAWebCatalogAttribute(){
        String webCatalogValue = getStringValues(CAWebCatalogAttributeBox,WaitLogic.CLICKABLE, "webCatalogBox");
        return webCatalogValue;
    }

    //verify given MessageType in MessageTypeAttribute feild
    public String verifyMessageTypeAttributeMessage()  {
        click(messageTypeAttributeBox, WaitLogic.CLICKABLE, "messageTypeBox");
        String messageTypeText = getStringValues(messageTypeAttributeBox,WaitLogic.CLICKABLE, "messageTypeBox");
        String DynamicTextMessage=addDynamicPattern(messageTypeText);
        WaitForMiliSec(2000);
        Javautils.selectAllAndDeletingThroughKeboard();
        WaitForMiliSec(5000);
        sendKeys(messageTypeTextFeild, DynamicTextMessage, WaitLogic.VISIBLE, "messageTypeFeild");
        WaitForMiliSec(2000);
        return DynamicTextMessage;
    }
    public String verifyMasterMessageType(){
        String messageTypeText = getStringValues(messageTypeAttributeBox,WaitLogic.CLICKABLE, "messageTypeBox");
        return  messageTypeText;
    }


    //verify hazardous flag is HazardousAttribute Feild
    public String getHazardousAttribute(){
        String hazardousValue = getStringValues(hazardousAttributeBox,WaitLogic.CLICKABLE, "hazardousBox");
        return hazardousValue;
    }

    //Verify Description feild in French is editable for US catalog
    public boolean isFrenchAttributeFeildsEditable(String fieldname) {
        By by = null;
        try {
            by = (By) LocalAttributePage.class.getField(fieldname).get(TextPage.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isClickable(by, fieldname);
    }

    //To select MainCategory of selected category in the structures group
    public TextPage selectMainCategory(String mainCategory){
        WaitForMiliSec(2000);
        click(getElementByReplaceText(mainCategoryDropdown, mainCategory), WaitLogic.CLICKABLE, "mainCategory");
        return this;
    }
    //To select SubCategory of selected category in the structures group
    public TextPage selectSubCategory(String subCategory){
        WaitForMiliSec(2000);
        click(getElementByReplaceText(subCategoryDropdown, subCategory), WaitLogic.CLICKABLE, "subCategory");
        return this;
    }

    public boolean isAllAttributesVisible(String fieldName) {
        By by = null;
        try {
            by = (By) TextPage.class.getField(fieldName).get(TextPage.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isVisible(by, fieldName);
    }


    public String getLastChangedBy(){
        String LastChangedBy = getStringValues(lastChangedByAttribute,WaitLogic.CLICKABLE, "lastChangedByText");
        return LastChangedBy;
    }
    public String getLastChangedOn(){
        String LastChangedOn = getStringValues(lastChangedOnAttribute,WaitLogic.CLICKABLE, "lastChangedOnText");
        return LastChangedOn;
    }

    //To select Filter button
    public TextPage SelectFilterButton(){
        click(filterButton, WaitLogic.CLICKABLE, "FilterButton");
        return this;
    }


}


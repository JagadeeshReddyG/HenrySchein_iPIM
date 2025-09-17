package com.pim.pages;

import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FieldSelectionPage extends BasePage {
    private final By fieldNameTextbox = By.xpath("//input[@class='v-textfield v-widget hpmw-treeSearch-fieldSelecton v-textfield-hpmw-treeSearch-fieldSelecton v-has-width v-textfield-prompt']");
    private final String headerData = "//span[text()='${variable}']";
    private final By addButton = By.xpath("//span[text()='Add']");
    private final By closeButton = By.xpath("//span[text()='Close']");
    private final By okButton = By.xpath("//span[text()='OK']");
    private final By languageTextbox = By.xpath("//span[contains(text(),'Language')]/../..//input[@class='v-filterselect-input']");
    private final By partnerDescriptionTextbox = By.xpath("//span[contains(text(),'Partner Description')]/../..//input[@class='v-filterselect-input']");
	public String valueSearchxpathfromDropdown = "//span[contains(text(),'${variable}')]";

    private final By productNotesLanguageTextbox= By.xpath("//span[contains(text(),'productNoteslanguage')]/../..//input[@class='v-filterselect-input']");
    private final By AlternateNumberCustomer = By.xpath("//*[@id='SelectFieldsDialog.LogicalKey.%logical-key.ArticleSalesType.LK.Buyer.name']/input");
    private final By fieldSelectionItems = By.xpath("//div[@class='v-label v-widget hpmw-select-fields-table-label v-label-hpmw-select-fields-table-label v-has-width v-has-height']");
    private final By fieldDeletebtn = By.xpath("(//td/div[@class='v-table-cell-wrapper']/div[@class='v-button v-widget hpmw-select-fields-delete-btn v-button-hpmw-select-fields-delete-btn'])[2]");
    private final By gTIN_Field = By.xpath("(//div[@class='v-tree-node-caption']//span[normalize-space(text())='GTIN'])[position()=1]");
    private final By pricingUOM_Field = By.xpath("(//div[@class='v-tree-node-caption']//span[normalize-space(text())='Pricing UOM'])");


    public FieldSelectionPage enterFieldName(String fieldname) {
        sendKeys(fieldNameTextbox, fieldname, WaitLogic.VISIBLE, "Field Name text box");
        return this;
    }

    public FieldSelectionPage clickHeaderText(String header){
        click(getElementByReplaceText(headerData, header), WaitLogic.CLICKABLE, "Header");
        return this;
    }

    public FieldSelectionPage clickAddButton(){
        WaitForMiliSec(1000);
        click(addButton, WaitLogic.CLICKABLE, "add buttton");
        return this;
    }
    public FieldSelectionPage clickCloseButton(){
        WaitForMiliSec(1000);
        click(closeButton, WaitLogic.CLICKABLE, "close buttton");
        return this;
    }

    public FieldSelectionPage clickOkButton(){
        click(okButton, WaitLogic.CLICKABLE, "ok buttton");
        return this;
    }

    public FieldSelectionPage enterLanguageValue(String fieldname) {
        sendKeys(languageTextbox, fieldname, WaitLogic.VISIBLE, "Language text box");
		WaitForMiliSec(2000);
		click(getElementByReplaceText(valueSearchxpathfromDropdown, fieldname), WaitLogic.CLICKABLE, fieldname);
        return this;
    }

    public FieldSelectionPage enterPartnerDescriptionValue(String fieldname) {
        sendKeys(partnerDescriptionTextbox, fieldname, WaitLogic.VISIBLE, "Partner Description text box");
		WaitForMiliSec(2000);
		click(getElementByReplaceText(valueSearchxpathfromDropdown, fieldname), WaitLogic.CLICKABLE, fieldname);
        return this;
    }

    /**
     * Name of the method: enterProductNotesLanguageValue
     * Description: method to select Language for Product Notes
     * Author:Jagadeesh
     */

    public FieldSelectionPage enterProductNotesLanguageValue(String fieldname){
        sendKeys(productNotesLanguageTextbox, fieldname, WaitLogic.VISIBLE, "Product Note language text box");
        WaitForMiliSec(2000);
        click(getElementByReplaceText(valueSearchxpathfromDropdown, fieldname), WaitLogic.CLICKABLE, fieldname);
        WaitForMiliSec(5000);
        return this;

    }

    public FieldSelectionPage enterCustomerName(String fieldname){
        WebElement element = DriverManager.getDriver().findElement(AlternateNumberCustomer);
        clearWithDelete(element);
        WaitForMiliSec(2000);
        sendKeys(AlternateNumberCustomer, fieldname, WaitLogic.VISIBLE, "Customer text box");
        WaitForMiliSec(2000);
        click(getElementByReplaceText(valueSearchxpathfromDropdown, fieldname), WaitLogic.CLICKABLE, fieldname);
        WaitForMiliSec(5000);
        return this;

    }

    /**
     * Name of the method: clearAllFieldsExceptItemNumandDesc
     * Description: method to clear all fields Except Item Number and Item Description
     * Author:Jagadeesh
     */

    public void clearAllFieldsExceptItemNumandDesc() {
        WaitForMiliSec(5000);
        while (true) {
            List<WebElement> webElements = DriverManager.getDriver().findElements(fieldSelectionItems);
            if (webElements.size() <= 1) {
                break;
            }
            WebElement removeBtn = DriverManager.getDriver().findElement(fieldDeletebtn);
            WaitForMiliSec(2000);
            removeBtn.click();
            WaitForMiliSec(2000);
        }
    }
    public FieldSelectionPage selectGTINField() {
        WaitForMiliSec(2000);
        click(gTIN_Field, WaitLogic.CLICKABLE, "GTIN Field");
        return this;
    }
    public FieldSelectionPage selectPricingUOMField() {
        WaitForMiliSec(2000);
        click(pricingUOM_Field, WaitLogic.CLICKABLE, "Pricing UOM Field");
        return this;
    }


}

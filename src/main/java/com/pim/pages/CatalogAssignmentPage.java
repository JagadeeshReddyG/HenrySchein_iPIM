package com.pim.pages;
import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;

public class CatalogAssignmentPage extends BasePage {

    private final By hsiItemCodeList = By.xpath("//div[@class='v-scrollable v-table-body-wrapper v-table-body']//tbody//td[2]/div");
    private final String itemList = "//div[@class='v-scrollable v-table-body-wrapper v-table-body']//tbody//td[2]/div[contains(text(),'${variable}')]";
    public By userDrivenValue = By.xpath("(//span[contains(text(),'User Driven')]/ancestor::td/following-sibling::td)[2]//div[@class='v-label v-widget hpmw-valueLabel v-label-hpmw-valueLabel v-label-undef-w']");
    public By exceptionListValue = By.xpath("(//span[contains(text(),'Exception List')]/ancestor::td/following-sibling::td)[2]//div[@class='v-label v-widget hpmw-valueLabel v-label-hpmw-valueLabel v-label-undef-w']");
    public By DentalCatalogList = By.xpath("(//span[contains(text(),'Dental Catalog List')]/ancestor::td/following-sibling::td)[2]//div[@class='v-label v-widget hpmw-valueLabel v-label-hpmw-valueLabel v-label-undef-w']");
    private final By UserDrivenDropdown = By.xpath("(//table//div[@class='v-filterselect-button'])[1]");
    private final By ExceptionListDropdown = By.xpath("(//table//div[@class='v-filterselect-button'])[2]");
    public By FieldDropDown = By.xpath("//table//div[@class='v-filterselect-button']");
    public By DropDownValues = By.xpath("//tbody/tr//td[@role='listitem']");

    private final String userAndExceptionListForAllCatalog = "//td[@aria-label='${variable}']";
    private final By SaveButton = By.xpath("//span[contains(text(),'SAVE')]");
    private final By TextFeild = By.xpath("//div[@class='v-filterselect v-widget']//input[@role='combobox']");
    public  By ZahnCatalogCanadianTab = By.xpath("//div[contains(text(),'Canadian Zahn Catalogs')]");
    public  By MedicalCatalogCanadianTab = By.xpath("//div[contains(text(),'Canadian Medical Catalogs')]");
    public  By DentalCatalogCanadianTab = By.xpath("//div[contains(text(),'Canadian Dental Catalogs')]");
    public By userDrivenField = By.xpath("(//span[text()='User Driven:']//following::td[@class='v-formlayout-contentcell'])[1]");
    public By exceptionListField = By.xpath("(//span[text()='Exception List:']//following::td[@class='v-formlayout-contentcell'])[1]");

    //Locators to clear already entered user driven and exception list catalogs
    private By clearUserDrivenSelectedCatalogs = By.xpath(
            "(//span[text()='User Driven:']//following::td[@class='v-formlayout-contentcell'])[1]//div[@class='v-slot']/div/div[1]/div/div[2]/div");
    private By listOfUserDrivenSelectedCatalogs = By.xpath(
            "(//span[text()='User Driven:']//following::td[@class='v-formlayout-contentcell'])[1]//div[@class='v-slot']/div/div/div/div[2]/div");
    private By clearExceptionListSelectedCatalogs = By.xpath(
            "(//span[text()='Exception List:']//following::td[@class='v-formlayout-contentcell'])[1]//div[@class='v-slot']/div/div[1]/div/div[2]/div");
    private By listofExceptionListSelectedcatalogs = By.xpath(
            "(//span[text()='Exception List:']//following::td[@class='v-formlayout-contentcell'])[1]//div[@class='v-slot']/div/div/div/div[2]/div");
    private String crossIconForUserDriven = "(//span[contains(text(),'User Driven:')]/ancestor::td/following-sibling::td)[2]//div[contains(text(),'${variable}')]/parent::div/following-sibling::div//div";


    //To search multiple item codes
    public List<String> selectMultipleItemCodes() {

        List<WebElement> hsiItemCodeListSize = DriverManager.getDriver().findElements(hsiItemCodeList);
        List<String> listHSIItemCode = new ArrayList<String>();
        for (WebElement itemCodeTableRows : hsiItemCodeListSize) {
            String itemCodeNumber = itemCodeTableRows.getText().trim();
            listHSIItemCode.add(itemCodeNumber);
        }
        for (int i = 0; i <= listHSIItemCode.size() - 1; i++) {
            String itemCode = listHSIItemCode.get(i);
            System.out.println(listHSIItemCode.get(i));
            click(getElementByReplaceText(itemList, itemCode), WaitLogic.CLICKABLE, "ItemNumber");
        }

        WaitForMiliSec(4000);
        return listHSIItemCode;
    }

    //To get selected user driven rule
    public List<String> getUserDrivenDentalCatalogs() {
        List<WebElement> listSize = DriverManager.getDriver().findElements(userDrivenValue);
        List<String> listUserDriven = new ArrayList<String>();
        for (WebElement catalogRules : listSize) {
            String getUserDriven = catalogRules.getText().trim();
            listUserDriven.add(getUserDriven);
        }
        return listUserDriven;

    }

    //To get selected Exception list
    public List<String> getExceptionListForDentalCatalog(){
        List<WebElement> listSize = DriverManager.getDriver().findElements(exceptionListValue);
        List<String> listExceptionList = new ArrayList<String>();
        for (WebElement catalogRules : listSize) {
            String getExceptionList = catalogRules.getText().trim();
            listExceptionList.add(getExceptionList);
        }
        return listExceptionList;

    }

    //To get Dental Catalog list of consolidated catalogs
    public List<String> getConsolidatedDentalCatalogList(){
        List<WebElement> listSize = DriverManager.getDriver().findElements(DentalCatalogList);
        List<String> dentalCatalogList = new ArrayList<String>();
        for (WebElement catalogRules : listSize) {
            String getDentalCatalogList = catalogRules.getText().trim();
            dentalCatalogList.add(getDentalCatalogList);
        }
        return dentalCatalogList;

    }

    //To Select User Driven Catalog from the dropdown
    public CatalogAssignmentPage SelectDentalUserDrivenCatalog(String catalogRuleType) {
        clearSelectedFields(listOfUserDrivenSelectedCatalogs, clearUserDrivenSelectedCatalogs);
        WaitForMiliSec(5000);
            click(UserDrivenDropdown, WaitLogic.CLICKABLE, "DentalUserDrivenDropdown");
            WaitForMiliSec(2000);
            click(getElementByReplaceText(userAndExceptionListForAllCatalog,catalogRuleType), WaitLogic.CLICKABLE, "userDrivenRuleType");
            WaitForMiliSec(5000);
            try {
				keyboardTabAndEnter();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            click(SaveButton, WaitLogic.CLICKABLE, "saveButton");
            WaitForMiliSec(2000);
        return this;
    }
    //To Select Exception List Catalog from the dropdown
    public CatalogAssignmentPage SelectDentalExceptionList(String catalogRuleType) {
        clearSelectedFields(listofExceptionListSelectedcatalogs, clearExceptionListSelectedCatalogs);
        WaitForMiliSec(5000);
        click(ExceptionListDropdown, WaitLogic.CLICKABLE, "DentalExceptionListDropdown");
            WaitForMiliSec(2000);
            click(getElementByReplaceText(userAndExceptionListForAllCatalog,catalogRuleType), WaitLogic.CLICKABLE, "exceptionListRuleType");
            WaitForMiliSec(2000);
            try {
				keyboardTabAndEnter();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            click(SaveButton, WaitLogic.CLICKABLE, "saveButton");
            WaitForMiliSec(2000);
        return this;
    }

    //To verify visibility of dental,medical and zahn tabs
    public boolean isAllAttributesVisible(String fieldName) {
        By by = null;
        try {
            by = (By) CatalogAssignmentPage.class.getField(fieldName).get(CatalogAssignmentPage.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isVisible(by, fieldName);
    }

    public void clearSelectedFields(By listOfSelectedCatalogs, By clearSelectedCatalogs) {

        WaitForMiliSec(5000);
        List<WebElement> webElements = DriverManager.getDriver().findElements(listOfSelectedCatalogs);
        if (webElements != null) {

            for (int i = 1; i <= webElements.size(); i++) {
                WaitForMiliSec(5000);
                DriverManager.getDriver().findElement(clearSelectedCatalogs).click();
            }
        }
    }

    //To clear and verify User Driven list catalog
    public List<String> clearAndVerifyUserDrivenList(){
        click(userDrivenField, WaitLogic.CLICKABLE, "UserDrivenField");
        WaitForMiliSec(5000);

    //If User Driven Values are selected
        clearSelectedFields(listOfUserDrivenSelectedCatalogs, clearUserDrivenSelectedCatalogs);
        WaitForMiliSec(5000);

        click(FieldDropDown, WaitLogic.CLICKABLE, "UserDrivenFieldDropDown");
        WaitForMiliSec(5000);

            List<WebElement> listSize = DriverManager.getDriver().findElements(DropDownValues);
            List<String> userDrivenList = new ArrayList<String>();
            for (WebElement catalogRules : listSize) {
                String getUserDrivenList  = catalogRules.getText().trim();
                userDrivenList.add(getUserDrivenList);
            }
            return userDrivenList;

    }

    //To clear and verify Exception list catalog
    public List<String> clearAndVerifyExceptionList(){
        click(exceptionListField, WaitLogic.CLICKABLE, "ExceptionListField");
        WaitForMiliSec(5000);

        //If Exception List Values are selected
        clearSelectedFields(listofExceptionListSelectedcatalogs, clearExceptionListSelectedCatalogs);
        WaitForMiliSec(5000);
        click(FieldDropDown, WaitLogic.CLICKABLE, "UserDrivenFieldDropDown");
        WaitForMiliSec(5000);

        List<WebElement> listSize = DriverManager.getDriver().findElements(DropDownValues);
        List<String> ExceptionList = new ArrayList<String>();
        for (WebElement catalogRules : listSize) {
            String getExceptionList = catalogRules.getText().trim();
            ExceptionList.add(getExceptionList);
        }
        return ExceptionList;
    }


}




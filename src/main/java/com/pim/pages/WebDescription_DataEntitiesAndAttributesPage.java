package com.pim.pages;

import com.pim.enums.LogType;
import com.pim.enums.WaitLogic;

import org.openqa.selenium.By;

import static com.pim.reports.FrameworkLogger.log;

public class WebDescription_DataEntitiesAndAttributesPage extends BasePage {

    private final By caCatalog = By.xpath("//div[normalize-space()='CA Catalog']");

    private final By filterBy = By
            .xpath("//input[@class='v-textfield v-widget hpmw-quicksearch-input v-textfield-hpmw-quicksearch-input v-has-width v-textfield-prompt']");

    private final By webDescription = By.xpath("//div[contains(text(),'Web Description')]");
    private final By divisionSelect = By.xpath("//span[contains(text(),'Division')]/ancestor::tr//td[@class='v-formlayout-contentcell']//div[@class='v-filterselect-button']");
    private final By dentalDivision = By.xpath("//span[normalize-space()='Dental']");
    private final By medicalDivision = By.xpath("//span[normalize-space()='Medical']");
    private final By zahnDivision = By.xpath("//span[normalize-space()='Zahn']");
    public static By fullDisplayDesc = By.xpath("//span[contains(text(),'Full Display Description:')]");
    private final By fullDisplayDescField = By.xpath("(//span[contains(text(),'Full Display Description:')]/ancestor:: tr//td[@class='v-formlayout-contentcell']//div)[7]");
    public static By abbreviatedDisplayDesc = By.xpath("//span[contains(text(),'Abbreviated Display Description:')]");
    private final By abbreviatedDisplayDescField = By.xpath("(//span[contains(text(),'Abbreviated Display Description:')]/ancestor:: tr//td[@class='v-formlayout-contentcell']//div)[7]");
    private final By lookAheadSearchDesc = By.xpath("//span[contains(text(),'Look Ahead Search Description:')]");
    private final By lookAheadSearchDescField = By.xpath("(//span[contains(text(),'Look Ahead Search Description:')]/ancestor:: tr//td[@class='v-formlayout-contentcell']//div)[7]");


    public WebDescription_DataEntitiesAndAttributesPage clickOnCACatalogSubMenu() {
        WaitForMiliSec(10000);
        click(caCatalog, WaitLogic.CLICKABLE,"CA Catalog sub Menu");
        log(LogType.INFO,"Click on CA Catalog Sub Menu");
        return this;
    }

    public WebDescription_DataEntitiesAndAttributesPage applyFilterByOptionInSearchPage(String itemNumber) {
        WaitForMiliSec(2000);
        sendKeys(filterBy, itemNumber, WaitLogic.PRESENCE, "itemNumber");
        log(LogType.INFO,"Taking ItemNumber in Filter By option for filtering ItemNumber from CA Catalog");
        return this;
    }

    public WebDescription_DataEntitiesAndAttributesPage clickOnWebDescriptionTab() {
        WaitForMiliSec(2000);
        click(webDescription, WaitLogic.CLICKABLE,"Web Description Tab");
        log(LogType.INFO,"Click on Web Description Tab");
        return this;
    }

    public WebDescription_DataEntitiesAndAttributesPage clickOnDivisionSelectDropdown() {
        WaitForMiliSec(2000);
        click(divisionSelect, WaitLogic.CLICKABLE,"Division Select DropDown");
        log(LogType.INFO,"Click on Division Select DropDown");
        return this;
    }

    public WebDescription_DataEntitiesAndAttributesPage clickOnDentalDivisionOption() {
        WaitForMiliSec(2000);
        click(dentalDivision, WaitLogic.CLICKABLE,"Dental Division");
        log(LogType.INFO,"Selecting Dental Division");
        return this;
    }

    public WebDescription_DataEntitiesAndAttributesPage clickOnMedicalDivisionOption() {
        WaitForMiliSec(2000);
        click(medicalDivision, WaitLogic.CLICKABLE,"Medical Division");
        log(LogType.INFO,"Selecting Medical Division");
        return this;
    }

    public WebDescription_DataEntitiesAndAttributesPage clickOnZahnDivisionOption() {
        WaitForMiliSec(2000);
        click(zahnDivision, WaitLogic.CLICKABLE,"Zahn Division");
        log(LogType.INFO,"Selecting Zahn Division");
        return this;
    }
    public String captureFullDisplayDescriptionFieldValue() {
        String fullDisplayDescValue = getTextValue(fullDisplayDescField,WaitLogic.PRESENCE, "Full Display Description Field Value");
        log(LogType.INFO,"Capturing Full Display Description Field Value ");
        return fullDisplayDescValue;
    }

    public String captureAbbreviatedDisplayDescriptionFieldValue() {
        String abbreviatedDisplayDescValue = getTextValue(abbreviatedDisplayDescField,WaitLogic.PRESENCE, "Abbreviated Display Description Field Value");
        log(LogType.INFO,"Capturing Abbreviated Display Description Field Value ");
        return abbreviatedDisplayDescValue;
    }





}

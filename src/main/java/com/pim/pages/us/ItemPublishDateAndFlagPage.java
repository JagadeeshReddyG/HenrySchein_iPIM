package com.pim.pages.us;



import com.pim.enums.LogType;
import com.pim.enums.WaitLogic;

import com.pim.pages.BasePage;
import org.openqa.selenium.By;


import static com.pim.reports.FrameworkLogger.log;


public class ItemPublishDateAndFlagPage extends BasePage {

    public static By publishFlagField = By.xpath("//*[contains(text(),'Publish Flag')]/../../..//td[@class='v-formlayout-contentcell']");
    private final By JdeDesContent = By.xpath("//tr[@class='v-table-row']//td[3]");
    private final By dentalCatalog = By.xpath("//div[contains(text(),'Dental Catalog')]");
    private final By medicalCatalog = By.xpath("//div[contains(text(),'Medical Catalog')]");
    private final By zahnCatalog = By.xpath("//div[contains(text(),'Zahn Catalog')]");
    private final By specialMarketsCatalog = By.xpath("//div[contains(text(),'Special Markets Catalog')]");
    private final By publishFlag = By.xpath("//span[@for='gwt-uid-34']");
    private final By publishflag = By.xpath("//span[contains(text(),'Publish Flag')]");
    public static By publishDateField = By.xpath("//*[contains(text(),'Publish Date')]/../../..//td[@class='v-formlayout-contentcell']");
    private final By publishDate = By.xpath("//span[@for='gwt-uid-36']");
    public  static By userDrivenfield = By.xpath("//*[contains(text(),'User Driven')]/../../..//td[@class='v-formlayout-contentcell']");

    public ItemPublishDateAndFlagPage clickOnDentalCatalogTab() {
        WaitForMiliSec(1000);
        click(dentalCatalog, WaitLogic.CLICKABLE,"Dental Catalog");
        return this;
    }

    public ItemPublishDateAndFlagPage clickOnJDEdescriptionContent() {
        WaitForMiliSec(1000);
        click(JdeDesContent, WaitLogic.CLICKABLE,"JDE Description Content");
        return this;
    }

    public ItemPublishDateAndFlagPage clickOnMedicalCatalogTab() {
        WaitForMiliSec(1000);
        click(medicalCatalog, WaitLogic.CLICKABLE,"Medical Catalog");
        return this;
    }

    public ItemPublishDateAndFlagPage clickOnZahnCatalogTab() {
        WaitForMiliSec(1000);
        click(zahnCatalog, WaitLogic.CLICKABLE,"Zahn Catalog");
        return this;
    }

    public ItemPublishDateAndFlagPage clickOnSpecialMarketsCatalogTab() {
        WaitForMiliSec(1000);
        click(specialMarketsCatalog, WaitLogic.CLICKABLE,"Special Markets Catalog");
        return this;
    }

    public ItemPublishDateAndFlagPage verifyEditPublishFlagField(){
        isClickable(publishFlagField,"Publish Flag");
        log(LogType.INFO,"Publish Flag Field Is editable---Validation successfully");
        return this;
    }
    public ItemPublishDateAndFlagPage verifyEditPublishDateField(){
        isClickable(publishDateField,"Publish Date");
        log(LogType.INFO,"Publish Date Field Is editable---Validation successfully");
        return this;
    }

    public String captureUserDrivenValue() {
        String userDriven = getTextValue(userDrivenfield,WaitLogic.PRESENCE, "User Driven");
        return userDriven;
    }

    public String capturePublishFlagValue() {
        String publishFlag = getTextValue(publishFlagField,WaitLogic.PRESENCE, "Publish Flag");
        return publishFlag;
    }

    public String capturePublishDateValue() {
        String PublishDate = getTextValue(publishDateField,WaitLogic.PRESENCE, "Publish Date");
        return PublishDate;
    }



}

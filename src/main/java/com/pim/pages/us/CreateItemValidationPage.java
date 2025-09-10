package com.pim.pages.us;


import com.pim.enums.WaitLogic;

import com.pim.pages.BasePage;


import org.openqa.selenium.By;


public class CreateItemValidationPage extends BasePage {

    private final By catalogs = By.xpath("//span[normalize-space()='Catalogs']/..");
    private final By usCatalog = By.xpath("//div[normalize-space()='US Catalog']");
    private final By caCatalog = By.xpath("//div[normalize-space()='CA Catalog']");
    private final By masterCatalog = By.xpath("//span[normalize-space()='Master catalog']");
    private final By filterBy = By.xpath("//input[@class='v-filterselect-input']");
    private final By filterSearchField = By.xpath("//input[@id='article_search_field']");
    private final By filterSelectButton = By.xpath("//div[@class='v-filterselect-button']");
    private final By JDEDescription = By.xpath("//span[normalize-space()='JDE Description']");
    private final By filterButton = By.xpath("//div[@id='article_search_button']//span[@class='v-button-wrap']");
    private final By JDEdescriptionContent = By.xpath("//tr[@class='v-table-row']//td[3]");
    private final By uncheckedIcon = By.xpath("//span[@class='v-menubar-menuitem v-menubar-menuitem-hpmw-search v-menubar-menuitem-unchecked']");

    public CreateItemValidationPage clickOnCatalogsMenu() {
        WaitForMiliSec(10000);
        click(catalogs, WaitLogic.CLICKABLE,"CatalogsMenu");
        return this;
    }


    public CreateItemValidationPage clickOnUsCatalogSubMenu() {
        WaitForMiliSec(10000);
        click(usCatalog, WaitLogic.CLICKABLE,"US Catalog sub Menu");
        return this;
    }

    public CreateItemValidationPage clickOnUncheckedIcon() {
        WaitForMiliSec(10000);
        click(uncheckedIcon, WaitLogic.CLICKABLE,"Unchecked Icon");
        return this;
    }

    public CreateItemValidationPage selectFilterByOption() {
        WaitForMiliSec(10000);
        click(filterSelectButton, WaitLogic.CLICKABLE,"Filter Select Button");
        click(JDEDescription, WaitLogic.CLICKABLE,"JDE Description");
        return this;
    }

    public CreateItemValidationPage enterFilterSearchField(String filtersearch) {
        WaitForMiliSec(10000);
        sendKeys(filterSearchField, filtersearch, WaitLogic.PRESENCE, "filterSearchField");
        return this;
    }
    public  CreateItemValidationPage clickOnFilterButton()  {
        click(filterButton, WaitLogic.CLICKABLE,"Filter Button");
        return this;
    }

    public String captureJDEdescriptionTextValue() {
        String jde = getTextValue(JDEdescriptionContent,WaitLogic.PRESENCE, "JDE Description content");
        return jde;
    }
}

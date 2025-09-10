package com.ipim.page;

import com.pim.driver.DriverManager;
import com.pim.enums.WaitLogic;
import com.pim.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class iPimMainPage extends BasePage {
    private final By usFlag = By.xpath("//div[contains(@class, 'flag-display flag-us')]");
    private final By caFlag = By.xpath("//div[contains(@class, 'flag-display flag-ca')]");
    private final By dentiraUSFlag = By.xpath("//div[@class='flag-display flag-dentira' and normalize-space()='US']");
    private final By dentiraCAFlag = By.xpath("//div[@class='flag-display flag-dentira' and normalize-space()='CA']");
    private final By lakehouseUSFlag = By.xpath("//div[@class='flag-display flag-lakehouse' and normalize-space()='US']");
    private final By itemCode_Input_Field = By.id("itemCode");
    private final By searchBtn = By.xpath("//i[@class='fas fa-search search-icon']");
    private final By timestamp_of_Records = By.xpath("//*[@class='list-group-item list-group-item-action result-item']/div/h5");
    private final By raw_Tab = By.id("raw-tab");
    private final By jsonContent = By.id("jsonContent");


    public iPimMainPage clickUsFlag()
    {
        click(usFlag, WaitLogic.CLICKABLE, "Click US Flag");
        return this;
    }

    public iPimMainPage clickCaFlag()
    {
        click(caFlag, WaitLogic.CLICKABLE, "Click CA Flag");
        return this;
    }
    public iPimMainPage clickdentiraUSFlag()
    {
        click(dentiraUSFlag, WaitLogic.CLICKABLE, "Click Dentira US Flag");
        return this;
    }
    public iPimMainPage clickdentiraCAFlag()
    {
        click(dentiraCAFlag, WaitLogic.CLICKABLE, "Click Dentira CA Flag");
        return this;
    }
    public iPimMainPage clickLakehouseUSFlag()
    {
        click(lakehouseUSFlag, WaitLogic.CLICKABLE, "Click Lakehouse US Flag");
        return this;
    }

    public iPimMainPage enterItemCodeNumber(String itemcodenumber) {
        sendKeys(itemCode_Input_Field, itemcodenumber, WaitLogic.PRESENCE, "itemCodeNumber");
        WaitForMiliSec(3000);
        return new iPimMainPage();

    }
    public iPimMainPage clickSearchBtn()
    {
        click(searchBtn, WaitLogic.CLICKABLE, "Click Search Button");
        return this;
    }
    public List<WebElement> getListOfRecords()
    {
        List<WebElement> records = DriverManager.getDriver().findElements(timestamp_of_Records);
        return records;
    }
    public iPimMainPage clickRawTab()
    {
        click(raw_Tab, WaitLogic.CLICKABLE, "Clicked RAW Tab");
        return this;
    }
    public iPimMainPage getJsonContent()
    {
        String jsonContext = getStringValues(jsonContent, WaitLogic.VISIBLE, "get JsonContent");
        System.out.println("JSON Content: "+jsonContext);
        return this;
    }


}

package com.pim.pages;
import static com.pim.reports.FrameworkLogger.log;

import java.util.*;

import com.pim.factories.ExplicitWaitFactory;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import com.pim.driver.DriverManager;
import com.pim.enums.LogType;
import com.pim.enums.WaitLogic;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.Javautils;

public class PricePage extends BasePage {

    Javautils javautils = new Javautils();
    private final By validForm = By.xpath("(//div/a[text()='Valid from'])[1]");
    private final By PrimaryListPrice = By.xpath("(//div[contains(text(),'Net customer price primary')])[1]/..//following::td[1]/div");
    private final By WebPrice_Price = By.xpath("(//div[contains(text(),'Web Price')])[1]/..//following::td[1]/div");
    private final By WebPrice_Currency = By.xpath("(//div[contains(text(),'Web Price')])[1]/..//following::td[2]/div");
    private final By WebPrice_ValidFrom= By.xpath("(//div[contains(text(),'Web Price')])[1]/..//following::td[4]/div");
    private final By WebPrice_ValidTo= By.xpath("(//div[contains(text(),'Web Price')])[1]/..//following::td[5]/div");
    private final By RefreshIcon = By.xpath("(//span[contains(@class,'hpmw-refresh')])[1]");
    private final By divisionalPrice_Price = By.xpath("(//div[contains(text(),'Divisional Price')])[1]/..//following::td[1]/div");
    private final By divisionalPrice_Currency = By.xpath("(//div[contains(text(),'Divisional Price')])[1]/..//following::td[2]/div");
    private final By divisionalPrice_ValidFrom= By.xpath("(//div[contains(text(),'Divisional Price')])[1]/..//following::td[4]/div");
    private final By divisionalPrice_ValidTo= By.xpath("(//div[contains(text(),'Divisional Price')])[1]/..//following::td[5]/div");
    private final By divisionalPrice_Customer= By.xpath("(//div[contains(text(),'Divisional Price')])[1]/..//following::td[6]/div");



    public PricePage sortPriceByValidFrom() {
        WaitForMiliSec(3000);
        click(validForm, WaitLogic.CLICKABLE, "validForm");
        WaitForMiliSec(3000);
        click(validForm, WaitLogic.CLICKABLE, "validForm");
        WaitForMiliSec(3000);
        return this;
    }

    public String GetTheListPrice() {
        String fullDisplayDescription = getStringValues(PrimaryListPrice, WaitLogic.VISIBLE, "get List Price Value");
        return fullDisplayDescription.trim();
    }
    public String getWebPrice() {
        String webPrice = getStringValues(WebPrice_Price, WaitLogic.VISIBLE, "get Web Price Value");
        return webPrice.trim();
    }
    public String getWebPriceCurrency() {
        String webPriceCurrency = getStringValues(WebPrice_Currency, WaitLogic.VISIBLE, "get Web Price Currency Value");
        return webPriceCurrency.trim();
    }
    public String getWebPriceStartDate() {
        String webPriceStartDate = getStringValues(WebPrice_ValidFrom, WaitLogic.VISIBLE, "get Web Price Start Date Value");
        return webPriceStartDate.trim();
    }
    public String getWebPriceEndDate() {
        String webPriceEndDate = getStringValues(WebPrice_ValidTo, WaitLogic.VISIBLE, "get Web Price End Date Value");
        return webPriceEndDate.trim();
    }

    public void ClickRefreshIcon() {
        WaitForMiliSec(3000);
        click(RefreshIcon, WaitLogic.CLICKABLE, "RefreshIcon");

    }
    public String getDivisionalPrice() {
        String divisionalPrice = getStringValues(divisionalPrice_Price, WaitLogic.VISIBLE, "get Divisional Price Value");
        return divisionalPrice.trim();
    }
    public String getDivisionalPriceCurrency() {
        String divisionalPriceCurrency = getStringValues(divisionalPrice_Currency, WaitLogic.VISIBLE, "get Divisional Price Currency Value");
        return divisionalPriceCurrency.trim();
    }
    public String getDivisionalPriceStartDate() {
        String divisionalPriceStartDate = getStringValues(divisionalPrice_ValidFrom, WaitLogic.VISIBLE, "get Divisional Price Start Date Value");
        return divisionalPriceStartDate.trim();
    }
    public String getDivisionalPriceEndDate() {
        String divisionalPriceEndDate = getStringValues(divisionalPrice_ValidTo, WaitLogic.VISIBLE, "get Divisional Price End Date Value");
        return divisionalPriceEndDate.trim();
    }
    public String getDivisionalPricCustomer() {
        String divisionalPriceCust = getStringValues(divisionalPrice_Customer, WaitLogic.VISIBLE, "get Divisional Price Division Value");
        return divisionalPriceCust.trim();
    }



}

package com.pim.pages;
import static com.pim.reports.FrameworkLogger.log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final By RefreshIcon = By.xpath("(//span[contains(@class,'hpmw-refresh')])[1]");

    public PricePage sortPriceByValidFrom() {
        WaitForMiliSec(3000);
        click(validForm, WaitLogic.CLICKABLE, "validForm");
        WaitForMiliSec(3000);
        click(validForm, WaitLogic.CLICKABLE, "validForm");
        WaitForMiliSec(3000);
        return this;
    }

    public String GetTheListPrice() {
        String fullDisplayDescription = getStringValues(PrimaryListPrice, WaitLogic.VISIBLE, "get FullDisplayDescription Value");
        return fullDisplayDescription.trim();

    }

    public void ClickRefreshIcon() {
        WaitForMiliSec(3000);
        click(RefreshIcon, WaitLogic.CLICKABLE, "RefreshIcon");

    }


}
